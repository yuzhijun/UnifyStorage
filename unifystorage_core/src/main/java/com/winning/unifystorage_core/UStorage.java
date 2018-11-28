package com.winning.unifystorage_core;

import com.winning.unifystorage_core.Utils.CommonUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class UStorage {
    private final Map<Method, ServiceMethod<?>> serviceMethodCache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked") // Single-interface proxy creation guarded by parameter safety.
    public <T> T create(final Class<T> service) {
        CommonUtil.validateServiceInterface(service);

        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new InvocationHandler() {
                    private final Object[] emptyArgs = new Object[0];

                    @Override
                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                        // If the method is a method from Object then defer to normal invocation.
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }

                        return loadServiceMethod(method).invoke(args != null ? args : emptyArgs);
                    }
                });
    }

    ServiceMethod<?> loadServiceMethod(Method method) {
        ServiceMethod<?> result = serviceMethodCache.get(method);
        if (result != null) return result;

        synchronized (serviceMethodCache) {
            result = serviceMethodCache.get(method);
            if (result == null) {
                result = ServiceMethod.parseAnnotations(this, method);
                serviceMethodCache.put(method, result);
            }
        }
        return result;
    }
}
