package com.whensunset.fluttercontainers.method;

import android.annotation.SuppressLint;
import android.util.Pair;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.internal.functions.Functions;

/**
 * Flutter <--> Native的通信标准化, 包含了对MethodChannel和EventChannel的约束.
 * 规则:
 * 1. 所有通信接口，参数必须为Map<String, Object>, 返回值也必须为Map<String, Object>, 即使不需要返回值和参数也必须定义成有参数和返回值的,
 * 从而达到规范化;
 * eg: Map<String, Object> invokeXXX(Map<String, Object> args);
 * 2. EventChannel通信传递的参数必须是Map<String, Object>, native返回给flutter的也必须是Map<String, Object>，
 * 同时要求返回的Map中必须包含eventKey字段，flutter层会根据eventKey来进行监听;
 */
@SuppressLint("CheckResult")
@SuppressWarnings({"ResultOfMethodCallIgnored", "unchecked"})
public abstract class BaseHandler
    implements
    MethodChannel.MethodCallHandler,
    EventChannel.StreamHandler {
  
  public final boolean mEnableEventChannel; // 是否使用event_channel
  private Observable<Map<String, Object>> mEventObservable; // event_channel observable
  
  public BaseHandler(boolean enableEvent) {
    mEnableEventChannel = enableEvent;
  }
  
  @Override
  public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
    final String method = methodCall.method;
    final Map<String, Object> arguments = obtainArgs(methodCall.arguments);
    Observable<Map<String, Object>> observable = invokeMethod(method, arguments);
    if (observable == null) { // 允许不处理
      return;
    }
    observable.subscribe(result::success, throwable -> {
      Map<String, String> errorMap = new HashMap<>();
      Pair<String, String> errorInfo = obtainErrorInfo(throwable);
      result.error(errorInfo.first, errorInfo.second, errorMap);
    });
  }
  
  @Override
  public void onListen(Object o, EventChannel.EventSink eventSink) {
    if (!mEnableEventChannel) { // 不支持EventChannel
      return;
    }
    if (mEventObservable == null) {
      mEventObservable = subscribeEvent(obtainArgs(o));
    }
    mEventObservable.doOnNext(eventSink::success)
        .doOnError(throwable -> {
          Map<String, String> errorMap = new HashMap<>();
          Pair<String, String> errorInfo = obtainErrorInfo(throwable);
          eventSink.error(errorInfo.first, errorInfo.second, errorMap);
        }).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
  }
  
  @Override
  public void onCancel(Object o) {
    mEventObservable = null;
  }
  
  private Map<String, Object> obtainArgs(Object o) {
    return (o instanceof Map) ? (Map<String, Object>) o : new HashMap<>();
  }
  
  private Pair<String, String> obtainErrorInfo(Throwable t) {
    String errorCode, errorMessage;
    errorCode = "0";
    errorMessage = t.getMessage();
    return new Pair<>(errorCode, errorMessage);
  }
  
  protected abstract Observable<Map<String, Object>> invokeMethod(String method,
                                                                  @NonNull Map<String, Object> args);
  
  protected Observable<Map<String, Object>> subscribeEvent(
      @NonNull Map<String, Object> args) {
    return null;
  }
}
