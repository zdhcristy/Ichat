package com.example.ichat;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.ichat.adapter.FindAdapter;

import android.content.Context;
import android.preference.PreferenceActivity.Header;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ChatListView extends ListView implements OnScrollListener{

	private View mHeader,mFooter;
	private TextView mHeaderTip;
	private ImageView mHeaderArrow;
	private ProgressBar mHeaderProgressBar;
	private int mStart;
	//顶部布局文件高度
	private int mHeaderHeight;
	//标记当前是ListView最顶端的时候下拉
	private boolean isRemark;
	//标记当前是ListView最底端的时候
	private boolean isLoading;
	//在ListView最顶端的时候下拉的Y值
	private int startY;
	//正常状态
	private static final int NONE = 0;
	//提示下拉状态
	private static final int PULL = 1;
	//提示松开状态
	private static final int RELEASE = 2;
	//刷新状态
	private static final int REFRESHING = 3;
	
	private int state;
	private int mScrollState;
	//当前页总item数量
	private int mTotalItemCount;
	//当前页最后一个item
	private int mLastVisibleItem;
	
	private IRefreshListener mIRefreshListener;
	
	public ChatListView(Context context) {
		this(context,null);
	}
	
	public ChatListView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	
	public ChatListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	/**
	 * 添加顶部下拉刷新的布局文件到ChatFragment的LisView里
	 * @param context
	 */
	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		mHeader = inflater.inflate(R.layout.header_layout, null);
		mFooter = inflater.inflate(R.layout.footer_layout, null);
		mHeaderTip = (TextView) mHeader.findViewById(R.id.header_tip);
		mHeaderArrow = (ImageView) mHeader.findViewById(R.id.header_arrow);
		mHeaderProgressBar = (ProgressBar) mHeader.findViewById(R.id.header_progress);
		//先隐藏底部布局
		mFooter.findViewById(R.id.footer_layout).setVisibility(View.GONE);
		measureView(mHeader);
		mHeaderHeight = mHeader.getMeasuredHeight();
		setTopPadding(-mHeaderHeight);
		this.addHeaderView(mHeader);
		this.addFooterView(mFooter);
		this.setOnScrollListener(this);
	}
	/**
	 * 设置header自身大小，通知父布局
	 * @param v
	 */
	private void measureView(View v){
		ViewGroup.LayoutParams vgl = v.getLayoutParams();
		if(vgl == null){
			vgl = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 
				0, vgl.width);
		int height;
		int tempHeight = vgl.height;
		if(tempHeight>0){
			height = MeasureSpec.makeMeasureSpec(tempHeight, 
					MeasureSpec.EXACTLY);
		}else{
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		v.measure(width, height);
	}
	
	/**
	 * 设置header布局的上边距
	 * @param topPadding
	 */
	private void setTopPadding(int topPadding){
		mHeader.setPadding(mHeader.getPaddingLeft(), topPadding, 
				mHeader.getPaddingRight(), mHeader.getPaddingBottom());
		mHeader.invalidate();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
		mScrollState = scrollState;
		if(mTotalItemCount == mLastVisibleItem 
				&& mScrollState == SCROLL_STATE_IDLE){
			if(!isLoading){
				isLoading = true;
				mFooter.findViewById(R.id.footer_layout).setVisibility(View.VISIBLE);
				mIRefreshListener.onLoadMore();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		mStart = firstVisibleItem;
		mTotalItemCount = totalItemCount;
		mLastVisibleItem = mStart + visibleItemCount;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(mStart == 0){
				isRemark = true;
				startY = (int) ev.getY();
			}
			break;
			
		case MotionEvent.ACTION_MOVE:
			onMove(ev);
			break;
			
		case MotionEvent.ACTION_UP:
			if(state == RELEASE){
				state = REFRESHING;
				//加载最新数据
				refreshViewByState();
				mIRefreshListener.onRefresh();
			}else if(state == PULL){
				state = NONE;
				isRemark = false;
				refreshViewByState();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}
	/**
	 * 判断滑动过程的状态进行相应操作
	 * @param ev
	 */
	private void onMove(MotionEvent ev) {
		if(!isRemark){
			return;
		}
		int tempY = (int) ev.getY();
		int moveLength = tempY - startY;
		int topPadding = moveLength - mHeaderHeight;
		switch (state) {
		case NONE:
			if(moveLength > 0){
				state = PULL;
				refreshViewByState();
			}
			break;
			
		case PULL:
			setTopPadding(topPadding);
			if(moveLength > mHeaderHeight+32 &&
					mScrollState == SCROLL_STATE_TOUCH_SCROLL){
				state = RELEASE;
				refreshViewByState();
			}
			break;
			
		case RELEASE:
			setTopPadding(topPadding);
			if(moveLength < mHeaderHeight+32){
				state = PULL;
				refreshViewByState();
			}else if(moveLength <= 0){
				state = NONE;
				isRemark = false;
				refreshViewByState();
			}
			break;
		}
	}
	
	/**
	 * 根据当前的状态改变header的界面显示
	 */
	private void refreshViewByState(){
		
		RotateAnimation downAnim = new RotateAnimation(0, 180, 
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		downAnim.setDuration(500);
		downAnim.setFillAfter(true);
		RotateAnimation upAnim = new RotateAnimation(180, 0, 
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		upAnim.setDuration(500);
		upAnim.setFillAfter(true);
		
		switch (state) {
		case NONE:
			mHeaderArrow.clearAnimation();
			setTopPadding(-mHeaderHeight);
			break;
			
		case PULL:
			mHeaderArrow.setVisibility(View.VISIBLE);
			mHeaderProgressBar.setVisibility(View.GONE);
			mHeaderTip.setText("下拉可以刷新！");
			mHeaderArrow.clearAnimation();
			mHeaderArrow.setAnimation(upAnim);
			break;
			
		case RELEASE:
			mHeaderArrow.setVisibility(View.VISIBLE);
			mHeaderProgressBar.setVisibility(View.GONE);
			mHeaderTip.setText("松开可以刷新！");
			mHeaderArrow.clearAnimation();
			mHeaderArrow.setAnimation(downAnim);
			break;
		
		case REFRESHING:
			setTopPadding(50);
			mHeaderArrow.setVisibility(View.GONE);
			mHeaderProgressBar.setVisibility(View.VISIBLE);
			mHeaderTip.setText("正在刷新...");
			mHeaderArrow.clearAnimation();
			break;
		}
	}
	/**
	 * 下拉获取完数据改变header的状态
	 */
	public void reflashComplete(){
		state = NONE;
		isRemark =false;
		refreshViewByState();
		TextView mLastUpdateTime = (TextView) mHeader.findViewById(R.id.header_last_update_time);
		SimpleDateFormat df = new SimpleDateFormat("MM月dd日hh时mm分");
		Date date = new Date(System.currentTimeMillis());
		String lastUpdateTime = df.format(date);
		mLastUpdateTime.setText("更新于:"+lastUpdateTime);
	}
	/**
	 * 加载完数据改变footer的状态
	 */
	public void loadComplete(){
		isLoading =false;
		mFooter.findViewById(R.id.footer_layout).setVisibility(View.GONE);
	}
	
	public void setInterface(IRefreshListener iReflashListener){
		mIRefreshListener = iReflashListener;
	}
	
	/**
	 * 下拉刷新与底部加载数据接口
	 */
	public interface IRefreshListener{
		
		public void onRefresh();
		
		public void onLoadMore();
		
	}
}
