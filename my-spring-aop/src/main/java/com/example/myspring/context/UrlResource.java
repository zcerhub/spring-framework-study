package com.example.myspring.context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/*
* URL形式的资源
* */
public class UrlResource implements Resource{
    //    URL资源对象
    private URL url;

    public UrlResource(URL url) {
        this.url = url;
    }

    public UrlResource(String url) throws MalformedURLException {
        this.url = new URL(url);
    }


    @Override
    public boolean exists() {
        return this.url!=null;
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
        if (this.url != null) {
            return this.url.openStream();
        }
        return null;
    }
}
