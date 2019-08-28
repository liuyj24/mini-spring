package com.shenghao.beans;

import com.shenghao.web.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化bean
 * 获得bean
 */
public class BeanFactory {

    //保存Bean实例的映射集合
    private static Map<Class<?>, Object> classToBean = new ConcurrentHashMap<>();

    /**
     * 根据class类型获取bean
     * @param cls
     * @return
     */
    public static Object getBean(Class<?> cls){
        return classToBean.get(cls);
    }

    /**
     * 初始化bean工厂
     * @param classList 需要一个.class文件集合
     * @throws Exception
     */
    public static void initBean(List<Class<?>> classList) throws Exception {
        //先创建一个.class文件集合的副本
        List<Class<?>> toCreate = new ArrayList<>(classList);
        //循环创建bean实例
        while(toCreate.size() != 0){
            int remainSize = toCreate.size();//记录开始时集合大小，如果一轮结束后大小没有变证明有相互依赖
            for(int i = 0; i < toCreate.size(); i++){//遍历创建bean，如果失败就先跳过，等下一轮再创建
                if(finishCreate(toCreate.get(i))){
                    toCreate.remove(i);
                }
            }
            if(toCreate.size() == remainSize){//有相互依赖的情况先抛出异常
                throw new Exception("cycle dependency!");
            }
        }
    }

    private static boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        //创建的bean实例仅包括Bean和Controller注释的类
        if(!cls.isAnnotationPresent(Bean.class) && !cls.isAnnotationPresent(Controller.class)){
            return true;
        }
        //先创建实例对象
        Object bean = cls.newInstance();
        //看看实例对象是否需要执行依赖注入，注入其他bean
        for(Field field : cls.getDeclaredFields()){
            if(field.isAnnotationPresent(AutoWired.class)){
                Class<?> fieldType = field.getType();
                Object reliantBean = BeanFactory.getBean(fieldType);
                if(reliantBean == null){//如果要注入的bean还未被创建就先跳过
                    return false;
                }
                field.setAccessible(true);
                field.set(bean, reliantBean);
            }
        }
        classToBean.put(cls, bean);
        return true;
    }
}
