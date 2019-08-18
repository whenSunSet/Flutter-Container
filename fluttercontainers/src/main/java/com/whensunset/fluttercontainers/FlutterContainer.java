package com.whensunset.fluttercontainers;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Created by whensunset on 2019/8/12.
 * Flutter 容器的启动类
 */

public class FlutterContainer {
  private static final String TAG = "FlutterContainer";
  private static boolean sInitialized = false;
  private static Context sApplicationContext;
  
  private static String sFlutterInstallPath = "";
  
  public static void init(@NonNull Application applicationContext,
                          @NonNull FlutterEngine.PrepareFlutterPackage prepareFlutterPackage) {
    init(applicationContext, null, prepareFlutterPackage, null);
  }
  
  public static void init(@NonNull Application applicationContext,
                          @NonNull String flutterInstallPath) {
    init(applicationContext, flutterInstallPath, null, null);
  }
  
  public static void init(@NonNull Application applicationContext,
                          @NonNull String flutterInstallPath,
                          @Nullable FlutterEngine.Callback startCallBack) {
    init(applicationContext, flutterInstallPath, null, startCallBack);
  }
  
  public static void init(@NonNull Application applicationContext,
                          @NonNull FlutterEngine.PrepareFlutterPackage prepareFlutterPackage,
                          @Nullable FlutterEngine.Callback startCallBack) {
    init(applicationContext, null, prepareFlutterPackage, startCallBack);
  }
  
  /**
   * 只能在 app 启动的时候初始化一次
   *
   * @param applicationContext
   */
  private static void init(@NonNull Application applicationContext, @Nullable String flutterInstallPath,
                           @Nullable FlutterEngine.PrepareFlutterPackage prepareFlutterPackage, @Nullable FlutterEngine.Callback startCallBack) {
    if (sInitialized) {
      return;
    }
    new FlutterManager(applicationContext);
    sInitialized = true;
    sApplicationContext = applicationContext;
    if (!TextUtils.isEmpty(flutterInstallPath)) {
      upgradeFlutterPackage(flutterInstallPath, startCallBack);
    } else if (prepareFlutterPackage != null) {
      upgradeFlutterPackage(prepareFlutterPackage, startCallBack);
    } else {
      Log.i(TAG, "FlutterContainer init no flutter package");
    }
  }
  
  /**
   * @param flutterInstallPath
   */
  public static void upgradeFlutterPackage(@NonNull String flutterInstallPath, @Nullable FlutterEngine.Callback startCallBack) {
    if (!sInitialized) {
      return;
    }
    FlutterManager.getInstance().resetFlutterPackage();
    sFlutterInstallPath = flutterInstallPath;
    FlutterManager.getInstance().getFlutterEngine().startFast(startCallBack);
  }
  
  public static void upgradeFlutterPackage(@NonNull FlutterEngine.PrepareFlutterPackage prepareFlutterPackage, @Nullable FlutterEngine.Callback startCallBack) {
    if (!sInitialized) {
      return;
    }
    FlutterManager.getInstance().resetFlutterPackage();
    FlutterManager.getInstance().getFlutterEngine().startSlow(startCallBack, () -> {
      sFlutterInstallPath = prepareFlutterPackage.prepareFlutterPackage();
      return sFlutterInstallPath;
    });
  }
  
  public static String getFlutterInstallPath() {
    return sFlutterInstallPath;
  }
  
  public static Context getApplicationContext() {
    return sApplicationContext;
  }
}
