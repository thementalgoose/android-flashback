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

-keep class tmg.flashback.rss.network.apis.model.**
-keep class tmg.flashback.rss.network.apis.*
-keep class tmg.flashback.rss.network.shared.*
-keep class tmg.flashback.rss.network.**
-keep class tmg.flashback.rss.repo.json.**
-keep class tmg.flashback.rss.repo.model.**
-keep class tmg.flashback.statistics.repository.models.**

# (2)Simple XML
-keep public class org.simpleframework.**{ *; }
-keep class org.simpleframework.xml.**{ *; }
-keep class org.simpleframework.xml.core.**{ *; }
-keep class org.simpleframework.xml.util.**{ *; }
-keep class retrofit2.converter.simplexml.** { *; }
# (1)Annotations and signatures
-keepattributes *Annotation*
-keepattributes Signature

-printmapping mapping.txt
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception