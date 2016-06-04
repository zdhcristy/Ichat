package com.example.ichat;

import com.example.ichat.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ChangeColorOfBottomView extends View {

	private Bitmap mIconBitmap;
	private String mText = "发现";
	private float mAlpha;
	private int mColor = 0xFF45C01A;
	private int mTextSize = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
	
	private Canvas mCanvas;
	private Bitmap mBitmap;
	private Paint mPaint;
	
	private Rect mIconRect;
	private Rect mTextBound;
	private Paint mTextPaint;
	
	private static final String INSTANCE_STATUS = "instance_status";
	private static final String STATUS_ALPHA = "status_alpha";
	
	public ChangeColorOfBottomView(Context context) {
		this(context,null);
	}
	public ChangeColorOfBottomView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	/**
	 * 遍历获取自定义属性的值，然后赋给成员变量;初始化成员变量
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public ChangeColorOfBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//遍历属性，拿到自定义属性的值
		TypedArray ta = context.obtainStyledAttributes(attrs, 
				R.styleable.ChangeColorOfBottomView);
		//将获取的值赋给成员变量
		for (int i = 0; i < ta.getIndexCount(); i++) {
			int attr = ta.getIndex(i);
			switch (attr) {
			case R.styleable.ChangeColorOfBottomView_icon:
				BitmapDrawable drawable = (BitmapDrawable) ta.getDrawable(attr);
				mIconBitmap = drawable.getBitmap();
				break;
				
			case R.styleable.ChangeColorOfBottomView_color:
				mColor = ta.getColor(attr, 0xFF45C01A);
				break;
				
			case R.styleable.ChangeColorOfBottomView_text:
				mText = ta.getString(attr);
				break;
				
			case R.styleable.ChangeColorOfBottomView_text_size:
				//转换为12sp
				mTextSize = (int) ta.getDimension(attr, TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
				break;
			}
		}
		//初始化成员变量之前要回收TypedArray
		ta.recycle();
		mTextBound = new Rect();
		mTextPaint = new Paint();
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(0xFF555555);
		//测量icon下文字的范围
		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int iconWidth = Math.min(getMeasuredWidth()-getPaddingLeft()-getPaddingRight(), 
				getMeasuredHeight()-getPaddingBottom()-getPaddingTop()-mTextBound.height());
		int left = (getMeasuredWidth()-iconWidth)/2;
		int top = (getMeasuredHeight()-mTextBound.height()-iconWidth)/2;
		
		mIconRect = new Rect(left, top, left+iconWidth, top+iconWidth);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
		int alpha = (int) Math.ceil(255*mAlpha);
		//在内存去准备mBitmap、Alpha、纯色、xfermode、图标
		setupTargetBitmap(alpha);
		//绘制原文本
		drawSourceText(canvas, alpha);
		//绘制变色的文本
		drawTargetText(canvas, alpha);
		canvas.drawBitmap(mBitmap, 0, 0,null);
	}
	//在内存中绘制可以变色的icon
	private void setupTargetBitmap(int alpha) {
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), 
				getHeight(), Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mPaint = new Paint();
		mPaint.setColor(mColor);    //设置纯色
		mPaint.setAntiAlias(true);  //设置抗锯齿
		mPaint.setDither(true);     //设置抖动
		mPaint.setAlpha(alpha);     //设置透明度
		mCanvas.drawRect(mIconRect, mPaint);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mPaint.setAlpha(255);
		mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
	}
	/**
	 * 绘制原文本
	 * @param canvas
	 * @param alpha
	 */
	private void drawSourceText(Canvas canvas, int alpha) {
		mTextPaint.setColor(0xFF333333);
		mTextPaint.setAlpha(255-alpha);
		int x=(getMeasuredWidth()-mTextBound.width())/2;
		int y = mIconRect.bottom + mTextBound.height();
		canvas.drawText(mText, x, y, mTextPaint);
	}
	/**
	 * 绘制变色文本
	 * @param canvas
	 * @param alpha
	 */
	private void drawTargetText(Canvas canvas, int alpha) {
		mTextPaint.setColor(mColor);
		mTextPaint.setAlpha(alpha);
		int x=(getMeasuredWidth()-mTextBound.width())/2;
		int y = mIconRect.bottom + mTextBound.height();
		canvas.drawText(mText, x, y, mTextPaint);
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
		bundle.putFloat(STATUS_ALPHA, mAlpha);
		return bundle;
	}
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if(state instanceof Bundle){
			Bundle bundle = (Bundle) state;
			mAlpha = bundle.getFloat(STATUS_ALPHA);
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
			return;
		}
		super.onRestoreInstanceState(state);
	}
	//外界可设置alpha
	public void setIconAlpha(float alpha){
		this.mAlpha = alpha;
		invalidateView();
	}
	//View重绘
	private void invalidateView() {
		if(Looper.getMainLooper() == Looper.myLooper()){
			//主线程重绘调用此方法
			invalidate();
		}else{
			//子线程重绘调用此方法
			postInvalidate();
		}
	}
}
