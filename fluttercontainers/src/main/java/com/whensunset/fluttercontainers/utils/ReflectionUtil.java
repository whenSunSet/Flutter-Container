package com.whensunset.fluttercontainers.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by whensunset on 2019/8/11.
 */

public class ReflectionUtil {
  
  private final static Map<Class<?>, Class<?>> PRIMITIVE_MAP = new HashMap<>();
  
  static {
    PRIMITIVE_MAP.put(Boolean.class, boolean.class);
    PRIMITIVE_MAP.put(Byte.class, byte.class);
    PRIMITIVE_MAP.put(Character.class, char.class);
    PRIMITIVE_MAP.put(Short.class, short.class);
    PRIMITIVE_MAP.put(Integer.class, int.class);
    PRIMITIVE_MAP.put(Float.class, float.class);
    PRIMITIVE_MAP.put(Long.class, long.class);
    PRIMITIVE_MAP.put(Double.class, double.class);
    PRIMITIVE_MAP.put(boolean.class, boolean.class);
    PRIMITIVE_MAP.put(byte.class, byte.class);
    PRIMITIVE_MAP.put(char.class, char.class);
    PRIMITIVE_MAP.put(short.class, short.class);
    PRIMITIVE_MAP.put(int.class, int.class);
    PRIMITIVE_MAP.put(float.class, float.class);
    PRIMITIVE_MAP.put(long.class, long.class);
    PRIMITIVE_MAP.put(double.class, double.class);
  }
  
  public static <T> T newInstance(Class<?> clazz, Object... args) {
    try {
      return newInstanceOrThrow(clazz, args);
    } catch (Throwable e) {
      return null;
    }
  }
  
  @SuppressWarnings("unchecked")
  public static <T> T newInstanceOrThrow(Class<?> clazz, Object... args)
      throws SecurityException, NoSuchMethodException,
      IllegalArgumentException, InstantiationException,
      IllegalAccessException, InvocationTargetException {
    Constructor<?> constructor = clazz
        .getConstructor(getParameterTypes(args));
    return (T) constructor.newInstance(getParameters(args));
  }
  
  private static Class<?>[] getParameterTypes(Object... args) {
    Class<?>[] parameterTypes = null;
    
    if (args != null && args.length > 0) {
      parameterTypes = new Class<?>[args.length];
      for (int i = 0; i < args.length; i++) {
        Object param = args[i];
        if (param != null && param instanceof JavaParam<?>) {
          parameterTypes[i] = ((JavaParam<?>) param).clazz;
        } else {
          parameterTypes[i] = param == null ? null : param.getClass();
        }
      }
    }
    return parameterTypes;
  }
  
  private static Object[] getParameters(Object... args) {
    Object[] parameters = null;
    
    if (args != null && args.length > 0) {
      parameters = new Object[args.length];
      for (int i = 0; i < args.length; i++) {
        Object param = args[i];
        if (param != null && param instanceof JavaParam<?>) {
          parameters[i] = ((JavaParam<?>) param).obj;
        } else {
          parameters[i] = param;
        }
      }
    }
    return parameters;
  }
  
  public static <T> T callMethod(Object targetInstance, String methodName, Object... args) {
    try {
      return callMethodOrThrow(targetInstance, methodName, args);
    } catch (Throwable e) {
      return null;
    }
  }
  
  @SuppressWarnings("unchecked")
  private static <T> T callMethodOrThrow(Object targetInstance,
                                         String methodName, Object... args) throws SecurityException,
      NoSuchMethodException, IllegalArgumentException,
      IllegalAccessException, InvocationTargetException {
    final Class<?> clazz = targetInstance.getClass();
    
    Method method = getDeclaredMethod(clazz, methodName,
        getParameterTypes(args));
    return (T) method.invoke(targetInstance, getParameters(args));
  }
  
  private static Method getDeclaredMethod(final Class<?> clazz, String name,
                                          Class<?>... parameterTypes) throws NoSuchMethodException,
      SecurityException {
    Method[] methods = clazz.getDeclaredMethods();
    Method method = findMethodByName(methods, name, parameterTypes);
    if (method == null) {
      if (clazz.getSuperclass() != null) {
        // find in parent
        return getDeclaredMethod(clazz.getSuperclass(), name, parameterTypes);
      } else {
        throw new NoSuchMethodException();
      }
    }
    method.setAccessible(true);
    return method;
  }
 
  private static Method findMethodByName(Method[] list, String name,
                                         Class<?>[] parameterTypes) {
    if (name == null) {
      throw new NullPointerException("Method name must not be null.");
    }
    
    for (Method method : list) {
      if (method.getName().equals(name)
          && compareClassLists(method.getParameterTypes(),
          parameterTypes)) {
        return method;
      }
    }
    return null;
  }
  
  private static boolean compareClassLists(Class<?>[] a, Class<?>[] b) {
    if (a == null) {
      return (b == null) || (b.length == 0);
    }
    
    if (b == null) {
      return (a.length == 0);
    }
    
    if (a.length != b.length) {
      return false;
    }
    
    for (int i = 0; i < a.length; ++i) {
      // if a[i] and b[i] is not same, return false
      if (!(a[i].isAssignableFrom(b[i])
          || (PRIMITIVE_MAP.containsKey(a[i]) && PRIMITIVE_MAP.get(
          a[i]).equals(PRIMITIVE_MAP.get(b[i]))))) {
        return false;
      }
    }
    return true;
  }
  
  public static class JavaParam<T> {
    public final Class<? extends T> clazz;
    public final T obj;
    
    public JavaParam(Class<? extends T> clazz, T obj) {
      this.clazz = clazz;
      this.obj = obj;
    }
  }
}
