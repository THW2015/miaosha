package com.imooc.miaosha.redis;

/**
 * Created by 谭皓文 on 2018/11/11.
 */
public class GoodsKey extends BasePrefix{

    private static final int GL_TIME = 60;
    private GoodsKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(GL_TIME,"gl");
    public static GoodsKey getGoodsDeatail = new GoodsKey(GL_TIME,"gd");
    public static GoodsKey getMiaoshaGoodsStock = new GoodsKey(0,"gs");
}
