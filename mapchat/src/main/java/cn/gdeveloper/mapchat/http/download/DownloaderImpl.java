package cn.gdeveloper.mapchat.http.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 图片下载管理
 */
public final class DownloaderImpl implements IDownloader {
	
	static final String TAG 	= "DownResourceImpl"  ;
	
	/** 缓存 */
	private  ImageDownloader mCacheManager ;
	private final ImageItem mImageItem = new ImageItem() ;
	
	public DownloaderImpl(Context context){
		checkCacheManager(context);
	}

	@Override
	public void downloadBitmap(String url, ImageView imageView) {
		downloadBitmap(url,imageView, -1, false);
	}

	@Override
	public void downloadBitmap(String url, ImageView imageView, int rid) {
		downloadBitmap(url, imageView, rid, true);
	}

	@Override
	public void downloadBitmap(String url, ImageView imageView, int rid,boolean fadeIn) {
		
		final ImageItem item = mImageItem.clone() ;
		item.setValidUrl(url);

		mCacheManager.downloadBitmap(item, imageView, rid, null, false);
	}

	@Override
	public Bitmap getBitmap(String url) {
		if(mCacheManager.getBitmapFromCache(url)==null){
			//缓存无数据了，重新下载吧
			downloadBitmap(url,new ImageView(null));
		}
		return mCacheManager != null ? mCacheManager.getBitmapFromCache(url) : null;
	}

	@Override
	public Bitmap getMyBitmap(String url) {
		if(mCacheManager.getBitmapFromCache(url)==null){
			//缓存无数据了，重新下载吧
			return mCacheManager.downloadResourecAndDecode(url);
		}
		return mCacheManager != null ? mCacheManager.getBitmapFromCache(url) : null;
	}

	@Override
	public void downloadBitmap(String url,
			ImageView imageView, Bitmap defaultBitmap) {
		downloadBitmap(url,imageView,defaultBitmap,null,defaultBitmap != null);
	}

	@Override
	public void downloadBitmap(String url,
			ImageView imageView, Bitmap defaultBitmap,
			IDownloaderCallback callback) {
		downloadBitmap(url,imageView,defaultBitmap,callback,defaultBitmap != null);
	}

	@Override
	public void downloadBitmap(String url, Object tag, ImageView imageView,
			Bitmap defaultBitmap, IDownloaderCallback callback) {
		downloadBitmap(url, tag,imageView, defaultBitmap, callback, defaultBitmap != null);
	}

	@Override
	public void downloadBitmap(String url,
			ImageView imageView, Bitmap defaultBitmap,
			IDownloaderCallback callback, boolean fadeInBitmap) {
		
		downloadBitmap(url, null,imageView, defaultBitmap, callback, fadeInBitmap);
	}

	@Override
	public void downloadBitmap(String url, Object tag,ImageView imageView, int rid,
			IDownloaderCallback callback, boolean fadeInBitmap) {
		
		final ImageItem item = mImageItem.clone() ;
		item.setValidUrl(url);
		item.setTag(tag);
		
		mCacheManager.downloadBitmap(item, imageView, rid, callback, false);
	}

	private final void downloadBitmap(String url,Object tag,
			ImageView imageView, Bitmap defaultBitmap,
			IDownloaderCallback callback, boolean fadeInBitmap) {
		
		final ImageItem item = mImageItem.clone() ;
		item.setValidUrl(url);
		item.setTag(tag);
		
		mCacheManager.downloadBitmap(item, imageView, defaultBitmap, callback, false);
	}

	@Override
	public void setState(ImageDownloader.State newState) {
		if(mCacheManager != null){
			mCacheManager.setState(newState);
		}
	}

	@Override
	public void onStart() {
		if(mCacheManager != null){
			mCacheManager.onStart() ;
		}
	}

	@Override
	public void onReset() {
		
		if(mCacheManager != null){
			mCacheManager.onReset() ;
		}
	}

	@Override
	public void onDestory() {
		
		if(mCacheManager != null){
			mCacheManager.onDestory() ;
		}
	}

	@Override
	public void onPause() {
		
		if(mCacheManager != null){
			mCacheManager.onPause() ;
		}
	}

	@Override
	public void onResume() {
		
		if(mCacheManager != null){
			mCacheManager.onResume() ;
		}
	}

	@Override
	public void clearCache() {
		
		/*if(mCacheManager != null){
			mCacheManager.onClear() ;
		}*/
	}

	@Override
	public ImageDownloader.State getState() {
		return mCacheManager != null ? mCacheManager.getState() : ImageDownloader.State.STATE_OFF ;
	}

	private void checkCacheManager(Context context){
		if(mCacheManager == null)
			mCacheManager = ImageDownloader.create(context, "MapChat", 0.20f);
	}

	@Override
	public String getDiskPath(String url) {
		return mCacheManager.getCacheLocalPath(url);
	}

}
