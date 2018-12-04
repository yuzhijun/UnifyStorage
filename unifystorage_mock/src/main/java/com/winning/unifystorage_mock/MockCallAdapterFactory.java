package com.winning.unifystorage_mock;

import android.content.Context;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public class MockCallAdapterFactory extends CallAdapter.Factory {
    private final Context context;
    private final Gson gson;

    private MockCallAdapterFactory(final Context context, Gson gson){
        if (gson == null) throw new NullPointerException("gson == null");
        this.context = context;
        this.gson = gson;
    }

    public static MockCallAdapterFactory create(Context context) {
        return new MockCallAdapterFactory(context,new Gson());
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        if (getRawType(returnType) != Call.class) {
            return null;
        }

        final Type responseType = Utils.getCallResponseType(returnType);
        return new MockCallAdapter<>(responseType, annotations);
    }

    class MockCallAdapter<R> implements CallAdapter<R, Call>{
        private final Type responseType;
        private final Annotation[] annotations;

        private MockCallAdapter(final Type responseType,final Annotation[] annotations){
            this.responseType = responseType;
            this.annotations = annotations;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public Call adapt(Call<R> call) {
            if (null != annotations && annotations.length > 0){
                String url = "";
                for (Annotation annotation : annotations){
                    if (annotation instanceof GET){
                        url = ((GET) annotation).value();
                    }else if (annotation instanceof POST){
                        url = ((POST) annotation).value();
                    }else if (annotation instanceof PUT){
                        url = ((PUT) annotation).value();
                    }else if (annotation instanceof DELETE){
                        url = ((DELETE) annotation).value();
                    }
                }

                if (!"".equalsIgnoreCase(url)){
                    if (url.lastIndexOf("/") >= 0 && url.length() > url.lastIndexOf("/")){
                        String jsonName = url.substring(url.lastIndexOf("/") + 1);
                        String json = Utils.readAssetsFileString(context, jsonName + ".json");
                        if (!"".equals(json)){
                            return new MockCall<>(gson.fromJson(json, responseType));
                        }
                        return new MockCall<>("");
                    }
                }
            }
            return new MockCall<>("");
        }
    }
}
