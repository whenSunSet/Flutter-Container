package com.whensunset.fluttercontainers.utils;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import io.flutter.embedding.android.FlutterView;

public class ViewUtil {
  
  /**
   * 查找 FlutterView
   * @param view
   * @return
   */
  @Nullable
  public static FlutterView getFlutterView(View view) {
    if (view instanceof FlutterView) {
      return (FlutterView) view;
    } else if (view instanceof ViewGroup) {
      ViewGroup parentView = (ViewGroup) view;
      for (int i = 0; i < parentView.getChildCount(); i++) {
        View childView = parentView.getChildAt(i);
        if (childView instanceof FlutterView) {
          return (FlutterView) childView;
        } else if (childView instanceof ViewGroup) {
          return getFlutterView(childView);
        }
      }
      return null;
    } else {
      return null;
    }
  }
}
