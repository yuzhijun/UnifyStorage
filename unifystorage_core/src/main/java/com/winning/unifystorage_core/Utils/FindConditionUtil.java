package com.winning.unifystorage_core.Utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import io.realm.RealmObject;
import io.realm.RealmQuery;

import static com.winning.unifystorage_core.Utils.Constants.AND_OR;
import static com.winning.unifystorage_core.Utils.Constants.patternArray;

public class FindConditionUtil {
    private static List<String> linkCondition = new ArrayList<>();

    public static RealmQuery<? extends RealmObject> otherFilter(RealmQuery<? extends RealmObject> query, String orderBy,int limit, String distinct ){
        if (!CommonUtil.isEmptyStr(orderBy)){
            query.sort(orderBy);
        }

        if (0 != limit){
            query.limit(limit);
        }

        if (!CommonUtil.isEmptyStr(distinct)){
            query.distinct(distinct);
        }

        return query;
    }

    public static RealmQuery<? extends RealmObject> whereFilter(String where, RealmQuery<? extends RealmObject> query, Object[] args, Type[] parameterTypes){
        linkCondition.clear();
        if (!CommonUtil.isEmptyStr(where)){
            Pattern linkPattern = Pattern.compile(AND_OR);
            Matcher linkMatcher = linkPattern.matcher(where);

            while (linkMatcher.find()){
                linkCondition.add(linkMatcher.group());
            }

            //说明有复合条件
            if (linkCondition.size() > 0){
                String[] whereArray = where.split(AND_OR);
                if (args.length != whereArray.length || parameterTypes.length != whereArray.length){
                    throw new IllegalArgumentException("parameter size is not equal to ?");
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
                }
            }else {//说明是单一条件
                buildWhereCondition(query, where, args.length == 0 ? null : args[0],parameterTypes.length == 0 ? null : parameterTypes[0]);
            }
        }

        return query;
    }

    private static void buildWhereCondition(@Nonnull RealmQuery<? extends RealmObject> query, @Nonnull String whereCondition,
                                     Object parameter, Type parameterType) {

        if (CommonUtil.isEmptyStr(whereCondition)){
            return;
        }

        if (null == parameter || null == parameterType){
            return;
        }
        Class<?> rawType = CommonUtil.getRawType(parameterType);
        for (int j = 0; j < patternArray.length; j ++){
            Pattern pattern = Pattern.compile(patternArray[j][0]);
            Matcher matcher = pattern.matcher(whereCondition);
            if (matcher.matches()){
                String[] array = whereCondition.split(patternArray[j][1]);
                if (parameterType == String.class){
                    if ("=".equalsIgnoreCase(patternArray[j][1])){
                        query.equalTo(array[0].trim(),(String) parameter);
                    }else if ("contains".equalsIgnoreCase(patternArray[j][1])){
                        query.contains(array[0].trim(),(String) parameter);
                    }else if("like".equalsIgnoreCase(patternArray[j][1])){
                        query.like(array[0].trim(),(String) parameter);
                    }else if ("notnull".equalsIgnoreCase(patternArray[j][1])){
                        query.isNotNull((String) parameter);
                    }else if ("null".equalsIgnoreCase(patternArray[j][1])){
                        query.isNull((String) parameter);
                    }
                }else if(parameterType == Integer.class){
                    if ("=".equalsIgnoreCase(patternArray[j][1])){
                        query.equalTo(array[0].trim(),(int)parameter);
                    }else if (">".equalsIgnoreCase(patternArray[j][1])){
                        query.greaterThan(array[0].trim(),(int) parameter);
                    }else if ("<".equalsIgnoreCase(patternArray[j][1])){
                        query.lessThan(array[0].trim(),(int) parameter);
                    }else if (">=".equalsIgnoreCase(patternArray[j][1])){
                        query.greaterThanOrEqualTo(array[0].trim(),(int) parameter);
                    }else if ("<=".equalsIgnoreCase(patternArray[j][1])){
                        query.lessThanOrEqualTo(array[0].trim(),(int) parameter);
                    }
                }else if (parameterType == Date.class){
                    if ("=".equalsIgnoreCase(patternArray[j][1])){
                        query.equalTo(array[0].trim(),(Date) parameter);
                    }else if (">".equalsIgnoreCase(patternArray[j][1])){
                        query.greaterThan(array[0].trim(),(Date) parameter);
                    }else if ("<".equalsIgnoreCase(patternArray[j][1])){
                        query.lessThan(array[0].trim(),(Date) parameter);
                    }else if (">=".equalsIgnoreCase(patternArray[j][1])){
                        query.greaterThanOrEqualTo(array[0].trim(),(Date) parameter);
                    }else if ("<=".equalsIgnoreCase(patternArray[j][1])){
                        query.lessThanOrEqualTo(array[0].trim(),(Date) parameter);
                    }
                }else if (List.class.isAssignableFrom(rawType)){
                    Type[] componentType =  ((ParameterizedType) parameterType).getActualTypeArguments();
                    if ("in".equalsIgnoreCase(patternArray[j][1]) && componentType[0] == String.class){
                        query.in(array[0].trim(), (String[]) ((List)parameter).toArray(new String[0]));
                    }
                }
            }
        }
    }
}
