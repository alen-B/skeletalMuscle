package com.skeletalmuscle.appupdate.utils;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\tJ\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\b\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u0010\u0010\u0011\u001a\u00020\r2\u0006\u0010\b\u001a\u00020\u000eH\u0007R\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0012"}, d2 = {"Lcom/skeletalmuscle/appupdate/utils/ApkUtils;", "", "()V", "GET_UNKNOWN_APP_SOURCES", "", "getGET_UNKNOWN_APP_SOURCES", "()I", "getVersionCode", "context", "Landroid/content/Context;", "getVersionName", "", "installAPK", "", "Landroid/app/Activity;", "apk", "Ljava/io/File;", "startPackageInstallActivity", "lib_appupdate_debug"})
public final class ApkUtils {
    @org.jetbrains.annotations.NotNull
    public static final com.skeletalmuscle.appupdate.utils.ApkUtils INSTANCE = null;
    private static final int GET_UNKNOWN_APP_SOURCES = 200;
    
    private ApkUtils() {
        super();
    }
    
    public final int getGET_UNKNOWN_APP_SOURCES() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getVersionName(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return null;
    }
    
    public final int getVersionCode(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return 0;
    }
    
    public final void installAPK(@org.jetbrains.annotations.NotNull
    android.app.Activity context, @org.jetbrains.annotations.NotNull
    java.io.File apk) {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public final void startPackageInstallActivity(@org.jetbrains.annotations.NotNull
    android.app.Activity context) {
    }
}