package com.imooc.miaosha.access;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * Created by 谭皓文 on 2018/12/2.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
    int second();
    int maxCount();
    boolean needLogin();
}
