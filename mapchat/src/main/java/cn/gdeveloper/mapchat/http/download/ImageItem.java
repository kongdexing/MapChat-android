package cn.gdeveloper.mapchat.http.download;

/**
 */
public class ImageItem implements Cloneable {

	/** 当前正在使用的URL */
	private String validUrl ;
	
	private int decodeWidth ;
	
	private int decodeHeight ;
	
	private Object tag ;

	public ImageItem(){
		setDecodeWidth(0);
		setDecodeHeight(0);
		setTag(null);
	}

	public Object getTag() {
		return tag;
	}

	public String getValidUrl() {
		return validUrl;
	}

	protected void setValidUrl(String validUrl) {
		this.validUrl = validUrl;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public int getDecodeWidth() {
		return decodeWidth;
	}

	public void setDecodeWidth(int decodeWidth) {
		this.decodeWidth = decodeWidth;
	}

	public int getDecodeHeight() {
		return decodeHeight;
	}

	public void setDecodeHeight(int decodeHeight) {
		this.decodeHeight = decodeHeight;
	}

	@Override
	public ImageItem clone(){
		try{
			return (ImageItem)super.clone();
		}catch(CloneNotSupportedException e){
			return new ImageItem() ;
		}
	}
}
