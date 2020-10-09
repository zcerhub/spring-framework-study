package com.example.myspring.beans;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/*
* 提前构建单例bean的工程
* */
public class PreBuildBeanFactory  extends DefaultBeanFactory{

    private List<String> beanNames = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void registryBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionRegisterException {
        super.registryBeanDefinition(beanName, beanDefinition);
        synchronized (beanNames) {
            beanNames.add(beanName);
        }
    }

    public void preInstantiateSingletons() {
        synchronized (beanNames) {
            beanNames.forEach(beanName->{
                BeanDefinition bd = this.getBeanDefinition(beanName);
                if (bd.isSingleton()) {
                    try {
                        this.getBean(beanName);
                        if (logger.isDebugEnabled()) {
                            logger.debug("preInstantiateSingletons:name="+beanName+" "+bd);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
