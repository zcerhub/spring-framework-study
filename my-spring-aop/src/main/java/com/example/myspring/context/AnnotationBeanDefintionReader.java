package com.example.myspring.context;

import com.example.myspring.beans.BeanDefinitionRegistry;
import com.example.myspring.beans.GenericBeanDefinition;
import com.example.myspring.context.config.annotation.AutoWired;
import com.example.myspring.context.config.annotation.Component;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

/*
* BeanDefinitionReader抽象实现
* 解读出来后BeanDefinition最终需要注册到BeanFactory中，通过BeanDefinitionRegistry接口来完成
* */
public class AnnotationBeanDefintionReader extends AbstractBeanDefintionReader {

    public AnnotationBeanDefintionReader(BeanDefinitionRegistry registry) {
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
                retriveAndRegistBeanDefinition(r);
            }
        }
    }

    private void retriveAndRegistBeanDefinition(Resource resource) {
        if (resource != null && resource.getFile() != null) {
            String className = getClassNameFromFile(resource.getFile());
            try {
                Class<?> clazz = Class.forName(className);
                Component component = clazz.getAnnotation(Component.class);
                if (component != null) {
//                    标注了@Component注解
                    GenericBeanDefinition bd = new GenericBeanDefinition();
                    bd.setBeanClass(clazz);
                    bd.setScope(component.scope());
                    bd.setFactoryBeanName(component.factoryBeanName());
                    bd.setFactoryMethodName(component.factoryMethodName());
                    bd.setInitMethod(component.initMethodName());
                    bd.setDestroyMethod(component.destoryMethodName());

//                    获得所有构造方法，在构造方法上找@Autowired注解，如有，将这个构造方法set到bd；
                    this.handleConstructor(clazz, bd);

//                    处理工厂方法参数依赖
                    if (StringUtils.isNotBlank(bd.getFactoryMethodName())) {
                        this.handlerFactoryMethodArgs(clazz, bd);
                    }
//                    处理属性依赖
                    this.handlePropertyDir(clazz, bd);

                    String beanName = "".equals(component.value()) ? component.name() : null;
                    if (StringUtils.isBlank(beanName)) {
//                        TODO 应用名称生成规则生成beanName
//                        默认驼峰命名法
                        beanName = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, clazz.getSimpleName());
                    }
//                    注册bean定义
                    this.registry.registryBeanDefinition(beanName, bd);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void handlePropertyDir(Class<?> clazz, GenericBeanDefinition bd) {
        //TODO
    }

    private void handlerFactoryMethodArgs(Class<?> clazz, GenericBeanDefinition bd) {
        //TODO
    }

    private void handleConstructor(Class<?> clazz, GenericBeanDefinition bd) {
//        获得所有构造方法，在构造方法上找@Autowire注解，如有，将这个构造方法set到bd；
        Constructor<?>[] cs = clazz.getConstructors();
        if (cs != null && cs.length > 0) {
            for (Constructor<?> c : cs) {
                if (c.getAnnotation(AutoWired.class) != null) {
                    bd.setConstructor(c);
                    Parameter[] ps = c.getParameters();
//                    TODO 遍历获取参数上的注解，及创建参数依赖
                    break;
                }
            }
        }
    }

    private int classPathAbsLength=AnnotationBeanDefintionReader.class.getResource("/").toString().length();

    private String getClassNameFromFile(File file) {
        String absPath = file.getAbsolutePath();
        String name = absPath.substring(classPathAbsLength + 1, absPath.indexOf(','));
        return StringUtils.replace(name, File.separator, ".");
    }

}
