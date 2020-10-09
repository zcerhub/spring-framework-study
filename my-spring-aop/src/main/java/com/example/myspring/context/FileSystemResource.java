package com.example.myspring.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/*
* 系统文件形式的资源
* */
public class FileSystemResource implements Resource{
    //    文件资源对象
    private File file;

    public FileSystemResource(String fileName) {
        this.file = new File(fileName);
    }

    public FileSystemResource(File file) {
        this.file = file;
    }

    @Override
    public boolean exists() {
        return this.file==null?false:file.exists();
    }

    @Override
    public boolean isReadable() {
        return this.file==null?false:file.canRead();
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }
}
