package cn.gdeveloper.mapchat.view.listview;

/**
 * 列表监听器
 */
public interface IListViewListener {

	/** 清除列表中所有元素 */
	public void onClear() ;
	
	/** 加载列表 */
	public void onReload();
}
