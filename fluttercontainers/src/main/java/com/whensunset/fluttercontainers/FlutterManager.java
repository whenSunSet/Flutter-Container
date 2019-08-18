package com.whensunset.fluttercontainers;

import android.app.Application;
import android.content.Context;

import com.whensunset.fluttercontainers.method.BaseHandler;

import java.io.File;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;


public class FlutterManager {
  
  private static FlutterManager sInstance;
  
  private final FlutterEngine mFlutterEngine;
  private final FlutterContextWrapper mFlutterContextWrapper;
  private final Context mContext;
  
  FlutterManager(Application context) {
    sInstance = this; // 简单单例, 线程并不安全, 逻辑保证
    mFlutterEngine = new FlutterEngine(context);
    mFlutterContextWrapper = new FlutterContextWrapper(context);
    mContext = context;
  }
  
  public static FlutterManager getInstance() {
    return sInstance;
  }
  
  public void registerChannel(BinaryMessenger messenger, String channel, BaseHandler handler) {
    new MethodChannel(messenger, channel + ".method").setMethodCallHandler(handler);
    if (handler.mEnableEventChannel) {
      new EventChannel(messenger, channel + ".event").setStreamHandler(handler);
    }
  }
  
  FlutterEngine getFlutterEngine() {
    return mFlutterEngine;
  }
  
  public FlutterContextWrapper getFlutterContextWrapper() {
    return mFlutterContextWrapper;
  }
  
  /**
   * 是否有 Flutter 包可用
   *
   * @return
   */
  public boolean isFlutterAvailable() {
    File activeApk = new File(FlutterContainer.getFlutterInstallPath());
    return activeApk.isFile();
  }
  
  /**
   * 如果要使用新的 Flutter 包，那么需要重置一下
   */
  void resetFlutterPackage() {
    mFlutterContextWrapper.reset();
  }
}
