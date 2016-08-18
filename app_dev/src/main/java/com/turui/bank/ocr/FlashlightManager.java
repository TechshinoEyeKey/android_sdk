/*   1:    */
package com.turui.bank.ocr;
/*   2:    */ 
/*   3:    */

import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*   4:    */
/*   5:    */
/*   6:    */

/*   7:    */
/*   8:    */ final class FlashlightManager
/*   9:    */ {
  /*  10: 39 */   private static final String TAG = FlashlightManager.class.getSimpleName();
  /*  11: 44 */   private static final Object iHardwareService = getHardwareService();
  /*  12: 45 */   private static final Method setFlashEnabledMethod = getSetFlashEnabledMethod(iHardwareService);

  /*  13:    */
/*  14:    */   static
/*  15:    */ {
/*  16: 46 */
    if (iHardwareService == null) {
/*  17: 47 */
      Log.v(TAG, "This device does supports control of a flashlight");
/*  18:    */
    } else {
/*  19: 49 */
      Log.v(TAG, "This device does not support control of a flashlight");
/*  20:    */
    }
/*  21:    */
  }

  /*  22:    */
/*  23:    */
  static void enableFlashlight()
/*  24:    */ {
/*  25: 61 */
    setFlashlight(false);
/*  26:    */
  }

  /*  27:    */
/*  28:    */
  static void disableFlashlight()
/*  29:    */ {
/*  30: 65 */
    setFlashlight(false);
/*  31:    */
  }

  /*  32:    */
/*  33:    */
  private static Object getHardwareService()
/*  34:    */ {
/*  35: 69 */
    Class<?> serviceManagerClass = maybeForName("android.os.ServiceManager");
/*  36: 70 */
    if (serviceManagerClass == null) {
/*  37: 71 */
      return null;
/*  38:    */
    }
/*  39: 74 */
    Method getServiceMethod = maybeGetMethod(serviceManagerClass, "getService", new Class[]{String.class});
/*  40: 75 */
    if (getServiceMethod == null) {
/*  41: 76 */
      return null;
/*  42:    */
    }
/*  43: 79 */
    Object hardwareService = invoke(getServiceMethod, null, new Object[]{"hardware"});
/*  44: 80 */
    if (hardwareService == null) {
/*  45: 81 */
      return null;
/*  46:    */
    }
/*  47: 84 */
    Class<?> iHardwareServiceStubClass = maybeForName("android.os.IHardwareService$Stub");
/*  48: 85 */
    if (iHardwareServiceStubClass == null) {
/*  49: 86 */
      return null;
/*  50:    */
    }
/*  51: 89 */
    Method asInterfaceMethod = maybeGetMethod(iHardwareServiceStubClass, "asInterface", new Class[]{IBinder.class});
/*  52: 90 */
    if (asInterfaceMethod == null) {
/*  53: 91 */
      return null;
/*  54:    */
    }
/*  55: 94 */
    return invoke(asInterfaceMethod, null, new Object[]{hardwareService});
/*  56:    */
  }

  /*  57:    */
/*  58:    */
  private static Method getSetFlashEnabledMethod(Object iHardwareService)
/*  59:    */ {
/*  60: 98 */
    if (iHardwareService == null) {
/*  61: 99 */
      return null;
/*  62:    */
    }
/*  63:101 */
    Class<?> proxyClass = iHardwareService.getClass();
/*  64:102 */
    return maybeGetMethod(proxyClass, "setFlashlightEnabled", new Class[]{Boolean.TYPE});
/*  65:    */
  }

  /*  66:    */
/*  67:    */
  private static Class<?> maybeForName(String name)
/*  68:    */ {
/*  69:    */
    try
/*  70:    */ {
/*  71:107 */
      return Class.forName(name);
/*  72:    */
    }
/*  73:    */ catch (ClassNotFoundException cnfe)
/*  74:    */ {
/*  75:110 */
      return null;
/*  76:    */
    }
/*  77:    */ catch (RuntimeException re)
/*  78:    */ {
/*  79:112 */
      Log.w(TAG, "Unexpected error while finding class " + name, re);
/*  80:    */
    }
/*  81:113 */
    return null;
/*  82:    */
  }

  /*  83:    */
/*  84:    */
  private static Method maybeGetMethod(Class<?> clazz, String name, Class<?>... argClasses)
/*  85:    */ {
/*  86:    */
    try
/*  87:    */ {
/*  88:119 */
      return clazz.getMethod(name, argClasses);
/*  89:    */
    }
/*  90:    */ catch (NoSuchMethodException nsme)
/*  91:    */ {
/*  92:122 */
      return null;
/*  93:    */
    }
/*  94:    */ catch (RuntimeException re)
/*  95:    */ {
/*  96:124 */
      Log.w(TAG, "Unexpected error while finding method " + name, re);
/*  97:    */
    }
/*  98:125 */
    return null;
/*  99:    */
  }

  /* 100:    */
/* 101:    */
  private static Object invoke(Method method, Object instance, Object... args)
/* 102:    */ {
/* 103:    */
    try
/* 104:    */ {
/* 105:131 */
      return method.invoke(instance, args);
/* 106:    */
    }
/* 107:    */ catch (IllegalAccessException e)
/* 108:    */ {
/* 109:133 */
      Log.w(TAG, "Unexpected error while invoking " + method, e);
/* 110:134 */
      return null;
/* 111:    */
    }
/* 112:    */ catch (InvocationTargetException e)
/* 113:    */ {
/* 114:136 */
      Log.w(TAG, "Unexpected error while invoking " + method, e.getCause());
/* 115:137 */
      return null;
/* 116:    */
    }
/* 117:    */ catch (RuntimeException re)
/* 118:    */ {
/* 119:139 */
      Log.w(TAG, "Unexpected error while invoking " + method, re);
/* 120:    */
    }
/* 121:140 */
    return null;
/* 122:    */
  }

  /* 123:    */
/* 124:    */
  private static void setFlashlight(boolean active)
/* 125:    */ {
/* 126:145 */
    if (iHardwareService != null) {
/* 127:146 */
      invoke(setFlashEnabledMethod, iHardwareService, new Object[]{Boolean.valueOf(active)});
/* 128:    */
    }
/* 129:    */
  }
/* 130:    */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.turui.bank.ocr.FlashlightManager

 * JD-Core Version:    0.7.0.1

 */