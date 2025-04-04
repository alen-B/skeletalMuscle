package com.ninja.appupdate.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J \u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tH&J\b\u0010\n\u001a\u00020\u0003H&\u00a8\u0006\u000b"}, d2 = {"Lcom/ninja/appupdate/base/BaseHttpDownLoadManager;", "", "cancle", "", "download", "apkUrl", "", "apkName", "listener", "Lcom/skeletalmuscle/appupdate/listener/OnDownloadListener;", "release", "lib_appupdate_debug"})
public abstract interface BaseHttpDownLoadManager {
    
    public abstract void download(@org.jetbrains.annotations.NotNull
    java.lang.String apkUrl, @org.jetbrains.annotations.NotNull
    java.lang.String apkName, @org.jetbrains.annotations.NotNull
    com.skeletalmuscle.appupdate.listener.OnDownloadListener listener);
    
    public abstract void cancle();
    
    public abstract void release();
}