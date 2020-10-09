package com.example.myspring.v4.config;

import com.example.myspring.context.UrlResource;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;


/*

https://my.oschina.net/u/2474629/blog/1501101
* https://blog.csdn.net/liuxiao723846/article/details/47441547
*https://blog.csdn.net/weixin_37477523/category_9286887_2.html
* */
public class ClassLoaderResourceTest {

    @Test
    public void printResourcePath() {
        Properties ps = System.getProperties();
        System.out.println(ps.getProperty("sun.boot.class.path"));
        System.out.println(ps.getProperty("java.ext.dirs"));
        System.out.println(ps.getProperty("java.class.path"));
    }

    @Test
    public void testClassLoaderResource() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        printClassLoader(classLoader);
//        先查询bootstrap下的目录下的文件
        URL url = classLoader.getResource("bean.xml");
        System.out.println(url);
        url = classLoader.getResource("xx/bean.xml");
        System.out.println(url);
        url = classLoader.getResource("java/lang/Class.class");
        System.out.println(url);
        url = classLoader.getResource("Class.class");
        System.out.println(url);
    }


    @Test
    public void testGetResources() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Enumeration<URL> resources = classLoader.getResources("bean.xml");
        while (resources.hasMoreElements()) {
            System.out.println(resources.nextElement());
        }
    }

    @Test
    public void testSystemResources() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Enumeration<URL> resources = ClassLoader.getSystemResources("bean.xml");
        while (resources.hasMoreElements()) {
            System.out.println(resources.nextElement());
        }
    }

    @Test
    public void testGetSystemResource() {
        URL url = ClassLoader.getSystemResource("bean.xml");
        System.out.println(url);
    }

    @Test
    public void testClassGetSystemResource() {
        URL url = this.getClass().getResource("/bean.xml");
        System.out.println(url);
    }

    private void printClassLoader(ClassLoader classLoader) {
        do {
            System.out.println(classLoader);
            classLoader = classLoader.getParent();
        } while (classLoader != null);
    }

}
