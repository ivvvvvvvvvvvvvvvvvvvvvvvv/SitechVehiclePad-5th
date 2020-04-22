# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#————————————————————— 讯飞AIUI start —————————————————————————
-keep class com.iflytek.**{*;}
-keepattributes Signature
#————————————————————— 讯飞AIUI end —————————————————————————


#---start---
-dontshrink
-dontoptimize
-dontpreverify
-flattenpackagehierarchy
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 将.class信息中的类名重新定义为"Proguard"字符串
-renamesourcefileattribute Proguard
-keepattributes Signature,SourceFile,LineNumberTable,*JavascriptInterface*
#保持所有的JavaBean都不被混淆，防止GSON解析出错
-keep class com.sitechdev.vehicle.pad.bean.** {*;}

-keep class com.google.** {*;}

-keep class **.R$* { *;}
-keep class org.apache.**
-keep class org.apache.** { *; }

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.**

-keep class android.support.** { *; }

-keepattributes Signature
-keepclasseswithmembernames class * {     # 保持 native 方法不被混淆
    native <methods>;
}

-keepclasseswithmembers class * {         # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {         # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity { #保持类成员
   public void *(android.view.View);
}

-keepclassmembers enum * {                  # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#关于注解
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation {*;}

-dontwarn lombok.**
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

-dontwarn android.support.**

-keep public class * implements java.io.Serializable {
    public *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#-------------okhttp-------------------
# okhttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

# stetho
-dontwarn com.facebook.stetho.**

#okgo
-dontwarn com.lzy.okgo.**
-keep class com.lzy.okgo.**{*;}
#-------------okhttp-------------------

#-------------Bugly-------------------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#-------------Bugly-------------------

#-------------EventBus-------------------
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#-------------EventBus-------------------

#-------------Glide-------------------
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#如果你的 target API 低于 Android API 27，请添加：
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder
#如果你使用 DexGuard 你可能还需要添加：
## for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#-------------Glide-------------------

#-------------Amap-------------------
#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}
-keep class com.amap.**{*;}
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
#搜索
-keep   class com.amap.api.services.**{*;}
#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}
#-------------Amap-------------------

#-------------考拉听伴-------------------
-dontwarn com.kaolafm.**
-keep class com.kaolafm.** { *; }
-keepclasseswithmembers class tv.danmaku.ijk.media.player.IjkMediaPlayer {
    <fields>;
    <methods>;
}
#-------------考拉听伴-------------------

#-------------PictureSelector-------------------
#PictureSelector 2.0
-keep class com.luck.picture.lib.** { *; }

-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

 #rxjava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#rxandroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#-------------PictureSelector-------------------

#-------------androidUtilCode-------------------
-dontwarn com.blankj.utilcode.**
-keep class com.blankj.utilcode.** { *; }
#-------------考拉听伴-------------------

#----------MMKV-------------
# Keep all native methods, their classes and any classes in their descriptors
-keepclasseswithmembers,includedescriptorclasses class com.tencent.mmkv.** {
    native <methods>;
    long nativeHandle;
    private static *** onMMKVCRCCheckFail(***);
    private static *** onMMKVFileLengthError(***);
    private static *** mmkvLogImp(...);
    private static *** onContentChangedByOuterProcess(***);
}

#----------MARS-------------
-keep class com.tencent.mars.** {
  public protected private *;
}
