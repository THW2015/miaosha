package com.imooc.miaosha.controller;

import com.imooc.miaosha.access.AccessLimit;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.rabbitmq.MiaoshaMessage;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.result.Result;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean{

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MiaoshaService miaoshaService;

	@Autowired
	MQSender sender;

	private Map<Long,Boolean> localOverMap = new HashMap<Long, Boolean>();


    @RequestMapping(value = "/{pathToken}/do_miaosha",method = RequestMethod.POST)
	@ResponseBody
    public Result<Integer> list(Model model, MiaoshaUser user,
					   @RequestParam("goodsId")long goodsId,@PathVariable("pathToken") String pathToken) {
    	model.addAttribute("user", user);
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
		//验证请求
		boolean  isLegalReq = miaoshaService.checkPath(user.getId(),goodsId,pathToken);
		if(!isLegalReq){
			return Result.error(CodeMsg.REQUEST_ILLEGAL);
		}

		boolean over = localOverMap.get(goodsId);
		if(over){
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}

		//预减库存
		Long  stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock,"" + goodsId);
		if (stock < 0){
			localOverMap.put(goodsId,true);
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}

		//判断是否已经秒杀到了
    	MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
    	if(order != null) {
    		return Result.error(CodeMsg.REPEATE_MIAOSHA);
    	}

		//加入队列
		MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
		miaoshaMessage.setUser(user);
		miaoshaMessage.setGoodsId(goodsId);
		sender.send(miaoshaMessage);
		return Result.success(0);

//    	//判断库存
//    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//    	int stock = goods.getStockCount();
//    	if(stock <= 0) {
//    		return Result.error(CodeMsg.MIAO_SHA_OVER);
//    	}
//    	//判断是否已经秒杀到了
//    	MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//    	if(order != null) {
//    		return Result.error(CodeMsg.REPEATE_MIAOSHA);
//    	}
//    	//减库存 下订单 写入秒杀订单
//    	OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
//        return Result.success(orderInfo);
    }

	/**
	 * 系统初始化
	 *
     */
	@Override
	public void afterPropertiesSet() throws Exception {
		List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
		if(goodsVoList == null){
			return;
		}
		for(GoodsVo goodsVo : goodsVoList){
			redisService.set(GoodsKey.getMiaoshaGoodsStock,"" + goodsVo.getId(),goodsVo.getStockCount());
			localOverMap.put(goodsVo.getId(),false);
		}
	}

	/**
	 * 返回值-1为商品已销售为空 0为排队 其他为商品id
	 * @param model
	 * @param user
	 * @param goodsId
     * @return
     */

	@RequestMapping(value = "/result",method = RequestMethod.GET)
	@ResponseBody
	public Result<Long>  miaoshaResult(Model model,MiaoshaUser user,@RequestParam("goodsId") long goodsId){
		model.addAttribute("user",user);
		if(user == null){
			return Result.error(CodeMsg.SESSION_ERROR);
		}

		long result = miaoshaService.getMiaoshaResult(user.getId(),goodsId);
		return Result.success(result);
	}
	@AccessLimit(second=5, maxCount=5, needLogin=true)
	@RequestMapping(value = "/path",method = RequestMethod.GET)
	@ResponseBody
	public Result<String>  getMiaoshaPath(Model model,MiaoshaUser user,
										  @RequestParam("goodsId") long goodsId,
										  @RequestParam(value="verifyCode", defaultValue="0")int verifyCode){
		model.addAttribute("user",user);
		if(user == null){
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		boolean check = miaoshaService.checkVerifyCode(user,goodsId,verifyCode);
		if(!check){
			return Result.error(CodeMsg.VERIFYCODE_ERROR);
		}
		String path = miaoshaService.getMiaoshaPath(user.getId(),goodsId);
		return Result.success(path);
	}

	@RequestMapping(value = "/verifyCode",method = RequestMethod.GET)
	@ResponseBody
	public Result<String> getMiaoshaVerifyCod(HttpServletResponse response,MiaoshaUser user,
											  @RequestParam("goodsId") long goodsId){

		if(user == null){
			return Result.error(CodeMsg.SESSION_ERROR);
		}

		try {
			BufferedImage bufferedImage = miaoshaService.createVerifyCode(user,goodsId);
			OutputStream outputStream = response.getOutputStream();
			ImageIO.write(bufferedImage,"JPEG",outputStream);
			outputStream.flush();
			outputStream.close();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return Result.error(CodeMsg.MIAOSHA_FAIL);
		}


	}

}
