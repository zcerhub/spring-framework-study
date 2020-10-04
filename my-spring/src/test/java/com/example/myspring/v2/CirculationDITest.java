package com.example.myspring.v2;

import com.example.myspring.beans.BeanDefinitionRegisterException;
import com.example.myspring.beans.BeanReference;
import com.example.myspring.beans.GenericBeanDefinition;
import com.example.myspring.beans.PreBuildBeanFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CirculationDITest {

    static PreBuildBeanFactory bf = new PreBuildBeanFactory();

    @Test
    public void testCirculation() throws Exception {
        GenericBeanDefinition niulangBd = new GenericBeanDefinition();
        niulangBd.setBeanClass(Lad.class);
        List<Object> args = new ArrayList<>();
        args.add("niulang");
        args.add(new BeanReference("zhinv"));
        niulangBd.setConstructorArgumentValues(args);
        bf.registryBeanDefinition("nl", niulangBd);

        GenericBeanDefinition zhinvBd = new GenericBeanDefinition();
        zhinvBd.setBeanClass(MagicGirl.class);
        List<Object> argsZN = new ArrayList<>();
        argsZN.add("zhinv");
        argsZN.add(new BeanReference("nl"));
        zhinvBd.setConstructorArgumentValues(argsZN);
        bf.registryBeanDefinition("zhinv",zhinvBd);

        bf.preInstantiateSingletons();
    }

}
