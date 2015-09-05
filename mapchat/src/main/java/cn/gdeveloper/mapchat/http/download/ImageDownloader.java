package cn.gdeveloper.mapchat.http.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public final class ImageDownloader {
	
	private static final String TAG 		= "CacheManager" ;
	private static final int FADE_IN_TIME	= 200 ;
	private static final int TIME_OUT		= 15 * 1000 ;
	
	public static enum State{STATE_ON,STATE_OFF}
	
	private static ImageDownloader _instance ;
	
	private int mMemCacheSize = 5 * 1024;
	private String mCachePath ;
	private ImageCache mImageCache ;
	
	private boolean mExitTasksEarly = false;
    protected boolean mPauseWork = false;
    private final Object mPauseWorkLock = new Object();
    private final Object mDiskCacheLock = new Object();
    private final ResultDrawable mResult = new ResultDrawable() ;
    
    /** default Bitmap */
	private final HashMap<Integer, Bitmap> mDefaultBitmap = new HashMap<Integer, Bitmap>() ;
    
    private Context 	mContext ;
    private Resources 	mResources ;
    private State		mState = State.STATE_ON ;

	private ImageDownloader(Context context,String cacheName,float percent){
		
		 if (percent < 0.05f || percent > 0.8f) {
             throw new IllegalArgumentException("setMemCacheSizePercent - percent must be "
                     + "between 0.05 and 0.8 (inclusive)");
         }
		 
		 if(context == null){
			 throw new IllegalArgumentException("Context is null");
		 }
		 
		 mContext			= context ;
		 mResources			= context.getResources() ;
		 mCachePath 	 	=  getDiskCacheDir(context,cacheName);
		 mMemCacheSize 		=  Math.round(percent * Runtime.getRuntime().maxMemory() / 1024);
		 mImageCache		= new ImageCache(mMemCacheSize,mCachePath);
		 
		 Log.d(TAG, String.format("CachePath=%s;memCacheSize=%d", mCachePath,mMemCacheSize));
	}
	
	private boolean isWifiConnected(Context context) {  
	     if (context != null) {  
	         ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	         NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
	         if (mWiFiNetworkInfo != null) {  
	             return mWiFiNetworkInfo.isAvailable();  
	         }  
	     }  
	     return false;  
	 }
	
	private static String getDiskCacheDir(Context context, String uniqueName) {
		final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? 
                				getExternalCacheDir(context).getPath()
                                : context.getCacheDir().getPath();

        return cachePath + File.separator + uniqueName ;
    }
    
	private static File getExternalCacheDir(Context context) {
        final String cacheDir = "/" + context.getPackageName() + "/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }
	
	public static ImageDownloader create(Context context,String cacheName,float percent){
		
		if(_instance == null){
			_instance = new ImageDownloader(context, cacheName, percent);
		}
		
		return _instance ;
	}
	
	public static ImageDownloader getImageDownloader(){
		return _instance ;
	}

	public void clearDiskCacheFile() {

		Log.d(TAG, "clearDiskCache, It will remove all cache file from disk");
		
		mDefaultBitmap.clear() ;

		if (mImageCache != null) {
			mImageCache.clearCacheFile() ;
		}
	}
	
	/** get Cache LocalPath*/
	public String getCacheLocalPath(String url){
		
		if(url == null || url.isEmpty()) return null ;
		
		return mImageCache != null ? mImageCache.getDiskCachPath(url) : null ;
	}
	
	public final void downloadBitmap(ImageItem item,ImageView imageView,int rid,IDownloaderCallback callback,boolean fadeInBitmap){
		
		final HashMap<Integer, Bitmap> map = mDefaultBitmap ;
		Bitmap bitmap = null ;
		
		if(rid > 0 ){
			bitmap = map.get(rid) ;
			if(bitmap == null){
				bitmap	= BitmapFactory.decodeResource(mResources, rid);
				map.put(rid, bitmap) ;
			}
		}
		
		// download bitmap for imageview
		downloadBitmap(item, imageView, bitmap, callback, fadeInBitmap ? bitmap != null : false);
	}
	
	public final void downloadBitmap(ImageItem item,ImageView imageView,Bitmap defaultBitmap,IDownloaderCallback callback,boolean fadeInBitmap){
		
		// check ImageItem
		if (item == null) {
			Log.e(TAG, "zhouwei:Item is null");
            return;
        }
		
		// 1. 检测本地高清图片是否存在? (是-->2) | (否-->6)
		// 2. 从缓存中查找高清图片 --> 3
		// 3. 如果存在高清 (是--> 4) | (否-->5)
		// 4. 从缓存中取高清
		// 5. 下载高清
		// 6. 检测下载高清是否打开
		
		// 选择图片地址
		final ImageCache imageCache = mImageCache ;
		final String validUrl		= item.getValidUrl() ;
		String unique 			= item.getValidUrl() ;
		
		// 检测有效地址
		item.setValidUrl(validUrl);
		// 使用地址
		unique 		= item.getValidUrl() ;
        BitmapDrawable value = null;
        
        // 是否有效地址
        if(unique == null || unique.isEmpty()){
        	Log.e(TAG, "Item Valid error");
        	return ;
        }
        
        // 从缓存中查找
        if (imageCache != null) {
        	// 自定义图片
        	if(callback != null){
        		BitmapDrawable src = imageCache.getBitmapFromMemCache(unique);
        		if(src != null && src.getBitmap() != null){
        			Bitmap des = callback.handleBitmap(item, src.getBitmap());
        			if(des != null){
        				value = new BitmapDrawable(des);
        			}
        		}
        	} else {
        		value = imageCache.getBitmapFromMemCache(unique);
        	}
        }

        // 设置图片
        if (value != null) {
            imageView.setImageDrawable(value);
            return ;
        } 
        
        // download on | off
		if(mState == State.STATE_OFF){
			Log.w(TAG, "Downloader is off");
			return ;
		}
        
        // download bitmap
        if (cancelPotentialWork(item, imageView)) 
        {
        	
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(mResources, defaultBitmap,task);
            imageView.setImageDrawable(asyncDrawable);

            // parameter list : item,defaultBitmap,callback,FadeIn
            task.executeOnExecutor(AsyncTask.DUAL_THREAD_EXECUTOR, item,defaultBitmap,callback,fadeInBitmap);
        }
	}
	
	public Bitmap getBitmapFromCache(String url){
		
		Bitmap bitmap = null ;
		
		final ImageCache imageCache = mImageCache ;
		
		if(imageCache != null){
			
			BitmapDrawable drawable = imageCache.getBitmapFromMemCache(url) ;
			
			if(drawable != null){
				return drawable.getBitmap() ;
			}
		}
		
		return bitmap ;
	}
	
	/**
	 * 
	 * @param data
	 * @param imageView
	 * @return
	 */
	private static boolean cancelPotentialWork(ImageItem data, ImageView imageView) {
    	
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
        	
            final ImageItem bitmapData = bitmapWorkerTask.data;
            
            if (bitmapData == null || !bitmapData.getValidUrl().equals(data.getValidUrl())) {
                bitmapWorkerTask.cancel(true);
            } else {
            	return false;
            }
        }
        
        return true;
    }
    
    public void setState(State newState){
    	
    	if(mState != newState){
    		mState	= newState ;
    	}
    }
    
    public State getState(){
    	return mState ;
    }
    
	public void onStart() {
		mExitTasksEarly = false;
		setPause(false);
	}
	
	public void onPause(){
		setPause(true);
	}
	
	public void onResume(){
		setPause(false);
	}
	
	/**
	 * 重置缓存
	 */
	public synchronized void onReset(){
		
		Log.d(TAG, "onReset Cache app=" + mContext.getPackageName());
		
		// 停止
		setState(State.STATE_OFF) ;
		onDestory();
		
		if(mImageCache != null){
			mImageCache.clearMemoryCache() ;
		}
		
		// 开始
		setState(State.STATE_ON) ;
		onStart() ;
		
		// 重新加载磁盘缓存
		if (mImageCache != null) {
			mImageCache.loadDiskCache();
		}
	}
	
	public void onDestory(){
		
		 mExitTasksEarly = true;
		 
		 setPause(true);
	}
	
	private final void setPause(boolean pause) {

		if (mPauseWork == pause)
			return;

		synchronized (mPauseWorkLock) {
			mPauseWork = pause;
			if (!mPauseWork) {
				mPauseWorkLock.notifyAll();
			}
		}
	}
	
	private void setImageDrawable(boolean fadeIn,ImageView imageView, Drawable drawable,Bitmap defBitmap) {
    	
        if (fadeIn) {
        	
            // 淡入效果
            final TransitionDrawable td =
                    new TransitionDrawable(new Drawable[] {
                            new ColorDrawable(android.R.color.transparent),
                            drawable
                    });
            
            // 设置默认背景
            imageView.setBackgroundDrawable(new BitmapDrawable(mResources, defBitmap));

            imageView.setImageDrawable(td);
            td.startTransition(FADE_IN_TIME);
        } else {
            imageView.setImageDrawable(drawable);
        }
    }
    
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
    	
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        
        return null;
    }
    
    /** 后台线程处理的对象 */
    private static final class ResultDrawable implements Cloneable {
    	
    	public ImageItem data ;
    	public BitmapDrawable drawable ;
    	public Bitmap defBitmap ;
    	public IDownloaderCallback callback ;
    	public boolean fadeIn ;
    	
    	public ResultDrawable(){
    		data		= null ;
    		drawable	= null ;
    		defBitmap	= null ;
    		callback	= null ;
    		fadeIn		= false ;
    	}
    	
		@Override
		public ResultDrawable clone(){
			try{
				return (ResultDrawable)super.clone();
			} catch(CloneNotSupportedException e){
				return new ResultDrawable() ;
			}
		}
    }
	
	////////////////////////////////////////////////////////////////////////////////////////
	 /**
     * The actual AsyncTask that will asynchronously process the image.
     */
    private final class BitmapWorkerTask extends AsyncTask<Object, Void, ResultDrawable> {
    	
        private ImageItem data;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            imageViewReference 	= new WeakReference<ImageView>(imageView);
        }

        /**
         * Background processing.
         */
        @Override
        protected ResultDrawable doInBackground(Object... params) {
        	
        	final ResultDrawable out 	= mResult.clone() ;
        	final ImageCache imageCache = mImageCache ;
        	
        	out.data 		= (ImageItem) params[0];
        	out.defBitmap 	= (Bitmap) params[1];
        	out.callback 	= (IDownloaderCallback) params[2];
        	out.fadeIn 		= (Boolean) params[3];
        	
        	data 			= out.data ;
            
        	// 默认选择高清
        	final  String dataString = data.getValidUrl() ;
            
            final String key		= dataString ;
            Bitmap bitmap 			= null ;
            BitmapDrawable drawable = null;

            // 如果暂停的时候,等待
            synchronized (mPauseWorkLock) {
                while (mPauseWork && !isCancelled()) {
                    try {
                        mPauseWorkLock.wait();
                    } catch (InterruptedException e) {}
                }
            }

            // 从磁盘中查找文件是否存在
            if (imageCache != null && !isCancelled() && getAttachedImageView() != null
                    && !mExitTasksEarly) {
                bitmap = imageCache.getBitmapFromDiskCache(dataString);
            }

            // 从网络上下图
            if (bitmap == null && !isCancelled() && getAttachedImageView() != null
                    && !mExitTasksEarly) {
                bitmap = downloadResourecAndDecode(dataString, out.callback,data,imageCache.getDiskCachPath(dataString));
            }
            
            // 添加到缓存中
            if (bitmap != null) {
            	
                drawable = new RecyclingBitmapDrawable(mResources, bitmap);
                
                if (imageCache != null) {
                	imageCache.putBitmapToCache(key, drawable);
                }
            }
            
            out.drawable = drawable ;

            return out ;
        }

        /** 后台线程处理完成后,显示到UI上*/
        @Override
        protected void onPostExecute(ResultDrawable value) {
        	
            // 检测任务是否退出
            if (isCancelled() || mExitTasksEarly) {
                value = null;
            }
            
            final ImageView imageView = getAttachedImageView();
            
            if(value != null && imageView != null && value.drawable != null){
            	// 自定义
            	if(value.callback != null){
            		Bitmap des = value.callback.handleBitmap(value.data, value.drawable.getBitmap());
            		if(des != null){
            			setImageDrawable(value.fadeIn,imageView, new BitmapDrawable(des),value.defBitmap);
            		}
            		
            	} else {
            		setImageDrawable(value.fadeIn,imageView, value.drawable,value.defBitmap);
            	}
            }
        }

        @Override
        protected void onCancelled(ResultDrawable value) {
            super.onCancelled(value);
            
            synchronized (mPauseWorkLock) {
                mPauseWorkLock.notifyAll();
            }
        }

        /** 得到当前ImageView 对应的同步任务 */
        private ImageView getAttachedImageView() {
        	
            final ImageView imageView = imageViewReference.get();
            
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (this == bitmapWorkerTask) {
                return imageView;
            }

            return null;
        }
    }
    
    /**
     * 自定义同步Drawable,代理BitmapDrawable(显示默认图片)
     * 
     * @author zhouwei
     *
     */
    private final static class AsyncDrawable extends BitmapDrawable {
    	
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res,Bitmap bitmap,BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }
    
    /** 下载资源 */
	private final Bitmap downloadResourecAndDecode(String url,IDownloaderCallback callback,ImageItem item,String outFile) {
		
		if(url == null || url.isEmpty()) return null ;
		
		Bitmap bitmap = null ;
		try{
			
			URL u = new URL(url.trim());
			HttpURLConnection connection = (HttpURLConnection)u.openConnection() ;
			connection.setReadTimeout(TIME_OUT);
			connection.setRequestMethod("GET");
			connection.connect() ;
			int size = connection.getContentLength() ;
			InputStream input = connection.getInputStream() ;
			
			Log.d(TAG, String.format("zhouwei: URL=%s;Size=%d", url,size));
			
			// download file and decode bitmap
			if(readInputStream(url,size,input,callback,item,outFile)){
				bitmap = BitmapUtils.createBitmap(outFile, item.getDecodeWidth(), item.getDecodeHeight());
			}
		} catch(Exception e){
			Log.d(TAG, e.toString());
		}
		
		return bitmap ;
	}
	
	public Bitmap downloadResourecAndDecode(String url){
		if(url == null || url.isEmpty()) return null ;
		
		Bitmap bitmap = null ;
		try{
			
			URL u = new URL(url.trim());
			HttpURLConnection connection = (HttpURLConnection)u.openConnection() ;
			connection.setReadTimeout(TIME_OUT);
			connection.setRequestMethod("GET");
			connection.connect() ;
			int size = connection.getContentLength() ;
			InputStream input = connection.getInputStream() ;
			bitmap= BitmapFactory.decodeStream(input);
			String outFile = mImageCache.getDiskCachPath(url);
//			// download file and decode bitmap
			FileOutputStream output 	= new FileOutputStream(outFile);
			 byte[] buffer = new byte[8 * 1024];// 8KB
             // start read
			 int read = -1,down = 0;
             while ((read = input.read(buffer)) != -1) {
             	// download counter
             	down += read ;
             	// save source to local sdcard
             	if(output != null){
             		output.write(buffer, 0, read);
             		output.flush();
             	}
             }
             // add file to disk list
             if(mImageCache != null){
          	   mImageCache.addDiskCacheFile(ImageCache.getUniqueName(url));
             }
             output.close();
             input.close();
			
		} catch(Exception e){
			Log.d(TAG, e.toString());
		}
		
		return bitmap ;
	} 
	

	/** */
    private final boolean readInputStream(String url,int total,InputStream input,String outFile) throws Exception {
       
    	synchronized (mDiskCacheLock) {
    		
    		FileOutputStream output 	= new FileOutputStream(outFile);
            try {
                // read byte[]
                int read = -1;
                int down = 0 ,parent = 0 ,pre_parent = 0;
                byte[] buffer = new byte[8 * 1024];// 8KB
                // start read
                while ((read = input.read(buffer)) != -1) {
                	// download counter
                	down += read ;
                	// download parent
                	if(total > 0) parent 	= (int)(((float) down / total) * 100) ;
                	// save source to local sdcard
                	if(output != null){
                		output.write(buffer, 0, read);
                		output.flush();
                	}
                }
                
               // add file to disk list
               if(mImageCache != null){
            	   mImageCache.addDiskCacheFile(ImageCache.getUniqueName(url));
               }
            } catch (Exception e) {
                throw e;
            } finally {
                // release resource
                if (output != null) {
                	output.close();
                	output = null;
                }
                //release input stream
                if(input != null){
                    input.close();
                    input = null ;
                }
            }
            // return byte[]
            return true;
		}
    }

	
	/** */
    private final boolean readInputStream(String url,int total,InputStream input,IDownloaderCallback callback,ImageItem item,String outFile) throws Exception {
       
    	synchronized (mDiskCacheLock) {
    		
    		FileOutputStream output 	= new FileOutputStream(outFile);
            try {
                // read byte[]
                int read = -1;
                int down = 0 ,parent = 0 ,pre_parent = 0;
                byte[] buffer = new byte[8 * 1024];// 8KB
                // start read
                while ((read = input.read(buffer)) != -1) {
                	// download counter
                	down += read ;
                	// download parent
                	if(total > 0) parent 	= (int)(((float) down / total) * 100) ;
                	// save source to local sdcard
                	if(output != null){
                		output.write(buffer, 0, read);
                		output.flush();
                	}
                	// call back
                	if(parent != pre_parent && callback != null){
                		callback.downloadProgress(item, total, parent, buffer, read) ;
                		pre_parent = parent ;
                	}
                }
                
               // add file to disk list
               if(mImageCache != null){
            	   mImageCache.addDiskCacheFile(ImageCache.getUniqueName(url));
               }
            } catch (Exception e) {
                throw e;
            } finally {
                // release resource
                if (output != null) {
                	output.close();
                	output = null;
                }
                //release input stream
                if(input != null){
                    input.close();
                    input = null ;
                }
            }
            // return byte[]
            return true;
		}
    }
    
}
