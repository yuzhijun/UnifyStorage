package com.winning.unifystorage_core.Handler;

import com.google.gson.Gson;
import com.winning.unifystorage_core.HandlerAdapter;
import com.winning.unifystorage_core.UStorage;
import com.winning.unifystorage_core.Utils.CommonUtil;
import com.winning.unifystorage_core.exception.ErrorParamsException;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class JsonHandler extends HandlerAdapter {
    private Gson mGson = new Gson();
    private String key;
    private Class<?> convert;

    private JsonHandler(Annotation[] annotations, String key, Class<?> convert){
        this.key = key;
        this.convert = convert;
    }

    public static HandlerAdapter parseAnnotations(Annotation[] annotations,final String key, Class<?> convert){
        return new JsonHandler(annotations, key, convert);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Boolean invoke(Object[] args, Type[] parameterTypes, Annotation[][] parameterAnnotationsArray) {
        if (args.length != 1){
            throw new ErrorParamsException("key-value method must be one parameter");
        }

        try{
            Type parameterType = parameterTypes[0];
            Class<?> rawType = CommonUtil.getRawType(parameterType);
            Object arg = args[0];

            if (parameterType == Integer.class){
                UStorage.kv.encode(this.key, (int)arg);
            }else if(parameterType == Boolean.class){
                UStorage.kv.encode(this.key, (boolean) arg);
            }else if(parameterType == String.class){
                UStorage.kv.encode(this.key, (String) arg);
            }else if(!(parameterType instanceof ParameterizedType) &&  Object.class.isAssignableFrom(rawType)){
                String json = mGson.toJson(arg, parameterType);
                UStorage.kv.encode(this.key, json);
            }else if(List.class.isAssignableFrom(rawType)){
                String json = mGson.toJson(arg, parameterType);
                UStorage.kv.encode(this.key, json);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}
