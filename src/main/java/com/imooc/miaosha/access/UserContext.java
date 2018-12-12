package com.imooc.miaosha.access;

import com.imooc.miaosha.domain.MiaoshaUser;

/**
 * Created by 谭皓文 on 2018/12/2.
 */
public class UserContext {
    private static ThreadLocal<MiaoshaUser> threadLocal = new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user){
        threadLocal.set(user);
    }

    public static MiaoshaUser getUser(){
        return threadLocal.get();
    }
}
