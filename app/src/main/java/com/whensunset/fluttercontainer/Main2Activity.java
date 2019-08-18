package com.whensunset.fluttercontainer;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import com.whensunset.fluttercontainers.FlutterTextureBaseActivity;

public class Main2Activity extends FlutterTextureBaseActivity {
  
  Button mButton;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mButton = new Button(getApplicationContext());
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    mButton.setLayoutParams(layoutParams);
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    getFlutterViewParent().addView(mButton);
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    getFlutterView().bringToFront();
  }
}
