package com.winning.unifystorage_core;

import com.winning.unifystorage_core.annotations.DB;
import com.winning.unifystorage_core.annotations.JSON;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

final class StorageFactory {
    ServiceMethod<?> serviceMethod;

    static StorageFactory parseAnnotations(UStorage storage, Method method) {
        return new Builder(storage, method).build();
    }

    private StorageFactory(Builder builder) {
        this.serviceMethod = builder.serviceMethod;
    }

    ServiceMethod getServiceMethod() {
        return serviceMethod;
    }

    static final class Builder {
        final UStorage storage;
        final Method method;
        final Annotation[] methodAnnotations;

        ServiceMethod<?> serviceMethod;


        Builder(UStorage storage, Method method) {
            this.storage = storage;
            this.method = method;
            this.methodAnnotations = method.getAnnotations();
        }

        StorageFactory build() {
            for (Annotation annotation : methodAnnotations) {
                parseMethodAnnotation(annotation);
            }

            return new StorageFactory(this);
        }

        private void parseMethodAnnotation(Annotation annotation){
            if (annotation instanceof DB){
                this.serviceMethod = DBServiceMethod.parseAnnotations(this.storage, this.method);
            }else if (annotation instanceof JSON){
                this.serviceMethod = JSONServiceMethod.parseAnnotations(this.storage, this.method);
            }
        }
    }
}
