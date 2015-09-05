package cn.gdeveloper.mapchat.http.download;

import android.graphics.Bitmap;
public interface IDownloaderCallback {

    public void downloadProgress(ImageItem item, int total, int parent, byte[] data, int length);

    public Bitmap handleBitmap(ImageItem item, Bitmap src);
}
