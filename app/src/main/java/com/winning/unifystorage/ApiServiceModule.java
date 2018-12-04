package com.winning.unifystorage;

import android.content.Context;

import com.winning.unifystorage_core.UStorage;
import com.winning.unifystorage_mock.MockCallAdapterFactory;
import com.winning.unifystorage_mock.MockConverterFactory;

import retrofit2.Retrofit;

/**
 * @author yuzhijun
 * */
public class ApiServiceModule {
    private volatile static ApiServiceModule mInstance;

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

    private Retrofit provideRetrofit(Context context){
        return new Retrofit.Builder()
                .addCallAdapterFactory(MockCallAdapterFactory.create(context))
                .addConverterFactory(MockConverterFactory.create())
                .baseUrl("http://test.com")
                .build();
    }

    <T> T provideRetrofitApiService(Context context,Class<T> api){
        return provideRetrofit(context).create(api);
    }

    <T> T provideApiService(Class<T> apiDataBase){
        return provideUStorage().create(apiDataBase);
    }
}
