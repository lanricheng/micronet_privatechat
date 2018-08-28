package com.vdunpay.vchat.chat;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by HY on 2018/8/16.
 * A ListView which its item can be scroll horizontal
 */
public class ScrollItemListView extends ListView implements IPersonalScrollView{

    private static final String TAG = "ScrollItemListView";
    public static final String DELETE_TAG = "delete";
    public static final String NOR_DELETE_TAG = "nor_delete";
    private int mLastInterceptX = 0;
    private int mLastInterceptY = 0;
    //做移动最大距离
    private int mMaxLeftMargin = 0;
    //左移View
    private View mScrollView = null;
    private View mLastScrollView = null;
    //Down点击时左边View的marginLeft值
    private int mDownLeftMargin = 0;

    private int mHasScrollItemPos = -1;

    private boolean mNeedScrollToNormal = false;

    private boolean mIsScrollingToNormal = false;

    private int mDownPos = -1;

    private boolean mDisallowedIntercept = false;

    public ScrollItemListView(Context context) {
        super(context);
    }

    public ScrollItemListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollItemListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent ev:" + ev.getAction());
        if(mDisallowedIntercept){
            return false;
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int pos = pointToPosition(x, y);
        boolean intercept = false;
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            mLastInterceptX = (int) ev.getX();
            mLastInterceptY = (int) ev.getY();
            getScrollData(pos);
        }
        else if(ev.getAction() == MotionEvent.ACTION_MOVE){
            int deltaX = x - mLastInterceptX;
            int deltaY = y - mLastInterceptY;
            if(Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > 10){ //Math.abs(deltaX) > 10用于防止抖动
                intercept = true;
            }
        }
        else if(ev.getAction() == MotionEvent.ACTION_UP){
            if(null != mLastScrollView) {
                Log.d(TAG, "onInterceptTouchEvent up mLastInterceptX" + mLastInterceptX +
                        " mLastScrollView.getRight()" + mLastScrollView.getRight() + " pos：" + pos + " mHasScrollItemPos：" + mHasScrollItemPos);
            }
            if(mNeedScrollToNormal && !mIsScrollingToNormal && null != mLastScrollView && //证明不是点击在删除按钮上
                    !(mLastInterceptX > mLastScrollView.getRight() && pos == mHasScrollItemPos)){
                new ScrollToEndTask(mLastScrollView, ScrollToEndTask.SCROLL_RIGHT).execute();
                mIsScrollingToNormal = true;
                intercept = true;
            }
            setNeedScrollToNormal(false, -1);
        }
        return intercept || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onTouchEvent ev:" + ev.getAction());
        if(mDisallowedIntercept){
            return false;
        }
        if(mNeedScrollToNormal){
            scrollToNormal(ev);
            return true;
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int pos = pointToPosition(x, y);
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            Log.d(TAG, "mDownPos = " + pos);
            mDownPos = pos;
        }
        else if(ev.getAction() == MotionEvent.ACTION_MOVE) {
            int deltaX = x - mLastInterceptX;
            int deltaY = y - mLastInterceptY;
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                Log.d(TAG, "action move mDownPos = " + pos);
                mDownPos = pos;
                dragItem(x, pos);
                return true;
            }
        }
        else if(ev.getAction() == MotionEvent.ACTION_UP){
            int nowLeftMargin = mDownLeftMargin + x - mLastInterceptX;
            scrollToEnd(nowLeftMargin);
            if(nowLeftMargin < mMaxLeftMargin*0.5){
                Log.d(TAG, "set down pos:" + mDownPos);
                setNeedScrollToNormal(true, mDownPos);
            }
        }
        return super.onTouchEvent(ev);
    }

    private void scrollToEnd(int nowLeftMargin){
        boolean flag = (Math.abs(nowLeftMargin)  < Math.abs(mMaxLeftMargin) * 0.5);
        new ScrollToEndTask(mScrollView,
                flag? ScrollToEndTask.SCROLL_RIGHT: ScrollToEndTask.SCROLL_LEFT).execute();
        mLastScrollView = mScrollView;
    }

    private void setNeedScrollToNormal(boolean flag, int pos){
        mNeedScrollToNormal = flag;
        mHasScrollItemPos = pos;
    }
    public boolean getNeedScrollToNormal(MotionEvent ev){
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int pos = this.pointToPosition(x, y - this.getTop());
        Log.d(TAG, "getNeedScrollToNormal ev:" + ev.getAction() + " pos:" + pos + " mHasScrollItemPos:" + mHasScrollItemPos);
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            mLastInterceptX = x;
            mLastInterceptY = y - this.getTop();
            //点击在删除按钮的部分不需要拦截
//            if(null != mLastScrollView)
//                Log.d(TAG, "getNeedScrollToNormal mLastInterceptX:" + mLastInterceptX + " mLastScrollView.getRight():" + mLastScrollView.getRight());
//            if(mLastScrollView != null && mLastInterceptX > mLastScrollView.getRight() && pos == mHasScrollItemPos){
//                setNeedScrollToNormal(false, -1);
//            }
            return false;
        }
        else if(ev.getAction() == MotionEvent.ACTION_MOVE &&
                mNeedScrollToNormal && !mIsScrollingToNormal
                && pos == mHasScrollItemPos){
            int delaX = x - mLastInterceptX;
            int delaY = y - mLastInterceptY;
//            if(Math.abs(delaY) < Math.abs(delaX)) {
//                setNeedScrollToNormal(false, -1);
//            }
        }
        Log.d(TAG, "getNeedScrollToNormal mNeedScrollToNormal:" + mNeedScrollToNormal);
        return mNeedScrollToNormal;
    }

    public void scrollToNormal(MotionEvent ev){
        if(!mIsScrollingToNormal){
            new ScrollToEndTask(mLastScrollView, ScrollToEndTask.SCROLL_RIGHT).execute();
            mIsScrollingToNormal = true;
        }
        /*if(ev.getAction() == MotionEvent.ACTION_MOVE){
            if(!mIsScrollingToNormal){
                new ScrollToEndTask(mLastScrollView, ScrollToEndTask.SCROLL_RIGHT).execute();
                mIsScrollingToNormal = true;
            }
        }
        else */if(ev.getAction() == MotionEvent.ACTION_UP){
            if(mNeedScrollToNormal) {
                setNeedScrollToNormal(false, -1);
            }
        }
    }

    /**
     * 获取滑动所需的数据
     * @param pos
     */
    private void getScrollData(int pos){
        if(pos > -1){
            View view = getChildAt(pos - getFirstVisiblePosition());
            mScrollView = view.findViewWithTag(NOR_DELETE_TAG);
            View delete = view.findViewWithTag(DELETE_TAG);
            if(null != delete) {
                mMaxLeftMargin = delete.getWidth() * (-1);
            }
            if(null != mScrollView) {
                MarginLayoutParams params = (MarginLayoutParams)mScrollView.getLayoutParams();
                //将宽度从WRAP_CONTENT改为固定值
                params.width = mScrollView.getWidth();
                mDownLeftMargin = params.leftMargin;
            }
        }
    }

    private void dragItem(int nowX, int pos) {
        if (pos > -1 && null != mScrollView) {
            MarginLayoutParams params = (MarginLayoutParams)mScrollView.getLayoutParams();
            mScrollView.setLayoutParams(params);
            int leftMargin = mDownLeftMargin + nowX - mLastInterceptX;
            leftMargin = Math.min(0, leftMargin);
            leftMargin = Math.max(leftMargin, mMaxLeftMargin);//leftMargin 介于0和mMaxLeftMargin之间
            params.leftMargin = leftMargin;
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public void disallowedIntercept(boolean disallowed) {
        mDisallowedIntercept = disallowed;
    }

    private class ScrollToEndTask extends AsyncTask<Void, Integer, Void>{
        public static final int SCROLL_LEFT = 1;
        public static final int SCROLL_RIGHT = 2;
        View view;
        MarginLayoutParams marginLayoutParams = null;
        int endMargin = 0;
        int direction = 0;
        public ScrollToEndTask(View view, int direction) {
            super();
            this.view = view;
            this.marginLayoutParams = (MarginLayoutParams)view.getLayoutParams();
            this.direction = direction;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int margin = marginLayoutParams.leftMargin;
            try {
                while (margin < 0 && margin >= mMaxLeftMargin){
                    if(direction == SCROLL_LEFT){
                        margin-=10;
                    }
                    else if(direction == SCROLL_RIGHT){
                        margin+=10;
                    }

                        Thread.sleep(10);
                    publishProgress(margin);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int margin = (int)(values[0]);
            if(margin > 0){
                margin = 0;
            }
            else if(margin < mMaxLeftMargin){
                margin = mMaxLeftMargin;
            }
            marginLayoutParams.leftMargin = margin;
            view.setLayoutParams(marginLayoutParams);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mIsScrollingToNormal){
                mIsScrollingToNormal = false;
            }
        }
    }
}
