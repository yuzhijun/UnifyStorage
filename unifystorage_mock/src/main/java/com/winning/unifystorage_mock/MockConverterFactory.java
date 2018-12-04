package com.winning.unifystorage_mock;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class MockConverterFactory extends Converter.Factory {

    public static MockConverterFactory create() {
        return create(new Gson());
    }

    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static MockConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new MockConverterFactory(gson);
    }

    private final Gson gson;


    private MockConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new MockConverter<>(type);
    }

    class MockConverter<T> implements Converter<ResponseBody, T> {
        private final Type responseType;

        private MockConverter(Type type){
            this.responseType = type;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            String responseStr = value.toString();
            return gson.fromJson(responseStr, responseType);
        }
    }
}
