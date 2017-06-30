package com.dyf.dyfkotlindemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dyf.dyfkotlindemo.view.image.ImageCycleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by dyf on 2017/6/30.
 */

public class ImageCycleActivity extends Activity {

    private ImageCycleView mAdView;

    private ArrayList<String> mImageUrl = null;

    private String[] imageUrls = {"http://img.juemei.com/album/2016-08-23/57bbb6a2d1ce3.jpg",
            "http://img.juemei.com/album/2016-08-23/57bbb6ada5e8c.jpg",
            "http://img.juemei.com/album/2016-08-25/57be699856733.jpg",
            "http://img.juemei.com/album/2016-08-25/57be699233440.jpg"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_cycle);
        mImageUrl = new ArrayList<String>();
        for(String str: imageUrls){
            mImageUrl.add(str);
        }
        mAdView = (ImageCycleView) findViewById(R.id.ad_view);
        mAdView.setImageResources(mImageUrl, mAdCycleViewListener);
    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

        @Override
        public void onImageClick(int position, View imageView) {
            Toast.makeText(ImageCycleActivity.this, "position->"+position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// 此处本人使用了ImageLoader对图片进行加装！
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.startImageCycle();
    };

    @Override
    protected void onPause() {
        super.onPause();
        mAdView.pushImageCycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.pushImageCycle();
    }
}
