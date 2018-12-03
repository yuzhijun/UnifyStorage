package com.winning.unifystorage_core.Handler;

import com.alibaba.fastjson.JSON;
import com.winning.unifystorage_core.HandlerAdapter;
import com.winning.unifystorage_core.UStorage;
import com.winning.unifystorage_core.model.DbResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class GetJsonHandler extends HandlerAdapter {
    private Class<?> convert;
    private String key;

    private GetJsonHandler(Annotation[] annotations, String key, Class<?> convert){
        this.key = key;
        this.convert = convert;
    }

    public static HandlerAdapter parseAnnotations(Annotation[] annotations, String key, Class<?> convert){
        return new GetJsonHandler(annotations, key, convert);
    }

    @Override
    public  DbResult invoke(Object[] args, Type[] parameterTypes, Annotation[][] parameterAnnotationsArray) {
        try{
            if (convert == Integer.class){
                return new DbResult<>(UStorage.kv.decodeInt(this.key));
            }else if(convert == String.class){
                return new DbResult<>(UStorage.kv.decodeString(this.key));
            }else if (convert == Boolean.class){
                return new DbResult<>(UStorage.kv.decodeBool(this.key));
            }else if (Object.class.isAssignableFrom(convert)){
                String json = UStorage.kv.decodeString(this.key);
                return new DbResult<>(JSON.parseObject(json, this.convert));
            }else if(convert.isArray()){
                String json = UStorage.kv.decodeString(this.key);
                return new DbResult<>(JSON.parseArray(json, convert));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
