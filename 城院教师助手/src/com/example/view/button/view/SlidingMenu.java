package com.example.view.button.view;

import com.cxyliuyu.cyjszs.R;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView{

	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	private int mScreenWidth;
	private int mMenuRightPadding=50;
	private boolean once=false;
	private int mMenuWidth;
	private boolean isopen;
	//ImageButton bb;///////
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO 自动生成的构造函数存根
		
	/*WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
	DisplayMetrics outMetrics= new DisplayMetrics();
	wm.getDefaultDisplay().getMetrics(outMetrics);	
	mScreenWidth=outMetrics.widthPixels;
	
	mMenuRightPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());*/
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO 自动生成的方法存根
		
		if(!once){
			mWapper =(LinearLayout)getChildAt(0);
			mMenu= (ViewGroup) mWapper.getChildAt(0);
			mContent = (ViewGroup) mWapper.getChildAt(1);
			
			mMenuWidth=mMenu.getLayoutParams().width=mScreenWidth-mMenuRightPadding;
			mContent.getLayoutParams().width=mScreenWidth;
			once=true;
			
		}
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO 自动生成的方法存根
		super.onLayout(changed, l, t, r, b);
		if(changed){
			this.scrollTo(mMenuWidth,0);
		}
	}
	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		// TODO 自动生成的方法存根
		
		int action = event.getAction();
		switch(action){
		case MotionEvent.ACTION_UP:
			int scrollX =getScrollX();
			
			if(scrollX >= mMenuWidth/2 ){
				this.smoothScrollTo(mMenuWidth, 0);
				isopen=false;
				
			}else{
				this.smoothScrollTo(0, 0);
				isopen=true;
				
			}
			
			return true;
		}
		return super.onTrackballEvent(event);
		
		
	}
	
	public void openView(){
		if(isopen)return;
		this.smoothScrollTo(0, 0);
		isopen=true;
		
	}
	
	public void closeView(){
		if(!isopen)return;
		this.smoothScrollTo(mMenuWidth, 0);
		isopen=false;
		
	}
	
	public void toggle(){
		
		
		if(isopen){
			
			closeView();
		}else{
			openView();
		}
		
		
		
	}


	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自动生成的构造函数存根
		 //bb = (ImageButton) findViewById(R.id.person);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.SlidingMenu,defStyle,0);
		int n =a.getIndexCount();
		
		for(int i=0; i<n; i++){
			int attr=a.getIndex(i);
			
			switch(attr){
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding=a.getDimensionPixelSize(attr, 
						(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, 
								context.getResources().getDisplayMetrics()));
				break;
				
				default:
					break;
					
			}
			
		}
		
		WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics= new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);	
		mScreenWidth=outMetrics.widthPixels;
		
		mMenuRightPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, context.getResources().getDisplayMetrics());
		
		
		
	}
	
	protected void onScrollChanged(int l, int t, int oldl, int oldt){
		super.onScrollChanged(l, t, oldl, oldt);
		float scale1 =l*1.0f/mMenuWidth;
		 float rightscale =0.7f+0.3f*scale1;
	    float leftscale =1.0f-scale1*0.3f;
	    float leftAlpha =0.6f+0.4f*(1-scale1);
	    
	    ViewHelper.setTranslationX(mMenu,mMenuWidth*scale1*0.7f);
	    ViewHelper.setScaleX(mMenu, leftscale);
	    ViewHelper.setScaleY(mMenu, leftscale);
	    ViewHelper.setAlpha(mMenu, leftAlpha);
	
	    
	    ViewHelper.setScaleX(mContent, 0);
	    ViewHelper.setScaleY(mContent, mContent.getHeight());
	    ViewHelper.setScaleX(mContent, rightscale);
	    ViewHelper.setScaleY(mContent, rightscale);
	    
	}

	public SlidingMenu(Context context) {
		this(context,null);
		// TODO 自动生成的构造函数存根
	}
}
