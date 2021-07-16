package ltd.newbee.mall.newbeemallapi.api.mall;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.newbee.mall.newbeemallapi.api.mall.param.SaveOrderParam;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallOrderDetailVO;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallOrderListVO;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.newbeemallapi.common.NewBeeMallException;
import ltd.newbee.mall.newbeemallapi.common.ServiceResultEnum;
import ltd.newbee.mall.newbeemallapi.config.annotation.TokenToMallUser;
import ltd.newbee.mall.newbeemallapi.entity.MallUser;
import ltd.newbee.mall.newbeemallapi.entity.MallUserAddress;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallOrderService;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallShoppingCartService;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallUserAddressService;
import ltd.newbee.mall.newbeemallapi.util.PageQueryUtil;
import ltd.newbee.mall.newbeemallapi.util.PageResult;
import ltd.newbee.mall.newbeemallapi.util.Result;
import ltd.newbee.mall.newbeemallapi.util.ResultGenerator;

@RestController
@Api(value = "v1", tags = "7.新蜂商城订单操作相关接口")
public class NewBeeMallOrderAPI {
	
	@Resource
	NewBeeMallShoppingCartService newBeeMallShoppingCartService;
	
	@Resource
	NewBeeMallUserAddressService userService;
	
	@Resource
	NewBeeMallOrderService orderService;

    @PostMapping("/saveOrder")
    @ApiOperation(value = "生成订单接口", notes = "传参为地址id和待结算的购物项id数组")
    public Result<String> saveOrder(@RequestBody SaveOrderParam orderParam, @TokenToMallUser MallUser loginMallUser) {
    	int price = 0;
        if (orderParam == null || orderParam.getCartItemIds() == null || orderParam.getAddressId() == null) {
            NewBeeMallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        if (orderParam.getCartItemIds().length < 1) {
            NewBeeMallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        List<NewBeeMallShoppingCartItemVO> itemsForSale = newBeeMallShoppingCartService.getCartItemsForSettle(Arrays.asList(orderParam.getCartItemIds()),loginMallUser.getUserId());
        if (CollectionUtils.isEmpty(itemsForSale)) {
        	NewBeeMallException.fail("参数异常");
        }
        else {
        	//计算价格
        	for (NewBeeMallShoppingCartItemVO item: itemsForSale) {
        		price += item.getGoodsCount() * item.getSellingPrice();
        	}
        	if (price < 1) {
        		return ResultGenerator.genFailResult("异常价格");
        	}
        	
        	MallUserAddress userAddress = userService.getAddressDetailById(orderParam.getAddressId());
        	
        	if(!loginMallUser.getUserId().equals(userAddress.getUserId())) {
        		return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        	}
        	
        	//通过所有测试,准备将订单存入数据库
        	String result = orderService.saveOrder(loginMallUser, userAddress, itemsForSale);
        	Result res = ResultGenerator.genSuccessResult(result);
        	return res;
        }
        return ResultGenerator.genFailResult("生成订单失败");
    }
    
    @GetMapping("/order/{orderNo}")
    @ApiOperation(value = "订单详情接口", notes = "传参为订单号")
    public Result<NewBeeMallOrderDetailVO> orderDetailPage(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, 
    		@TokenToMallUser MallUser loginMallUser) {
    	NewBeeMallOrderDetailVO ans = orderService.getOrderDetailByOrderNo(orderNo,loginMallUser.getUserId());
    	System.out.println(ans.toString()+ " is the order returned");
    	return ResultGenerator.genSuccessResult(ans);
    	
    }
    
	
	@GetMapping("/order")	  
	@ApiOperation(value = "订单列表接口", notes = "传参为页码") 
	public Result<PageResult<List<NewBeeMallOrderListVO>>> orderList(@ApiParam(value = "页码") @RequestParam(required = false) Integer pageNumber,  
			  @ApiParam(value = "订单状态:0.待支付 1.待确认 2.待发货 3:已发货 4.交易成功") @RequestParam(required = false)
	          Integer status, @TokenToMallUser MallUser loginMallUser) {
		  
		  System.out.println("status is: "+ status);
		  Map map = new HashMap(8);
		  
		  if (pageNumber < 1 || pageNumber == null) {
			  pageNumber = 1;
		  }
		  map.put("pageNumber", pageNumber);
		  map.put("limit", 5);
		  map.put("orderStatus", status);
		  map.put("userId", loginMallUser.getUserId());
		  
		  System.out.println("map is: "+ map.toString());
		  
		  PageQueryUtil pageUtil = new PageQueryUtil(map);
		  System.out.println("pageUtil is: "+ pageUtil.toString());
		  return ResultGenerator.genSuccessResult(orderService.getMyOrders(pageUtil));
	  }
	  
	  @GetMapping("/paySuccess")
	  @ApiOperation(value  = "订单支付接口", notes = "传参为订单号,支付方式")
	  public Result paySuccess(@ApiParam(value = "订单号") @RequestParam(required = true) String orderNo, 
			  @ApiParam(value = "支付方式") @RequestParam(required = true) int payType) {
		  String result = orderService.paySuccess(orderNo, payType);
		  if (result.equals("success")) {
			  return ResultGenerator.genSuccessResult();
		  }
		  else {
			  return ResultGenerator.genFailResult(result);
		  }
	  }
	  
	  @PutMapping("/order/{orderNo}/cancel")
	  @ApiOperation(value = "订单取消接口", notes = "传参为订单号")
	  public Result cancelOrder(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToMallUser MallUser loginMallUser) {
		  String cancelOrder = orderService.cancelOrder(orderNo, loginMallUser.getUserId());
		  System.out.println(cancelOrder+" is the canceling result");
		  if (cancelOrder.equals("success")) {
			  return ResultGenerator.genSuccessResult();
		  }
		  return ResultGenerator.genFailResult(cancelOrder);
	  }
	 
}
