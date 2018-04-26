package com.image.slide.charcolee.slidetranslationimage.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

import uk.co.senab.photoview.PhotoView;

/**
 * Created 18/4/25 21:14
 * Author:charcolee
 * Version:V1.0
 * ----------------------------------------------------
 * 文件描述：
 * ----------------------------------------------------
 */

public class TranslationPhotoView extends PhotoView implements Animator.AnimatorListener {

    private float  translationY;
    private float  downY ,lastMoveY;
    private static final int DISTANCE = 400;
    private OnTranslationListener mListener;
    private ObjectAnimator mUpAnimator , mDownAnimator , mResetAnimator;
    private boolean isStartTranslcation = false;

    public TranslationPhotoView(Context context) {
        this(context,null);
    }

    public TranslationPhotoView(Context context, AttributeSet attr) {
        this(context, attr,0);
    }

    public TranslationPhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }


    //getParent().requestDisallowInterceptTouchEvent(true);
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //如果是一只手指点击并且图片缩放比例为1，则可以平移
        Log.d("charco","getScale "+getScale()+" *** count"+event.getPointerCount());
        if (getScale() == 1 && event.getPointerCount() == 1 ){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    Log.d("charco","ACTION_DOWN ");
                    downY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d("charco","ACTION_MOVE ");
                    //过滤不合理的距离，单指滑动后再用第二根手机点击屏幕，会造成照片瞬移
                    if (lastMoveY!=0&&Math.abs(lastMoveY - event.getY())>100){
                        return true;
                    }
                    translationY = event.getY() - downY;
                    lastMoveY = event.getY();
                    //滑动距离大于指定值，通知平移开始
                    if (Math.abs(translationY)>20&&mListener!=null&&!isStartTranslcation){
                        mListener.onTranslationStart();
                        isStartTranslcation = true;
                    //滑动距离小于指定值，通知平移复位
                    }else if (Math.abs(translationY)<=20&&mListener!=null&&isStartTranslcation){
                        mListener.onTranslationReset();
                        isStartTranslcation = false;
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d("charco","ACTION_UP ");
                    if (translationY > DISTANCE){
                        downTranslationAmination();
                    }else if (translationY < -DISTANCE){
                        upTranslationAmination();
                    }else {
                        resetTranslationAmination();
                    }
                    lastMoveY = 0;
                    translationY = 0;
                    isStartTranslcation = false;
                    invalidate();
                    break;
            }
            return true;
        }else {
            //避免平移工程中两个手指进行缩放
            if (translationY!=0){
                return true;
            }else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        return  super.dispatchTouchEvent(event);
    }

    public void setListener(OnTranslationListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public float getTranslationY() {
        return translationY;
    }

    @Override
    public void setTranslationY(float translationY) {
        this.translationY = translationY;
        invalidate();
    }

    //重置动画
    private void resetTranslationAmination(){
        if (mResetAnimator == null){
            mResetAnimator = ObjectAnimator.ofFloat(this, "translationY", translationY, 0)
                    .setDuration(200);
            mResetAnimator.setInterpolator(new LinearInterpolator());
            mResetAnimator.addListener(this);
        }else {//更新最新的translationY值
            mResetAnimator.setFloatValues(translationY, 0);
        }
        mResetAnimator.start();
    }

    //缩手后image自动下滑退出
    private void downTranslationAmination(){
        if (mDownAnimator == null){
            mDownAnimator = ObjectAnimator.ofFloat(this, "translationY", translationY, getHeight())
                    .setDuration(200);
            mDownAnimator.addListener(this);
        }else {
            mDownAnimator.setFloatValues(translationY, 0);
        }
        mDownAnimator.start();
    }

    //缩手后image自动上升退出
    private void upTranslationAmination(){
        if (mUpAnimator == null){
            mUpAnimator = ObjectAnimator.ofFloat(this, "translationY", translationY, -getHeight())
                    .setDuration(200);
            mUpAnimator.addListener(this);
        }else {
            mUpAnimator.setFloatValues(translationY, 0);
        }
        mUpAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //通过平移canvas达到平移图片的效果
        canvas.translate(0,translationY);

        //如果照片平移，则修改背景透明度
        //透明度有效值 0～255 0为全透明 255为不透明
        if (translationY!=0){
            int alpha=255-(int) Math.abs(255 * translationY / DISTANCE);
            if (alpha<0) alpha = 0;
            if (alpha>255)alpha = 255;
            int color = changeAlpha(Color.BLACK, alpha);
            setBackgroundColor(color);
        }else {
            setBackgroundColor(Color.BLACK);
        }
        super.onDraw(canvas);
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {

        if (mListener!=null){//复位动画结束
            if (animator == mResetAnimator){
                mListener.onTranslationReset();
            }else {//上升或下降动画结束
                mListener.onTranslationFinish();
            }
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }


    public interface OnTranslationListener{
        void onTranslationStart();
        void onTranslationReset();
        void onTranslationFinish();
    }

    /**
     * 修改颜色透明度
     * @param color
     * @param alpha
     * @return
     */
    public static int changeAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return Color.argb(alpha, red, green, blue);
    }

}
