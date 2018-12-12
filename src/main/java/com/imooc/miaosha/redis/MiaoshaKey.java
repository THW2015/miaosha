package com.imooc.miaosha.redis;

/**
 * Created by 谭皓文 on 2018/11/20.
 */
public class MiaoshaKey extends BasePrefix{
    private MiaoshaKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }
    public static MiaoshaKey isGoodsOver = new MiaoshaKey(0,"go");
    public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(0,"mspath");
    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(0,"msvc");
}
