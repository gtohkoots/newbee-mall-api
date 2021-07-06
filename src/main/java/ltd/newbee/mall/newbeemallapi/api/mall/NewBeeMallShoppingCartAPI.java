package ltd.newbee.mall.newbeemallapi.api.mall;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.newbee.mall.newbeemallapi.api.mall.param.SaveCartItemParam;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.newbeemallapi.config.annotation.TokenToMallUser;
import ltd.newbee.mall.newbeemallapi.entity.MallUser;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallShoppingCartService;
import ltd.newbee.mall.newbeemallapi.util.Result;
import ltd.newbee.mall.newbeemallapi.util.ResultGenerator;

@RestController
@Api(value = "v1", tags = "5.新蜂商城购物车相关接口")
public class NewBeeMallShoppingCartAPI {
	
	@Resource
	private NewBeeMallShoppingCartService newBeeMallShoppingCartService;
	
	@PostMapping("/shop-cart")
	@ApiOperation(value = "添加商品到购物车接口", notes = "传参为商品id、数量")
	public Result updateNewBeeMallShoppingCartItem(@RequestBody SaveCartItemParam saveCartItemParam,
            										@TokenToMallUser MallUser loginMallUser) {
		
		String saveResult = newBeeMallShoppingCartService.saveNewBeeMallCartItem(saveCartItemParam, loginMallUser.getUserId());
		
		if (saveResult.equals("success")) {
			return ResultGenerator.genSuccessResult(saveResult);
		}
		
		return ResultGenerator.genFailResult(saveResult);
		
	}
	
	@GetMapping("/shop-cart")
	@ApiOperation(value = "购物车列表(网页移动端不分页)", notes = "")
	public Result<List<NewBeeMallShoppingCartItemVO>> cartItemList(@TokenToMallUser MallUser loginMallUser) {
		List<NewBeeMallShoppingCartItemVO> result = newBeeMallShoppingCartService.getMyShoppingCartItems(loginMallUser.getUserId());
		System.out.println("returned List: " + result.toString());
		return ResultGenerator.genSuccessResult(result);
	}

}
