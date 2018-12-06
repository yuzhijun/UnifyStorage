package com.winning.unifystorage_core.Utils;

import com.winning.unifystorage_core.exception.ErrorParamsException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.Sort;

import static com.winning.unifystorage_core.Utils.Constants.AND;
import static com.winning.unifystorage_core.Utils.Constants.AND_OR;
import static com.winning.unifystorage_core.Utils.Constants.EQUAL_TO;
import static com.winning.unifystorage_core.Utils.Constants.patternArray;

public final class FindConditionUtil {
    private static List<String> linkCondition = new ArrayList<>();
    private static List<String> setCondition = new ArrayList<>();
    private static Map<String,Object> setMap = new HashMap<>();


    public static RealmQuery<? extends RealmObject> otherFilter(RealmQuery<? extends RealmObject> query, String orderBy,int limit, String distinct ){
        if (!CommonUtil.isEmptyStr(orderBy)){
            if (orderBy.contains("desc")){
                query.sort(orderBy.substring(0, orderBy.indexOf("desc")).trim(), Sort.DESCENDING);
            }
            else if (orderBy.contains("asc")){
                query.sort(orderBy.substring(0, orderBy.indexOf("asc")).trim(), Sort.ASCENDING);
            }else {
                query.sort(orderBy);
            }
        }

        if (0 != limit){
            query.limit(limit);
        }

        if (!CommonUtil.isEmptyStr(distinct)){
            query.distinct(distinct);
        }

        return query;
    }


    public static RealmQuery<? extends RealmObject> setFilter(String set, String where, RealmQuery<? extends RealmObject> query, Object[] args, Type[] parameterTypes){
        linkCondition.clear();
        if (!CommonUtil.isEmptyStr(where)){
            Pattern linkPattern = Pattern.compile(AND_OR);
            Matcher linkMatcher = linkPattern.matcher(where);

            while (linkMatcher.find()){
                linkCondition.add(linkMatcher.group());
            }

            //说明有复合条件
            int whereLength;
            if (linkCondition.size() > 0){
                String[] whereArray = where.split(AND_OR);
                whereLength = whereArray.length;
                if (CommonUtil.isEmptyStr(set)){
                    if (args.length != whereArray.length || parameterTypes.length != whereArray.length){
                        throw new IllegalArgumentException("parameter size is not equal to ?");
                    }
                }

                for (int i = 0;i < whereArray.length;i ++){
                    String whereCondition = whereArray[i];
                    Object parameter = args[i];
                    Type parameterType = parameterTypes[i];
                    //构造查询条件
                    buildWhereCondition(query, whereCondition, parameter, parameterType);

                    if (linkCondition.size() - 1 >= i){
                        String condition = linkCondition.get(i);
                        if ("and".equalsIgnoreCase(condition)){
                            query.and();
                        }else {
                            query.or();
                        }
                    }

                    if (!CommonUtil.isEmptyStr(whereCondition) && whereCondition.contains(")")){
                        query.endGroup();
                    }
                }
            }else {//说明是单一条件
                buildWhereCondition(query, where, args.length == 0 ? null : args[0],parameterTypes.length == 0 ? null : parameterTypes[0]);
                whereLength = 1;
            }

            buildSetFilter(set, args,whereLength,parameterTypes);
        }

        return query;
    }

    private static void buildSetFilter(String set, Object[] args,int whereLength,Type[] parameterTypes) {
        setMap.clear();
        if (CommonUtil.isEmptyStr(set)){
            return;
        }

        setCondition.clear();
        Pattern setPattern = Pattern.compile(AND);
        Matcher setMatcher = setPattern.matcher(set);

        while (setMatcher.find()){
            setCondition.add(setMatcher.group());
        }


        if (args.length !=  whereLength + setCondition.size() + 1  || parameterTypes.length != whereLength + setCondition.size()+1){
            throw new IllegalArgumentException("parameter size is not equal to ?");
        }


        Object[] values = new Object[setCondition.size() + 1];
        for (int i = 0; i < linkCondition.size() + 1;i++){
            values[i] = args[linkCondition.size() + 1 + i];
        }
        if (setCondition.size() > 0){
            //复合条件
            String[] sets = set.split(AND);
            for (int i = 0;i < sets.length;i++){
                buildSetMap(sets[i].trim(),values[i]);
            }

        }else {
            //单一条件
            buildSetMap(set,values[0]);
        }
    }


