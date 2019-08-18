package com.whensunset.fluttercontainers.utils;


import android.util.Log;

import com.whensunset.fluttercontainers.BuildConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import io.reactivex.exceptions.Exceptions;

/**
 * Created by whensunset on 2019/2/27.
 * 在 debug 版本用到的工具类
 */

public class DebugUtil {
  
  /**
   * debug 版本 crash
   *
   * @param tag
   * @param msg
   */
  public static void logError(String tag, String msg) {
    Log.e(tag, msg);
    if (BuildConfig.DEBUG) {
      throw new RuntimeException(msg);
    }
  }
  
  /**
   * debug 版本 crash
   *
   * @param throwable
   */
  public static void logError(Throwable throwable) {
    logCrashStackTrace(throwable);
    if (BuildConfig.DEBUG) {
      Exceptions.propagate(throwable);
    }
  }
  
  
  public static String getStackTraceString(Throwable tr) {
    String result = "";
    if (tr == null) {
      return result;
    }
    
    // This is to reduce the amount of log spew that apps do in the non-error
    // condition of the network being unavailable.
    Throwable t = tr;
    while (t != null) {
      if (t instanceof UnknownHostException) {
        return result;
      }
      t = t.getCause();
    }
    
    try (StringWriter sw = new StringWriter()) {
      PrintWriter pw = new PrintWriter(sw);
      tr.printStackTrace(pw);
      pw.flush();
      result = sw.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }
  
  public static void logCrashStackTrace(Throwable throwable) {
    logCrashStackTrace("@crash", throwable);
  }
  
  public static void logCrashStackTrace(String tag, Throwable throwable) {
    Log.e(tag, getStackTraceString(throwable), null);
  }
}
