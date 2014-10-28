#JSON classes
-keep class com.robinfinch.journal.domain.** { *; }
-keep class com.robinfinch.journal.app.rest.DiffResponse { *; }

#Butterknife
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}
-dontwarn butterknife.internal.**

#Retrofit
-keep class retrofit.** { *; }
-dontwarn rx.**
-dontwarn retrofit.appengine.**
-dontwarn retrofit.client.OkClient

