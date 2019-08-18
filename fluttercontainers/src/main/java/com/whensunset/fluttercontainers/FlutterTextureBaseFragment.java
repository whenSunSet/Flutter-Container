package com.whensunset.fluttercontainers;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whensunset.fluttercontainers.utils.ViewUtil;

import io.flutter.embedding.android.FlutterFragment;
import io.flutter.embedding.android.FlutterView;

/**
 * Created by whensunset on 2019/8/12.
 */

public class FlutterTextureBaseFragment extends FlutterFragment {
  protected FlutterView mFlutterView;
  protected FlutterContextWrapper mFlutterContextWrapper;
  
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    mFlutterView = ViewUtil.getFlutterView(view);
    mFlutterContextWrapper = new FlutterContextWrapper(getContext());
    return mFlutterView;
  }
  
  @Nullable
  public FlutterView getFlutterView() {
    return mFlutterView;
  }
  
  public static class TextureBuilder extends FlutterFragment.Builder {
    @NonNull
    public <T extends FlutterFragment> T build() {
      try {
        T frag = (T) FlutterTextureBaseFragment.class.newInstance();
        if (frag == null) {
          throw new RuntimeException("The FlutterFragment subclass sent in the constructor (" + FlutterTextureBaseFragment.class.getCanonicalName() + ") does not match the expected return type.");
        } else {
          Bundle args = this.createArgs();
          frag.setArguments(args);
          return frag;
        }
      } catch (Exception var3) {
        throw new RuntimeException("Could not instantiate FlutterFragment subclass (" + FlutterTextureBaseFragment.class.getName() + ")", var3);
      }
    }
  }
  
  @Override
  public Context getContext() {
    if (mFlutterContextWrapper == null) {
      return super.getContext();
    } else {
      return mFlutterContextWrapper;
    }
  }
}