    private static void buildSetMap(String set,Object value){
        Pattern pattern = Pattern.compile(EQUAL_TO);
        Matcher matcher = pattern.matcher(set);
        if (matcher.matches()){
            //匹配成功!
            String key = set.substring(0, set.indexOf("=")).trim();
            String field = key.substring(0, 1).toUpperCase() + key.substring(1);
            setMap.put(field,value);
        }
    }

    public static RealmQuery<? extends RealmObject> whereFilter(String where, RealmQuery<? extends RealmObject> query, Object[] args, Type[] parameterTypes){
        return  setFilter("", where, query, args, parameterTypes);
    }

    private static void buildWhereCondition(@Nonnull RealmQuery<? extends RealmObject> query, @Nonnull String whereCondition,
                                     Object parameter, Type parameterType) {

        if (CommonUtil.isEmptyStr(whereCondition)){
            return;
        }

        if (null == parameter || null == parameterType){
            throw new ErrorParamsException("parameter is null, please check your code");
        }

        if (whereCondition.contains("(")){
            query.beginGroup();
        }

        Class<?> rawType = CommonUtil.getRawType(parameterType);
        for (int j = 0; j < patternArray.length; j ++){
            Pattern pattern = Pattern.compile(patternArray[j][0]);
            Matcher matcher = pattern.matcher(whereCondition);
            if (matcher.find()){
                String[] array = whereCondition.split(patternArray[j][1]);
                String preStr = array[0].trim();
                if (preStr.contains("(")){
                    preStr = preStr.substring(preStr.lastIndexOf("(")+1);
                }

                if (preStr.contains(")")){
                    preStr = preStr.substring(0, preStr.lastIndexOf(")"));
                }
                if (parameterType == String.class){
                    if ("=".equalsIgnoreCase(patternArray[j][1])){
                        query.equalTo(preStr,(String) parameter);
                    }else if ("contains".equalsIgnoreCase(patternArray[j][1])){
                        query.contains(preStr,(String) parameter);
                    }else if("like".equalsIgnoreCase(patternArray[j][1])){
                        query.like(preStr,(String) parameter);
                    }else if ("notnull".equalsIgnoreCase(patternArray[j][1])){
                        query.isNotNull((String) parameter);
                    }else if ("null".equalsIgnoreCase(patternArray[j][1])){
                        query.isNull((String) parameter);
                    }
                }else if(parameterType == Integer.class || parameterType == int.class){
                    if ("=".equalsIgnoreCase(patternArray[j][1])){
                        query.equalTo(preStr,(int)parameter);
                    }else if (">".equalsIgnoreCase(patternArray[j][1])){
                        query.greaterThan(preStr,(int) parameter);
                    }else if ("<".equalsIgnoreCase(patternArray[j][1])){
                        query.lessThan(preStr,(int) parameter);
                    }else if (">=".equalsIgnoreCase(patternArray[j][1])){
                        query.greaterThanOrEqualTo(array[0].trim(),(int) parameter);
                    }else if ("<=".equalsIgnoreCase(patternArray[j][1])){
                        query.lessThanOrEqualTo(array[0].trim(),(int) parameter);
                    }
                }else if (parameterType == Date.class){
                    if ("=".equalsIgnoreCase(patternArray[j][1])){
                        query.equalTo(preStr,(Date) parameter);
                    }else if (">".equalsIgnoreCase(patternArray[j][1])){
                        query.greaterThan(preStr,(Date) parameter);
                    }else if ("<".equalsIgnoreCase(patternArray[j][1])){
                        query.lessThan(preStr,(Date) parameter);
                    }else if (">=".equalsIgnoreCase(patternArray[j][1])){
                        query.greaterThanOrEqualTo(preStr,(Date) parameter);
                    }else if ("<=".equalsIgnoreCase(patternArray[j][1])){
                        query.lessThanOrEqualTo(preStr,(Date) parameter);
                    }
                }else if (List.class.isAssignableFrom(rawType)){
                    Type[] componentType =  ((ParameterizedType) parameterType).getActualTypeArguments();
                    if ("in".equalsIgnoreCase(patternArray[j][1]) && componentType[0] == String.class){
                        query.in(preStr, (String[]) ((List)parameter).toArray(new String[0]));
                    }
                }
            }
        }
    }

    public static Map<String, Object> getSetMap() {
        return setMap;
    }


}
