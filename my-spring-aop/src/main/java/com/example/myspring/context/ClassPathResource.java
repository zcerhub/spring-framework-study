package com.example.myspring.context;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/*
* ClassPathResource 资源对象
* */
public class ClassPathResource implements Resource{

//    ClassPath资源需要的信息
    private String path;
    private Class<?> clazz;
    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this.path = path;
    }

    public ClassPathResource(String path, Class<?> clazz) {
        this.path = path;
        this.clazz = clazz;
    }

    public ClassPathResource(String path, Class<?> clazz, ClassLoader classLoader) {
        this.path = path;
        this.clazz = clazz;
        this.classLoader = classLoader;
    }

    @Override
    public boolean exists() {
        if (StringUtils.isNotBlank(path)) {
            if (this.clazz != null) {
                return this.clazz.getResource(path) != null;
            }
            if (this.classLoader != null) {
                return this.classLoader.getResource(path.startsWith("/") ? path.substring(1) : path) != null;
            }
            return this.getClass().getResource(path)!=null;
        }
        return false;
    }

    @Override
    public boolean isReadable() {
        return exists();
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (StringUtils.isNotBlank(path)) {
            if (this.clazz != null) {
                return this.clazz.getResourceAsStream(path);
            }
            if (this.classLoader != null) {
                return this.classLoader.getResourceAsStream(path.startsWith("/") ? path.substring(1) : path);
            }
            return this.getClass().getResourceAsStream(path);
        }
        return null;
    }
}
