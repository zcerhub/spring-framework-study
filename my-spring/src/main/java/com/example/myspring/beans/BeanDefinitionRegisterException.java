package com.example.myspring.beans;

public class BeanDefinitionRegisterException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 6056374114834139330L;

    public BeanDefinitionRegisterException(String mess) {
        super(mess);
    }

    public BeanDefinitionRegisterException(String mess, Throwable e) {
        super(mess, e);
    }
}
