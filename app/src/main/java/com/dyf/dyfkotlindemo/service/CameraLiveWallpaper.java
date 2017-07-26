package com.dyf.dyfkotlindemo.service;

import android.hardware.Camera;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by dyf on 2017/7/26.
 */

public class CameraLiveWallpaper extends WallpaperService {

    //实现wallpaperService 必须实现的抽象方法
    @Override
    public Engine onCreateEngine() {
        //返回自定义的CameraEngine
        return new CameraEngine();
    }

    class CameraEngine extends Engine implements Camera.PreviewCallback {
        private Camera camera;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            startPreview();
            //设置处理触摸事件
            setTouchEventsEnabled(true);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            //事件处理:点击拍照 ,长按拍照
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            stopPreview();
        }


        //这个一定要加, 不然不行 黑屏 小米4c 亲测有效
        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                startPreview();
            } else {
                stopPreview();
            }
        }


        public void startPreview(){
            camera = Camera.open();
            camera.setDisplayOrientation(90);
            try {
                camera.setPreviewDisplay(getSurfaceHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
        }

        public void stopPreview(){
            if (camera != null){
                camera.stopPreview();
                camera.setPreviewCallback(null);
//                camera.lock();
                camera.release();
                //
                camera = null;
            }
        }

        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            camera.addCallbackBuffer(bytes);
        }
    }


}
