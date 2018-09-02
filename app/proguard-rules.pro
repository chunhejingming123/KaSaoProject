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

#忽略警告
-ignorewarnings
-dontwarn

-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose

#忽略反射类
-keepattributes Signature
-keepattributes *Annotation*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
    *;
}
-keep class * implements java.io.Serializable {
    *;
}
-keep public class * {
    public protected *;
}
-keep class **.R$* {
    *;
}

# keep getters/setters in RotatingDrawable so that animations can still work.
-keepclassmembers class com.getbase.floatingactionbutton.FloatingActionsMenu$RotatingDrawable {
   void set*(***);
   *** get*();
}


-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keep class **.R$* {
    *;
}
## ----------------------------------
## ########## 不混淆 v4,v7 ##########
## --------------------------------
-keep class android.support.v4.** {*;}
-keep class android.support.v7.** {*;}
-keep class Android.support.design.widget.TabLayout{*;}


#android support 库不混淆
-keep class android.support.**{*;}

#----------------------------------实体类--------------------------------------
#忽略所有的model
-keep class com.kasao.qintai.model.**{*;}
-keep class com.kasao.qintai.model.domain.**{*;}
-keep class com.kasao.qintai.adapter.**{*;}
-keepclasseswithmembers class com.kasao.qintai.adapter.**{*;}
-keepclasseswithmembernames class com.kasao.qintai.adapter.**{*;}
-keep class com.kasao.qintaiframework.**{*;}
##-------------------地图
-keep class com.amap.api.services.**{*;}
-keep class com.amap.api.**{*;}
-keep class com.autonavi.**{*;}
#OkHttp
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
#retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
#RxJava/RxAndroid
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
#jsoup 在webView 使用
-keep class org.jsoup.*
# 微信混淆
-keep class com.tencent.mm.opensdk.** {
*;
}
-keep class com.tencent.wxop.** {
*;
}
-keep class com.tencent.mm.sdk.** {
*;
}
#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

