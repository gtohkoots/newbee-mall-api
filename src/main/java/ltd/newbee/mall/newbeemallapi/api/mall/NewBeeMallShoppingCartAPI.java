package ltd.newbee.mall.newbeemallapi.api.mall;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.newbee.mall.newbeemallapi.api.mall.param.SaveCartItemParam;
import ltd.newbee.mall.newbeemallapi.api.mall.param.UpdateCartItemParam;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.newbeemallapi.common.NewBeeMallException;
import ltd.newbee.mall.newbeemallapi.config.annotation.TokenToMallUser;
import ltd.newbee.mall.newbeemallapi.entity.MallUser;
import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallShoppingCartItem;
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
	
	@DeleteMapping("/shop-cart/{newBeeMallShoppingCartItemId}")
	@ApiOperation(value = "删除购物车项目", notes = "")
	public Result deleteCartItemById(@PathVariable("newBeeMallShoppingCartItemId") Long newBeeMallShoppingCartItemId, 
			@TokenToMallUser MallUser loginMallUser) {
		
		NewBeeMallShoppingCartItem item = newBeeMallShoppingCartService.getMallItemById(newBeeMallShoppingCartItemId);
		
		if (!item.getUserId().equals(loginMallUser.getUserId())) {
			return ResultGenerator.genFailResult("物品返回错误");
		}
		Boolean result = newBeeMallShoppingCartService.deleteById(newBeeMallShoppingCartItemId);
		if (result) {
			return ResultGenerator.genSuccessResult();
		}
		return ResultGenerator.genFailResult("物品删除失败");
	}
	
	@PutMapping("/shop-cart")
	@ApiOperation(value = "修改购物项数据", notes = "传参为购物项id、数量")
	public Result updateNewBeeMallShoppingCartItem(@RequestBody UpdateCartItemParam updateCartItemParam,
			@TokenToMallUser MallUser loginMallUser) {
		String result = newBeeMallShoppingCartService.updateNewBeeMallShoppingCartItem(updateCartItemParam, loginMallUser.getUserId());
		if (result.equals("success")) {
			return ResultGenerator.genSuccessResult();
		}
		return ResultGenerator.genFailResult(result);
	}
	
	@GetMapping("/shop-cart/settle")
	@ApiOperation(value = "根据购物项id数组查询购物项明细", notes = "确认订单页面使用")
	public Result<List<NewBeeMallShoppingCartItemVO>> toSettle(Long[] cartItemId, @TokenToMallUser MallUser loginMallUser) {
		if (cartItemId.length < 1) {
			return ResultGenerator.genFailResult("传输数据失败");
		}
		
		List<NewBeeMallShoppingCartItemVO> itemsForSale = newBeeMallShoppingCartService.getCartItemsForSettle(Arrays.asList(cartItemId), loginMallUser.getUserId());
		if (CollectionUtils.isEmpty(itemsForSale))  {
			NewBeeMallException.fail("参数异常");
		}
		return ResultGenerator.genSuccessResult(itemsForSale);
		
		
	}

}
