package com.shenghao.web.servlet;

import com.shenghao.web.handler.HandlerManager;
import com.shenghao.web.handler.MappingHandler;

import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class DispatcherServlet implements Servlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
//        servletResponse.getWriter().println("test");
System.out.println("测试有多少个mappingHandler：" + HandlerManager.mappingHandlerList.size());
        for(MappingHandler mappingHandler : HandlerManager.mappingHandlerList){
            try{
                if (mappingHandler.handle(servletRequest, servletResponse)){
                    return;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
