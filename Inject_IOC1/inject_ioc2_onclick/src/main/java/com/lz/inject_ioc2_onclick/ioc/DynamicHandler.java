package com.lz.inject_ioc2_onclick.ioc;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by Liangzai on 2017/1/19.
 */

public class DynamicHandler implements InvocationHandler {

    private WeakReference<Object> handlerRef;

    private final HashMap<String, Method> mMethodHashMap = new HashMap<>(1);

    public DynamicHandler(Object handler) {
        this.handlerRef = new WeakReference<Object>(handler);
    }

    public void addMethod(String name, Method method) {
        mMethodHashMap.put(name, method);
    }

    public WeakReference<Object> getHandler() {
        return handlerRef;
    }

    public void setHandler(WeakReference<Object> handlerRef) {
        this.handlerRef = handlerRef;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object handler = handlerRef.get();
        if (null != handler) {
            String methodName = method.getName();
            method = mMethodHashMap.get(methodName);
            if (null != method) {
                return method.invoke(handler, args);
            }
        }
        return null;
    }
}
