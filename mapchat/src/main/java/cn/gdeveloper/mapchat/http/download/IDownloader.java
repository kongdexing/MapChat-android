package cn.gdeveloper.mapchat.http.download;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 资源下载
 * 每一接口提供了2组资源地址: 普通(高质量),低质量
 *
 */
public interface IDownloader {
	
	public void downloadBitmap(String url, ImageView imageView);
	
	public void downloadBitmap(String hdUrl, String lowUrl, ImageView imageView) ;
	
	public void downloadBitmap(String hdUrl, String lowUrl, ImageView imageView, int rid) ;
	
	public void downloadBitmap(String hdUrl, String lowUrl, ImageView
			imageView,
							   int rid,
							   boolean fadeIn) ;
	
	public void downloadBitmap(String hdUrl, String lowUrl,
							   ImageView imageView,
							   Bitmap defaultBitmap) ;
	
	public void downloadBitmap(String hdUrl, String lowUrl,
							   ImageView imageView,
							   Bitmap defaultBitmap,
							   IDownloaderCallback callback) ;
	
	public void downloadBitmap(String hdUrl, String lowUrl, Object tag,
							   ImageView imageView,
							   Bitmap defaultBitmap,
							   IDownloaderCallback callback) ;
	
	public void downloadBitmap(String hdUrl, String lowUrl,
							   ImageView imageView,
							   Bitmap defaultBitmap,
							   IDownloaderCallback callback,
							   boolean fadeInBitmap) ;
	
	public void downloadBitmap(String hdUrl, String lowUrl, Object tag,
							   ImageView imageView,
							   int rid,
							   IDownloaderCallback callback,
							   boolean fadeInBitmap) ;
	
	public Bitmap getBitmap(String url) ;
	
	public Bitmap getMyBitmap(String url);
	
	public String getDiskPath(String url);
	
	/** 恢复下载 */
	public void onStart() ;
	
	public void onPause() ;
	
	public void onResume() ;
	
	public void onDestory() ;
	
	/** wifi 情况下下载高清图片 */
	public void setDownloadHDImage(boolean downHD);
	
	/** 下载状态: 开|关 */
	public void setState(ImageDownloader.State newState);
	
	// 当前现在状态
	public ImageDownloader.State getState();
	
	// 清除缓存
	public void clearCache() ;
	
	/** 重置缓存 */
	public void onReset() ;
	
}
