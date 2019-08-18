package com.whensunset.fluttercontainers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.engine.FlutterShellArgs;

public class FlutterTextureBaseActivity extends FlutterActivity {
  protected FlutterView mFlutterView;
  protected FlutterTextureBaseFragment mFlutterTextureBaseFragment;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }
  
  @Nullable
  public ViewGroup getFlutterViewParent() {
    getFlutterView();
    if (mFlutterView == null) {
      return null;
    } else {
      return (ViewGroup) mFlutterView.getParent();
    }
  }
  
  @Nullable
  public FlutterView getFlutterView() {
    if (mFlutterTextureBaseFragment == null) {
      return null;
    } else if (mFlutterTextureBaseFragment.getView() != null) {
      mFlutterView = mFlutterTextureBaseFragment.getFlutterView();
      return mFlutterView;
    } else {
      return null;
    }
  }
  
  @Nullable
  public FlutterTextureBaseFragment getFlutterTextureBaseFragment() {
    return mFlutterTextureBaseFragment;
  }
  
  @NonNull
  protected FlutterTextureBaseFragment createFlutterFragment() {
    mFlutterTextureBaseFragment = (new FlutterTextureBaseFragment.TextureBuilder())
        .dartEntrypoint(this.getDartEntrypoint())
        .initialRoute(this.getInitialRoute())
        .appBundlePath(this.getAppBundlePath())
        .flutterShellArgs(FlutterShellArgs.fromIntent(this.getIntent()))
        .renderMode(FlutterView.RenderMode.texture)
        .transparencyMode(FlutterView.TransparencyMode.opaque)
        .build();
    return mFlutterTextureBaseFragment;
  }
}
