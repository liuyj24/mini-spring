package com.shenghao.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {
    public static List<Class<?>> scanClass(String packageName) throws IOException, ClassNotFoundException {
        //用于保存结果的容器
        List<Class<?>> classList = new ArrayList<>();
        //把文件名改为文件路径
        String path = packageName.replace(".", "/");
        //获取默认的类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //通过文件路径获取该文件夹下所有资源的URL
        Enumeration<URL> resources = classLoader.getResources(path);

        int index = 0;//测试

        while(resources.hasMoreElements()){
            System.out.println(++index);//测试

            //拿到下一个资源
            URL resource = resources.nextElement();
            //先判断是否是jar包，因为默认.class文件会被打包为jar包
            if(resource.getProtocol().contains("jar")){

                System.out.println("拿到一个jar包");//测试

                //把URL强转为jar包链接
                JarURLConnection jarURLConnection = (JarURLConnection)resource.openConnection();
                //根据jar包获取jar包的路径名
                String jarFilePath = jarURLConnection.getJarFile().getName();
                //把jar包下所有的类添加的保存结果的容器中
                classList.addAll(getClassFromJar(jarFilePath, path));
            }else{//也有可能不是jar文件，先放下
                //todo
            }
        }
        return classList;
    }

    /**
     * 获取jar包中所有路径符合的类文件
     * @param jarFilePath
     * @param path
     * @return
     */
    private static List<Class<?>> getClassFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();//保存结果的集合
        JarFile jarFile = new JarFile(jarFilePath);//创建对应jar包的句柄
        Enumeration<JarEntry> jarEntries = jarFile.entries();//拿到jar包中所有的文件
        while(jarEntries.hasMoreElements()){
            JarEntry jarEntry = jarEntries.nextElement();//拿到一个文件
            String entryName = jarEntry.getName();//拿到文件名，大概是这样：com/shenghao/test/Test.class
            if (entryName.startsWith(path) && entryName.endsWith(".class")){//判断是否是类文件
                String classFullName = entryName.replace("/", ".")
                        .substring(0, entryName.length() - 6);
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}
