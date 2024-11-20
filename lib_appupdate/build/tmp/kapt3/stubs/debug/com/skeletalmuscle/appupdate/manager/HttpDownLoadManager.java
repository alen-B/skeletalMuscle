package com.skeletalmuscle.appupdate.manager;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0015\u001a\u00020\u0016H\u0016J \u0010\u0017\u001a\u00020\u00162\u0006\u0010\b\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0018\u001a\u00020\u0016H\u0002J\b\u0010\u0019\u001a\u00020\u0016H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\u0004R\u0016\u0010\f\u001a\n \u000e*\u0004\u0018\u00010\r0\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/skeletalmuscle/appupdate/manager/HttpDownLoadManager;", "Lcom/ninja/appupdate/base/BaseHttpDownLoadManager;", "downloadPath", "", "(Ljava/lang/String;)V", "HTTP_TIME_OUT", "", "apkName", "apkUrl", "getDownloadPath", "()Ljava/lang/String;", "setDownloadPath", "executor", "Ljava/util/concurrent/ExecutorService;", "kotlin.jvm.PlatformType", "listener", "Lcom/skeletalmuscle/appupdate/listener/OnDownloadListener;", "runnable", "Ljava/lang/Runnable;", "shutdown", "", "cancle", "", "download", "fullDownload", "release", "lib_appupdate_debug"})
public final class HttpDownLoadManager implements com.ninja.appupdate.base.BaseHttpDownLoadManager {
    @org.jetbrains.annotations.NotNull
    private java.lang.String downloadPath;
    private final int HTTP_TIME_OUT = 10000;
    private boolean shutdown = false;
    private java.lang.String apkUrl = "";
    private java.lang.String apkName = "";
    private com.skeletalmuscle.appupdate.listener.OnDownloadListener listener;
    private final java.util.concurrent.ExecutorService executor = null;
    private final java.lang.Runnable runnable = null;
    
    public HttpDownLoadManager(@org.jetbrains.annotations.NotNull
    java.lang.String downloadPath) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDownloadPath() {
        return null;
    }
    
    public final void setDownloadPath(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    private final void fullDownload() {
    }
    
    @java.lang.Override
    public void download(@org.jetbrains.annotations.NotNull
    java.lang.String apkUrl, @org.jetbrains.annotations.NotNull
    java.lang.String apkName, @org.jetbrains.annotations.NotNull
    com.skeletalmuscle.appupdate.listener.OnDownloadListener listener) {
    }
    
    @java.lang.Override
    public void cancle() {
    }
    
    @java.lang.Override
    public void release() {
    }
}