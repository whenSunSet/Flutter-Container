package com.whensunset.fluttercontainers;

import android.content.Context;

import com.whensunset.fluttercontainers.utils.DebugUtil;

import io.flutter.view.FlutterMain;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.schedulers.Schedulers;

/**
 * 负责Flutter引擎的启动方法.
 */
class FlutterEngine {
  
  private static boolean sInitialized; // 全局标记引擎已经启动
  private final Context mContext;
  
  FlutterEngine(Context context) {
    mContext = context;
  }
  
  /**
   * 快速启动模式，表示已经有包了
   */
  void startFast(@Nullable Callback callback) {
    if (sInitialized) {
      // 需要尽快启动，所以需要去重
      callback(callback, null);
      return;
    }
    if (FlutterManager.getInstance().isFlutterAvailable()) { // 当前有可用包
      startFlutterInitialization();
      ensureInitializationComplete();
      callback(callback, null);
    } else {
      DebugUtil.logError(new RuntimeException("startFast but no available package"));
    }
  }
  
  /**
   * 慢速启动模式, 表示没有报，需要准备
   */
  void startSlow(@Nullable Callback callback, @NonNull PrepareFlutterPackage prepareFlutterPackage) {
    Single.fromCallable(() -> {
      // 此处不去重, 不管是否sInitialized都重新初始化, 保证使用最新flutter包.
      prepareFlutterPackage.prepareFlutterPackage();
      return new Object();
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(o -> {
          startFlutterInitialization();
          ensureInitializationComplete();
          callback(callback, null);
        }, throwable -> {
          throwable.printStackTrace();
          callback(callback, throwable);
        });
  }
  
  private static void callback(@Nullable Callback callback, Throwable t) {
    if (callback != null) {
      callback.onCompleted(t);
    }
  }
  
  private void startFlutterInitialization() { // 不阻塞UI
    // Flutter SDK的start方法可以多次调用, 他的主要作用是解压资源, 因此不用做去重
    FlutterMain.startInitialization(FlutterManager.getInstance().getFlutterContextWrapper());
  }
  
  private void ensureInitializationComplete() {
    FlutterMain.ensureInitializationComplete(mContext, null);
    sInitialized = true; // 已经初始化
  }
  
  // 启动回调
  public interface Callback {
    
    /**
     * 初始化完成.
     *
     * @param e 成功为null，失败不为null.
     */
    void onCompleted(Throwable e);
  }
 
  // 准备 Flutter 包的回调
  public interface PrepareFlutterPackage {
    String prepareFlutterPackage();
  }
}
