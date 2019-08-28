package com.shenghao.web.server;

import com.shenghao.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

/**
 * 内嵌Tomcat服务器
 */
public class TomcatServer {
    private Tomcat tomcat;//自带一个tomcat实例
    private String[] args;

    public TomcatServer(String[] args){
        this.args = args;
    }

    public void startServer() throws LifecycleException {
        tomcat = new Tomcat();
        tomcat.setPort(6699);//设置tomcat在操作系统上监听的端口号
        tomcat.start();//启动tomcat

        Context context = new StandardContext();//tomcat中的容器是分层级的，存放servlet的是context容器，先初始化一个
        context.setPath("");//设置context的路径为空
        context.addLifecycleListener(new Tomcat.FixContextListener());//注册一个监听器

        DispatcherServlet servlet = new DispatcherServlet();//创建DispatcherServlet对象，所有请求都先经过此处
        Tomcat.addServlet(context, "dispatcherServlet", servlet).setAsyncSupported(true);//把dispatcherServlet注册进Tomcat中，并设置为异步
        context.addServletMappingDecoded("/", "dispatcherServlet");//并为DispatcherServlet设置请求路径，设置为根目录意味着所有请求都会到这里

        tomcat.getHost().addChild(context);//把context注册到host中，host是tomcat中更高一级的容器

        Thread awaitThread = new Thread("tomcat_await_thread"){
            @Override
            public void run() {
                TomcatServer.this.tomcat.getServer().await();//设置tomcat线程一直等待，不然的话启动完就会关闭
            }
        };
        awaitThread.setDaemon(false);//设置为非守护线程
        awaitThread.start();//启动
    }


}
