package com.winning.unifystorage;

import com.winning.unifystorage_core.UStorage;
/**
 * @author yuzhijun
 * */
public class ApiServiceModule {
    private static ApiServiceModule mInstance;

    private ApiServiceModule(){

    }
    public static ApiServiceModule getInstance(){
        if (null == mInstance){
            synchronized (ApiServiceModule.class){
                if (null == mInstance){
                    mInstance = new ApiServiceModule();
                }
            }
        }
        return mInstance;
    }

    private UStorage provideUStorage(){
        return new UStorage.Builder()
                .setSchemaVersion(1)
                .build();
    }

    <T> T provideApiService(Class<T> apiDataBase){
        return provideUStorage().create(apiDataBase);
    }
}
