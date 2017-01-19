package com.lz.inject_ioc2_onclick.ioc;

import android.app.Activity;
import android.view.View;

import com.lz.inject_ioc2_onclick.ioc.annotation.ContentView;
import com.lz.inject_ioc2_onclick.ioc.annotation.EventBase;
import com.lz.inject_ioc2_onclick.ioc.annotation.ViewInject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Liangzai on 2017/1/19.
 */

public class ViewInjectUtils {

    private static final String METHOD_SET_CONTENTVIEW = "setContentView";
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";

    public static void inject(Activity activity) {
        injectContentView(activity);
        injectViews(activity);
        injectEvents(activity);
    }

    /**
     * 注解View
     *
     * @param activity
     */
    private static void injectViews(Activity activity) {

        Class<? extends Activity> clazz = activity.getClass();
        // 拿到这个类的声明的所有字段
        Field[] declaredFields = clazz.getDeclaredFields();
        //        遍历
        for (Field declaredField : declaredFields) {
            //   查询字段上是否存在ViewInject注解
            ViewInject viewInjectAnnotation = declaredField.getAnnotation(ViewInject.class);
            //  存在
            if (null != viewInjectAnnotation) {
                int viewId = viewInjectAnnotation.value();
                if (-1 != viewId) {
                    try {
                        // 反射执行
                        Method method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID, int.class);
                        Object resView = method.invoke(activity, viewId);
                        // 抑制Java语言访问时检查
                        declaredField.setAccessible(true);
                        declaredField.set(activity, resView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 注解布局文件
     *
     * @param activity
     */
    private static void injectContentView(Activity activity) {

        Class<? extends Activity> clazz = activity.getClass();

        //   查询类上是否存在ContentView注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);

        //  存在
        if (null != contentView) {

            int contentViewLayoutId = contentView.value();
            try {
                // 反射执行
                Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW, int.class);
                method.setAccessible(true);
                method.invoke(activity, contentViewLayoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注解事件
     *
     * @param activity
     */
    private static void injectEvents(Activity activity) {
        //拿到Class对象
        Class<? extends Activity> aClass = activity.getClass();
        //获取类中全部方法
        Method[] methods = aClass.getMethods();
        //遍历
        for (Method method : methods) {
            //拿到方法的全部注解
            Annotation[] annotations = method.getAnnotations();
            //遍历
            for (Annotation annotation : annotations) {
                // 注解的Class对象
                Class<? extends Annotation> annotationType = annotation.annotationType();
                // 获取EventBase
                EventBase eventBaseAnnotation = annotationType.getAnnotation(EventBase.class);
                // 是否为null
                if (null != eventBaseAnnotation) {
                    // 属性
                    String listenerSetter = eventBaseAnnotation.listenerSetter();
                    Class<?> listenerType = eventBaseAnnotation.listenerType();
                    String methodName = eventBaseAnnotation.methodName();

                    try {
                        //  获取声明的方法
                        Method valueMethod = annotationType.getDeclaredMethod("value");
                        // 拿到全部的ViewID
                        int[] viewIds = (int[]) valueMethod.invoke(annotation, (Object[]) null);

                        // 通过InvocationHandler设置代理
                        DynamicHandler dynamicHandler = new DynamicHandler(activity);
                        dynamicHandler.addMethod(methodName, method);
                        // 获取事件
                        Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class<?>[]{listenerType}, dynamicHandler);
                        // 遍历所以View，设置事件
                        for (int viewId : viewIds) {
                            View view = activity.findViewById(viewId);
                            //
                            Method setEventListenerMethod = view.getClass().getMethod(listenerSetter, listenerType);
                            setEventListenerMethod.invoke(view, listener);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}
