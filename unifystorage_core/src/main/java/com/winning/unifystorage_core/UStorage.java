package com.winning.unifystorage_core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.tencent.mmkv.MMKV;
import com.winning.unifystorage_core.Utils.CommonUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public final class UStorage {
    private final Map<Method, ServiceMethod<?>> serviceMethodCache = new ConcurrentHashMap<>();
    private static Application application;
    private static int activityCount = 0;
    public  static Realm realm;
    private  static String rootDir;
    public  static MMKV kv;

    private UStorage(Builder builder){
        realm = builder.realmDefault;
    }

    public static void initialize(Application context){
        application = context;
        registerActivityListener();
        Realm.init(application);

        rootDir = MMKV.initialize(application);
        kv = MMKV.defaultMMKV();
    }

    @SuppressWarnings("unchecked") // Single-interface proxy creation guarded by parameter safety.
    public <T>  T create(final Class<T> service) {
        CommonUtil.validateServiceInterface(service);

        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new InvocationHandler() {
                    private final Object[] emptyArgs = new Object[0];

                    @Override
                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                        // If the method is a method from Object then defer to normal invocation.
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }

                        return loadServiceMethod(method).invoke(args != null ? args : emptyArgs);
                    }
                });
    }

    ServiceMethod<?> loadServiceMethod(Method method) {
        ServiceMethod<?> result = serviceMethodCache.get(method);
        if (result != null) return result;

        synchronized (serviceMethodCache) {
            result = serviceMethodCache.get(method);
            if (result == null) {
                result = ServiceMethod.parseAnnotations(this, method);
                serviceMethodCache.put(method, result);
            }
        }
        return result;
    }

    public static final class Builder {
        private static final String DEFAULT_DB_NAME = "winningStorage.realm";

        private String dbName;
        private int schemaVersion = 0;
        private BaseMigration migration;
        private Realm realmDefault;

        public Builder() {
        }

        public Builder setDbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public Builder setSchemaVersion(int schemaVersion) {
            this.schemaVersion = schemaVersion;
            return this;
        }

        public Builder setMigration(BaseMigration migration) {
            this.migration = migration;
            return this;
        }

        public UStorage build() {
            configDB();

            return new UStorage(this);
        }

        private void configDB(){
            RealmConfiguration.Builder otherConfigBuilder = new RealmConfiguration.Builder()
                    .name(CommonUtil.isEmptyStr(dbName) ? DEFAULT_DB_NAME : dbName)
                    .schemaVersion(schemaVersion);

            if (null == migration){
                otherConfigBuilder.deleteRealmIfMigrationNeeded();
            }else {
                otherConfigBuilder.migration(migration);
            }

            RealmConfiguration otherConfig = otherConfigBuilder.build();
            Realm.setDefaultConfiguration(otherConfig);

            realmDefault = Realm.getDefaultInstance();
        }
    }

    private static void registerActivityListener() {
        if (null == application){
            throw new RuntimeException("UStorage is not initialized, please invoke initialize() method");
        }

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                activityCount ++;
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activityCount --;
                if (activityCount <= 0){
                    if (null != realm){
                        realm.close();
                        realm = null;
                    }
                }
            }
        });
    }
}
