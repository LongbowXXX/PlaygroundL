# 難読化は行わない
-dontobfuscate

# Jetpack Compose
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }
-keep class androidx.lifecycle.DefaultLifecycleObserver

# Kotlin Coroutines
-dontwarn kotlinx.coroutines.**
-keep class kotlinx.coroutines.** { *; }

# Kotlin Serialization
-dontwarn kotlinx.serialization.**
-keep,includedescriptorclasses class ** implements kotlinx.serialization.KSerializer { *; }

# Realm
-dontwarn io.realm.annotations.RealmModule
-dontwarn io.realm.**
-keep class io.realm.** { *; }
-keep @io.realm.annotations.RealmModule class *

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

# gRPC
-dontwarn io.grpc.**
-keep class io.grpc.** { *; }
-dontwarn com.google.api.**

-keep class com.google.common.collect.ImmutableMap { *; }
-dontwarn org.apache.commons.logging.**

# google api client
# Needed to keep generic types and @Key annotations accessed via reflection
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault
-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}
-keep class com.google.api.client.util.** { *; }
-keep class com.google.api.client.http.** { *; }
-keep class com.google.api.client.json.** { *; }
-keep class io.opencensus.trace.** { *; }
-keep class com.google.api.services.customsearch.** { *; }
-keep class net.longbowxxx.** { *; }

# JSoup
-keeppackagenames org.jsoup.nodes