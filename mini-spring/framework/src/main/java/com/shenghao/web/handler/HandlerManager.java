package com.shenghao.web.handler;

import com.shenghao.web.mvc.Controller;
import com.shenghao.web.mvc.RequestMapping;
import com.shenghao.web.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class HandlerManager {

    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    /**
     * 处理类文件集合，挑出MappingHandler
     * @param classList
     */
    public static void resolveMappingHandler(List<Class<?>> classList){
        System.out.println("测试有多少个.class文件：" + classList.size());
        for(Class<?> cls : classList){
            if(cls.isAnnotationPresent(Controller.class)){//MappingHandler会在controller里面
                parseHandlerFromController(cls);//继续从controller中分离出一个个MappingHandler
            }
        }
    }

    private static void parseHandlerFromController(Class<?> cls) {
        //先获取该controller中所有的方法
        Method[] methods = cls.getDeclaredMethods();
        //从中挑选出被RequestMapping注解的方法进行封装
        for(Method method : methods){
            if(!method.isAnnotationPresent(RequestMapping.class)){
                continue;
            }
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();//拿到RequestMapping定义的uri
            List<String> paramNameList = new ArrayList<>();//保存方法参数的集合
            for(Parameter parameter : method.getParameters()){
                if(parameter.isAnnotationPresent(RequestParam.class)){//把有被RequestParam注解的参数添加入集合
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);//把参数集合转为数组，用于反射
            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params);//反射生成MappingHandler
            mappingHandlerList.add(mappingHandler);//把mappingHandler装入集合中
        }
    }
}
