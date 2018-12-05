package com.winning.unifystorage_core.Handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.winning.unifystorage_core.HandlerAdapter;
import com.winning.unifystorage_core.UStorage;
import com.winning.unifystorage_core.Utils.CommonUtil;
import com.winning.unifystorage_core.model.DbResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GetJsonHandler extends HandlerAdapter {
    private Gson mGson = new Gson();
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
            if (convert == Integer.class || convert == int.class){
                return new DbResult<>(UStorage.kv.decodeInt(this.key));
            }else if(convert == String.class){
                return new DbResult<>(UStorage.kv.decodeString(this.key));
            }else if (convert == Boolean.class || convert == boolean.class){
                return new DbResult<>(UStorage.kv.decodeBool(this.key));
            }else if (!convert.isArray() && Object.class.isAssignableFrom(convert)){
                String json = UStorage.kv.decodeString(this.key);
                return new DbResult<>(mGson.fromJson(json, convert));
            }else if(convert.isArray()){
                String json = UStorage.kv.decodeString(this.key);
                return new DbResult<>(getJsonList(json, convert.getComponentType()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private  <T extends Object> List<T> getJsonList(String jsonStr, Class<T> clazz) {
        try {
            if (!CommonUtil.isEmptyStr(jsonStr)) {
                List<T> results = new ArrayList<T>();
                java.lang.reflect.Type type = new TypeToken<ArrayList<JsonObject>>() {}.getType();
                ArrayList<JsonObject> jsonObjs = mGson.fromJson(jsonStr, type);
                for (JsonObject jsonObj : jsonObjs) {
                    results.add(mGson.fromJson(jsonObj, clazz));
                }
                return results;
            }else{
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }
}
