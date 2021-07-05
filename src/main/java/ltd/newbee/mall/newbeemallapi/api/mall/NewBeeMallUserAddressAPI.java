package ltd.newbee.mall.newbeemallapi.api.mall;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.newbee.mall.newbeemallapi.api.mall.param.SaveMallUserAddressParam;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallUserAddressVO;
import ltd.newbee.mall.newbeemallapi.config.annotation.TokenToMallUser;
import ltd.newbee.mall.newbeemallapi.entity.MallUser;
import ltd.newbee.mall.newbeemallapi.entity.MallUserAddress;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallUserAddressService;
import ltd.newbee.mall.newbeemallapi.util.BeanUtil;
import ltd.newbee.mall.newbeemallapi.util.Result;
import ltd.newbee.mall.newbeemallapi.util.ResultGenerator;

@RestController
@Api(value = "v1", tags = "6.新蜂商城个人地址相关接口")
public class NewBeeMallUserAddressAPI {
	
	@Resource
	NewBeeMallUserAddressService addressService;
	
	@GetMapping("/address")
	@ApiOperation(value = "我的收货地址列表", notes = "")
	public Result<List<NewBeeMallUserAddressVO>> addressList(@TokenToMallUser MallUser loginMallUser) {
		List<NewBeeMallUserAddressVO> addressList = addressService.getMyAddresses(loginMallUser.getUserId());
		System.out.println("The addressList is" + addressList.toString());
		if (addressList == null) {
			return ResultGenerator.genFailResult("返回失败!");
		}
		return ResultGenerator.genSuccessResult(addressList);
	}
	
	@PostMapping("/address")
	@ApiOperation(value = "添加地址", notes = "")
	public Result<Boolean> saveUserAddress(@RequestBody SaveMallUserAddressParam saveMallUserAddressParam, @TokenToMallUser MallUser loginMallUser) {
		System.out.println("object received, " + saveMallUserAddressParam.toString());
		MallUserAddress userAddress = new MallUserAddress();
		BeanUtil.copyProperties(saveMallUserAddressParam, userAddress);
		userAddress.setUserId(loginMallUser.getUserId());
		Boolean result = addressService.saveUserAddress(userAddress);
		System.out.println("The result is" + result);
		if (!result) {
			return ResultGenerator.genFailResult("添加地址失败!");
		}
		return ResultGenerator.genSuccessResult();
	}

}
