package com.example.myspring.context;

import com.example.myspring.beans.BeanDefinitionRegistry;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/*
* xml配置文件的ApplicationContext实现
* */
public class XmlApplicationContext extends  AbstractApplicationContext{

    private List<Resource> resources;

    private BeanDefinitionReader reader;

    public XmlApplicationContext(String... location) throws IOException {
        super();
        load(location);
//        资源解析成BeanDefinition，外派给BeanDefinitionReader接口来实现
        this.reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) this.beanFactory);
        Resource[] resourceArray = new Resource[resources.size()];
        resources.toArray(resourceArray);
//        将解读后的BeanDefinition装载到BeanFactory中
        reader.loadBeanDefinitions(resourceArray);
    }

    /*
    * 根据用户指定的配置文件位置，加载资源信息
    * */
    private void load(String... location) throws IOException {
        if (resources == null) {
            resources = new ArrayList<>();
        }
//        完成加载，创建好Resource
        if (location != null && location.length > 0) {
            for (String lo : location) {
                Resource re = this.getResource(lo);
                if (re != null) {
                    this.resources.add(re);
                }
            }
        }
    }

    /*
    * 资源加载接口的实现
    * */
    @Override
    public Resource getResource(String location) throws IOException {
        if (StringUtils.isNotBlank(location)) {
//            根据字符串前缀进行区分，class、系统文件、URL三种资源的加载
            if (location.startsWith(Resource.CLASS_PATH_PREFIX)) {
                return new ClassPathResource(location.substring(Resource.CLASS_PATH_PREFIX.length()));
            } else if (location.startsWith(Resource.FILE_SYSTEM_PREFIX)) {
                return new FileSystemResource(location.substring(Resource.FILE_SYSTEM_PREFIX.length()));
            }else{
                return new UrlResource(location);
            }
        }
        return null;
    }
}
