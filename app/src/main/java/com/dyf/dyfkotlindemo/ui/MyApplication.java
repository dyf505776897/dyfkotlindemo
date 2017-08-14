package com.dyf.dyfkotlindemo.ui;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.avos.avoscloud.AVOSCloud;
import com.dyf.dyfkotlindemo.R;
import com.dyf.dyfkotlindemo.weex.ImageAdapter;
import com.dyf.dyfkotlindemo.weex.extend.compontent.RichText;
import com.dyf.dyfkotlindemo.weex.extend.module.PhoneInfoModule;
import com.github.zhoukekestar.weexquickstart.extend.MyInput;
import com.github.zhoukekestar.weexquickstart.extend.WXEventModule;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;

import org.litepal.LitePalApplication;

/**
 * MyApplication
 * 
 * @author minking
 */
public class MyApplication extends LitePalApplication {


	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化ImageLoader
		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_stub) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);

		//
		NoHttp.initialize(this);
		Logger.setDebug(true);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
		Logger.setTag("NoHttpSample");// 打印Log的tag。

		AVOSCloud.initialize(this,"zksrg6fpR18GjAsv0eHPs4Kz-gzGzoHsz","XfkYkvCvsJ1FkhEqzdTsMnNC");

		InitConfig configWeex = new InitConfig.Builder().setImgAdapter(new com.github.zhoukekestar.weexquickstart.ImageAdapter()).build();
		WXSDKEngine.initialize(this,configWeex);
		try{
			WXSDKEngine.registerModule("poneInfo", PhoneInfoModule.class);
			WXSDKEngine.registerComponent("rich", RichText.class, false);
			//
			WXSDKEngine.registerModule("myModule", WXEventModule.class);
			WXSDKEngine.registerComponent("myinput", MyInput.class);
		}catch (WXException e){
			e.printStackTrace();
		}
	}

}
