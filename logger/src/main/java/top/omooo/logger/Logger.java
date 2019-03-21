package top.omooo.logger;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Omooo
 * Date:2019/3/21
 * 统一日志类
 */
public final class Logger {
    private static final String TAG = "Logger";
    private static final String TOP_LINE = "┌────────────────────────────────────────────────────────────────────";
    private static final String BOTTOM_LINE = "└────────────────────────────────────────────────────────────────────";
    private static final String CENTER_LINE = "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";

    private Logger() {

    }

    public static void e(String mag) {
        e(TAG, mag);
    }

    public static void e(@NonNull String tag, String mag) {
        Log.e(tag, TOP_LINE);
        Log.e(tag, "│ " + tag);
        Log.e(tag, CENTER_LINE);
        Log.e(tag, "│ " + mag);
        Log.e(tag, BOTTOM_LINE);

    }

    public static void i(String mag) {
        i(TAG, mag);
    }

    public static void i(@NonNull String tag, String mag) {
        Log.i(tag, TOP_LINE);
        Log.i(tag, "│ " + tag);
        Log.i(tag, CENTER_LINE);
        Log.i(tag, "│ " + mag);
        Log.i(tag, BOTTOM_LINE);

    }

    public static void d(String mag) {
        d(TAG, mag);
    }

    public static void d(@NonNull String tag, String mag) {
        Log.d(tag, TOP_LINE);
        Log.d(tag, "│ " + tag);
        Log.d(tag, CENTER_LINE);
        Log.d(tag, "│ " + mag);
        Log.d(tag, BOTTOM_LINE);

    }
}
