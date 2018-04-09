package com.xiaoshu.geocodinglibrary.utils;

import android.util.Log;

/**
 * 日志工具
 * Created by XiaoShu on 2017/6/24.
 */

public class LogUtils {
    private static final String TAG = "XiaoShuLog";
    private static boolean showLog = true;

    public static void e(Object o){
        dispose(Log.ERROR, o);
    }

    public static void i(Object o){
        dispose(Log.INFO, o);
    }

    public static void i(Object... objects){
        dispose(Log.INFO, objects);
    }

    public static void w(Object o){
        dispose(Log.WARN, o);
    }

    public static void d(Object o){
        dispose(Log.DEBUG, o);
    }

    public static void d(Object... objects){
        dispose(Log.DEBUG, objects);
    }

    private static void dispose(int i, Object o){
        if (showLog) {
            String msg;
            if (null != o) {
                msg = o.toString();
            } else {
                msg = "[null]";
            }
            printLog(i, msg);
        }
    }

    public static void dispose(int i, Object... objects){
        if (showLog) {
            StringBuilder msg = null;
            for (Object object : objects) {
                if (null == msg) {
                    msg = new StringBuilder("[" + object + "]");
                } else {
                    msg.append(" [").append(object).append("]");
                }
            }
            printLog(i, msg.toString());
        }
    }

    private static void printLog(int i, String msg){
        StackTraceElement element = Thread.currentThread().getStackTrace()[5];

        msg  = "\n" + msg;
        switch (i) {
            case Log.DEBUG:
                Log.d(TAG, element.toString() + msg);
                break;
            case Log.WARN:
                Log.w(TAG, element.toString() + msg);
                break;
            case Log.ERROR:
                Log.e(TAG, element.toString() + msg);
                break;
            default:
            case Log.INFO:
                Log.i(TAG, element.toString() + msg);
                break;
        }
    }

    public static void setShowLog(boolean showLog) {
        LogUtils.showLog = showLog;
    }
}
