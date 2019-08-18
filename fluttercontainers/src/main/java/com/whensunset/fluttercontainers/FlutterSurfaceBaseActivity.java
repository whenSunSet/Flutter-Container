package com.whensunset.fluttercontainers;

import android.os.Bundle;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.view.FlutterNativeView;


public class FlutterSurfaceBaseActivity extends FlutterActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
  }

  @Override
  public FlutterNativeView createFlutterNativeView() {
    return new FlutterNativeView(FlutterManager.getInstance().getFlutterContextWrapper());
  }

  static final class GeneratedPluginRegistrant {

    static void registerWith(PluginRegistry registry) {
      final String key = GeneratedPluginRegistrant.class.getCanonicalName();

      if (registry.hasPlugin(key)) {
        return;
      }
      registry.registrarFor(key);
    }
  }
}
