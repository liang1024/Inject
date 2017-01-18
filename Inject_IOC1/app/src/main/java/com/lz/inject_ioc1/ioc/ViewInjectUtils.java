package com.lz.inject_ioc1.ioc;

import android.app.Activity;

import com.lz.inject_ioc1.ioc.annotation.ContentView;
import com.lz.inject_ioc1.ioc.annotation.ViewInject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/1/18.
 */

public class ViewInjectUtils {

    private static final String METHOD_SET_CONTENTVIEW = "setContentView";
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";

    public static void inject(Activity activity) {
        injectContentView(activity);
        injectViews(activity);
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

}
