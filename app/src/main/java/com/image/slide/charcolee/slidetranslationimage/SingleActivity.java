package com.image.slide.charcolee.slidetranslationimage;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.image.slide.charcolee.slidetranslationimage.view.TranslationPhotoView;

public class SingleActivity extends AppCompatActivity implements TranslationPhotoView.OnTranslationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        initView();
    }

    private void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        TranslationPhotoView translationPhotoView = findViewById(R.id.photoview);
        translationPhotoView.setListener(this);
    }

    @Override
    public void onTranslationStart() {

    }

    @Override
    public void onTranslationReset() {

    }

    @Override
    public void onTranslationFinish() {
        finish();
    }
}
