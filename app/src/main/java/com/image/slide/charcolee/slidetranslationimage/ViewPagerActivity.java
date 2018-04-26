package com.image.slide.charcolee.slidetranslationimage;

import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.image.slide.charcolee.slidetranslationimage.view.TranslationPhotoView;

public class ViewPagerActivity extends AppCompatActivity implements TranslationPhotoView.OnTranslationListener {

    private ViewPager mViewPager;
    private TranslationPhotoView[] mPhotoViews;
    private int[] ids = new int[]{R.drawable.bg1,R.drawable.bg2,R.drawable.bg3};
    private String[] urls = new String[]{
            "http://img.52fuqing.com/upload/news/2015-1-30/2015130202347795e386h.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2069716012,863663220&fm=214&gp=0.jpg",
            "http://www.zjstv.com/liv_loadfile/news/zjnews/fold133/1468303207_59855200.jpg"
    };
    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        initView();
    }

    private void initView() {
        textview = findViewById(R.id.textview);
        mViewPager = findViewById(R.id.viewpager);

        mPhotoViews = new TranslationPhotoView[urls.length];
        for (int i = 0; i < mPhotoViews.length; i++) {
            mPhotoViews[i] = (TranslationPhotoView) View.inflate(this, R.layout.item_viewpager, null);
            mPhotoViews[i].setImageResource(ids[i]);
            mPhotoViews[i].setListener(this);
//            Glide.with(this).load(urls[i]).into(mPhotoViews[i]);//配合glide使用
        }

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mPhotoViews.length;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mPhotoViews[position]);
                return mPhotoViews[position];
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mPhotoViews[position]);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {

                return view == object;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                textview.setText((position+1)+"/"+ids.length);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void onTranslationStart() {
        Log.d("charco","onTranslationStart") ;
        textview.setVisibility(View.GONE);
    }

    @Override
    public void onTranslationReset() {
        Log.d("charco","onTranslationReset") ;
        textview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTranslationFinish() {
        finish();
    }
}
