package com.ameda.kevin.security_reactjs.function;

/*
*
@author ameda
@project security-reactjs
@
*
*/



@FunctionalInterface
public interface TriConsumer<T,U,V>{
    void accept(T t, U u, V v);
}
