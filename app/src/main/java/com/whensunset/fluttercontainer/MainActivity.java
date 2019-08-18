package com.whensunset.fluttercontainer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.whensunset.fluttercontainers.FlutterContainer;

public class MainActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    RxPermissions permissions = new RxPermissions(this);
    permissions.setLogging(true);
    permissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
        .subscribe(aBoolean -> FlutterContainer.init(getApplication(), "/storage/emulated/0/flutter1.apk"));
    findViewById(R.id.aaa).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Main2Activity.class)));
  }
}
