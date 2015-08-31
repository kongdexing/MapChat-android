package cn.gdeveloper.mapchat.http.download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;

/**
 * 
 * @author zhouwei
 *
 */
public final class BitmapUtils {
	
	/** default bitmap width: 4:3*/
	public static final int MAX_WIDTH			= 480 ;
	
	/** default bitmap height*/
	public static final int MAX_HEIGHT			= 360 ;

	public static final Bitmap createBitmap(byte[] data,int target_width,int target_height) {
		try{
			int width				 = target_width <= 0 ? MAX_WIDTH : target_width ;
			int height				 = target_height <= 0 ? MAX_HEIGHT :  target_height;
			int minSideLength 		 = 0 ;
			Options opts  			 = new Options();
			opts.inJustDecodeBounds  = true;
			BitmapFactory.decodeByteArray(data, 0, data.length,opts);
			// set parameter
			minSideLength 	 		 = Math.min(width, height);
			opts.inSampleSize 		 = computeSampleSize(opts, minSideLength, width * height);
			opts.inJustDecodeBounds  = false;
			opts.inInputShareable 	 = true;
			opts.inPurgeable 		 = true;
			opts.inPreferredConfig 	 = Config.RGB_565 ;
			// decode bitmap
			return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		} catch (OutOfMemoryError e) {
			e.printStackTrace() ;
			return null ;
		}
	}
	
	public static final Bitmap createBitmap(String path,int target_width,int target_height) {
		try{
			int width				 = target_width <= 0 ? MAX_WIDTH : target_width ;
			int height				 = target_height <= 0 ? MAX_HEIGHT :  target_height;
			int minSideLength 		 = 0 ;
			Options opts  			 = new Options();
			opts.inJustDecodeBounds  = true;
			BitmapFactory.decodeFile(path,opts);
			// set parameter
			minSideLength 	 		 = Math.min(width, height);
			opts.inSampleSize 		 = computeSampleSize(opts, minSideLength, width * height);
			opts.inJustDecodeBounds  = false;
			opts.inInputShareable 	 = true;
			opts.inPurgeable 		 = true;
			opts.inPreferredConfig 	 = Config.RGB_565 ;
			// decode bitmap
			return BitmapFactory.decodeFile(path,opts);
		} catch (OutOfMemoryError e) {
			e.printStackTrace() ;
			return null ;
		}
	}
	
	private final static int computeSampleSize(Options options,int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
	    int roundedSize;
	    if (initialSize <= 8) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }
	    return roundedSize;
	}
	
	private final static int computeInitialSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;

	    int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
	        return lowerBound;
	    }

	    if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}
}
