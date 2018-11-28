package com.winning.unifystorage_core;

import java.lang.reflect.Method;

abstract class ServiceMethod<T> {
    static <T> ServiceMethod<T> parseAnnotations(UStorage storage, Method method) {
        StorageFactory storageFactory = StorageFactory.parseAnnotations(storage, method);

        return storageFactory.getServiceMethod();
    }

    abstract T invoke(Object[] args);
}
