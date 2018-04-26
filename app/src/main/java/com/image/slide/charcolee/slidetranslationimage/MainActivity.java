package com.image.slide.charcolee.slidetranslationimage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void viewpager(View view) {
        startActivity(new Intent(this,ViewPagerActivity.class));
    }

    public void single(View view) {
        startActivity(new Intent(this,SingleActivity.class));
    }
}
