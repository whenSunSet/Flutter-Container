package com.whensunset.fluttercontainers;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.whensunset.fluttercontainers.utils.ReflectionUtil;

import java.io.File;

public class FlutterContextWrapper extends ContextWrapper {

  private AssetManager sAssets;

  FlutterContextWrapper(Context base) {
    super(base);
  }

  public void reset() {
    sAssets = null; // 在每次安装flutter包之后，需要重新创建新的assets
  }

  @Override
  public Resources getResources() {
    return new Resources(getAssets(), super.getResources().getDisplayMetrics(),
        super.getResources().getConfiguration());
  }

  @Override
  public AssetManager getAssets() {
    if (sAssets != null) {
      return sAssets;
    }

    File activeApk = new File(FlutterContainer.getFlutterInstallPath());
    if (!activeApk.isFile()) {
      return super.getAssets();
    }

    sAssets = ReflectionUtil.newInstance(AssetManager.class);
    ReflectionUtil.callMethod(sAssets, "addAssetPath", activeApk.getPath());
    return sAssets;
  }

  @Override
  public PackageManager getPackageManager() {
    return new FlutterPackageManager(super.getPackageManager());
  }
}
