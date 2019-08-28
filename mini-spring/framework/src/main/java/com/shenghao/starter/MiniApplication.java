package com.shenghao.starter;

import com.shenghao.beans.BeanFactory;
import com.shenghao.core.ClassScanner;
import com.shenghao.web.handler.HandlerManager;
import com.shenghao.web.server.TomcatServer;

import java.util.List;

public class MiniApplication {

    public static void run(Class<?> cls, String[] args){
        System.out.println("Hello mini-spring!");
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            //启动tomcat
            tomcatServer.startServer();
            //扫描启动类下所有的.class文件
            System.out.println(cls.getPackage().getName());
            List<Class<?>> classList = ClassScanner.scanClass(cls.getPackage().getName());
            //初始化bean工厂
            BeanFactory.initBean(classList);
            //解析所有.class文件，获得mappingHandler集合
            HandlerManager.resolveMappingHandler(classList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
