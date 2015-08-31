package cn.gdeveloper.mapchat.view.listview;

import java.util.Date;
import java.util.logging.Handler;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.common.MapChatHandler;
import cn.gdeveloper.mapchat.common.MapChatHttpService;

/**
 * 下拉刷新
 */
public abstract class PullToRefreshListView extends ListView implements OnScrollListener,IListViewListener {

	static final String TAG = "PullToRefreshListView";
	
	private final static int RELEASE_To_REFRESH 	= 0;
	private final static int PULL_To_REFRESH 		= 1;
	private final static int REFRESHING 			= 2;
	private final static int DONE 					= 3;
	private final static int LOADING 				= 4;
	static final int STATE_IDLE						= 1 << 1 ;
	static final int STATE_LOAD						= 1 << 2 ;

	private final static int RATIO 					= 3;
	private final static String TEXT_PULL			= "下来刷新" ;
	private final static String TEXT_RELEASE		= "松开刷新" ;
	private final static String TEXT_DATE_HEADER	= "最近更新:" ;
	private final static String TEXT_LOADING		= "刷新中..." ;

	private LayoutInflater inflater;

	public LinearLayout headView;

	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	private boolean isRecored;

	private int headContentWidth;
	private int headContentHeight;

	private int startY;
	private int firstItemIndex;

	private int state;

	private boolean isBack;

	private OnRefreshListener refreshListener;

	private boolean isRefreshable;
	
	public MapChatHandler mHandler ;
	protected Context mContext ; //必须是activity的context,不能是APP context
	private int loadState ;

	public PullToRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext	= context ;
		mHandler	= getMessageHandler() ;

		inflater = LayoutInflater.from(context);

		headView = (LinearLayout) inflater.inflate(R.layout.pull_to_refresh_header, null);

		arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		
		progressBar 	= (ProgressBar) headView.findViewById(R.id.head_progressBar);
		tipsTextview 	= (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight 	= headView.getMeasuredHeight();
		headContentWidth 	= headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		addHeaderView(headView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
		setIdleState() ;
	}

	public MapChatHandler getMessageHandler() {
		return new MapChatHandler(mContext);
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,int arg3) {
		firstItemIndex = firstVisiableItem;
	}
	
	protected final void setIdleState(){
		loadState = STATE_IDLE ;
	}
	
	protected final void uniteListView(ListView list) {
		list.setDividerHeight(9);
		list.setCacheColorHint(Color.TRANSPARENT);
		list.setDivider(mContext.getResources().getDrawable(R.mipmap.ic_line));
	}
	
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
		if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
			MapChatHttpService.getInstance().getDownloader().onPause();
		} else {
			MapChatHttpService.getInstance().getDownloader().onResume();
		}
		
		if (scrollState == SCROLL_STATE_IDLE 
				&& view.getLastVisiblePosition() == view.getCount() - 1){
			// 加载更多
			if(loadState == STATE_IDLE){
				loadState = STATE_LOAD ;
				onLoadNextPage();
			}
        }  
	}

	public boolean onTouchEvent(MotionEvent event) {

		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;
			case MotionEvent.ACTION_UP:

				if (state != REFRESHING && state != LOADING) {
					
					if (state == DONE) {
						
					}
					
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
					}
					
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}

				if (state != REFRESHING && isRecored && state != LOADING) {

					if (state == RELEASE_To_REFRESH) {

						setSelection(0);

						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
						else {
							
						}
					}
					if (state == PULL_To_REFRESH) {

						setSelection(0);

						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						}
						
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}

					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);
					}

					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);
					}
				}

				break;
			}
		}

		return super.onTouchEvent(event);
	}

	private final void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText(TEXT_RELEASE);
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextview.setText(TEXT_PULL);
			} else {
				tipsTextview.setText(TEXT_PULL);
			}
			
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);
			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText(TEXT_LOADING);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.mipmap.ic_pulltorefresh_arrow);
			tipsTextview.setText(TEXT_PULL);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		}
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public final void onRefreshComplete() {
		
		state 	= DONE;
		
		lastUpdatedTextView.setText(TEXT_DATE_HEADER + new Date().toLocaleString());
		
		changeHeaderViewByState();
	}

	private final void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	private final void measureView(View child) {
		
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
		}
		
		child.measure(childWidthSpec, childHeightSpec);
	}
	
	public void setAdapter(BaseAdapter adapter) {
		
		lastUpdatedTextView.setText(TEXT_DATE_HEADER + new Date().toLocaleString());
		
		super.setAdapter(adapter);
	}
	
	protected abstract void onLoadNextPage() ;

	public static interface OnRefreshListener {
		public void onRefresh();
	}
}
