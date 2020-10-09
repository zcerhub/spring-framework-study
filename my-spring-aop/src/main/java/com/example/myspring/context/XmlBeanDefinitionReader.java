package com.example.myspring.context;

import com.example.myspring.beans.BeanDefinitionRegistry;

/*
* xml配置文件Bean定义解读其
* */
public class XmlBeanDefinitionReader extends AbstractBeanDefintionReader {


    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public void loadBeanDefinition(Resource resource) {
        this.loadBeanDefinitions(new Resource[]{resource});
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) {
        if (resources != null && resources.length > 0) {
            for (Resource r : resources) {
                this.parseXml(r);
            }
        }
    }

    private void parseXml(Resource r) {
//  TODO     解析xm文档，获取bean定义，创建bean定义对象，注册到BeanDefinitionRegistry中。
    }
}
