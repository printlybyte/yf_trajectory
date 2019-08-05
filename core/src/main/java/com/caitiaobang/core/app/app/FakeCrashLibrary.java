package com.caitiaobang.core.app.app;

/**
 * ============================================
 * 描  述：
 * 包  名：com.caitiaobang.core.app.app
 * 类  名：FakeCrashLibrary
 * 创建人：lgd
 * 创建时间：2019/6/26 13:29
 * ============================================
 **/

/** Not a real crash reporting library! */
public final class FakeCrashLibrary {
    public static void log(int priority, String tag, String message) {
        // TODO add log entry to circular buffer.
    }

    public static void logWarning(Throwable t) {
        // TODO report non-fatal warning.
    }

    public static void logError(Throwable t) {
        // TODO report non-fatal error.
    }

    private FakeCrashLibrary() {
        throw new AssertionError("No instances.");
    }
}