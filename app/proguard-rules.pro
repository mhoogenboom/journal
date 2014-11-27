#JSON classes
-keep class com.robinfinch.journal.domain.** { *; }
-keep class com.robinfinch.journal.app.rest.DiffResponse { *; }
-keep class com.robinfinch.journal.app.sync.Revision { *; }

#Butterknife
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}
-dontwarn butterknife.internal.**

#OkHttp
-keep class com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**

#Retrofit
-keep class retrofit.** { *; }
-dontwarn retrofit.**
-dontwarn rx.**

#Dagger
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
}
-keep class **$$ModuleAdapter
-keep class **$$InjectAdapter
-dontwarn dagger.internal.codegen.**
