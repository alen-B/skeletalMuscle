package com.skeletalmuscle.appupdate.listener;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H&J\u0014\u0010\u0007\u001a\u00020\u00032\n\u0010\b\u001a\u00060\tj\u0002`\nH&J\u0018\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH&J\b\u0010\u000f\u001a\u00020\u0003H&\u00a8\u0006\u0010"}, d2 = {"Lcom/skeletalmuscle/appupdate/listener/OnDownloadListener;", "", "cancel", "", "done", "apk", "Ljava/io/File;", "error", "e", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onDownLoading", "max", "", "progress", "start", "lib_appupdate_release"})
public abstract interface OnDownloadListener {
    
    public abstract void start();
    
    public abstract void onDownLoading(int max, int progress);
    
    public abstract void cancel();
    
    public abstract void done(@org.jetbrains.annotations.NotNull
    java.io.File apk);
    
    public abstract void error(@org.jetbrains.annotations.NotNull
    java.lang.Exception e);
}