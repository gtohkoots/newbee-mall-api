package ltd.newbee.mall.newbeemallapi.api.mall;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.newbee.mall.newbeemallapi.api.mall.param.SaveMallUserAddressParam;
import ltd.newbee.mall.newbeemallapi.api.mall.param.UpdateMallUserAddressParam;
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
	
	@PutMapping("/address")
	@ApiOperation(value = "修改地址", notes = "")
	public Result updateAddressByParams(@TokenToMallUser MallUser loginMallUser, @RequestBody UpdateMallUserAddressParam updateParam) {
		MallUserAddress userAddress = new MallUserAddress();
		BeanUtil.copyProperties(updateParam, userAddress);
		userAddress.setUserId(loginMallUser.getUserId());
		Boolean result = addressService.updateAddressByParams(userAddress);
		if (result) {
			return ResultGenerator.genSuccessResult();
		}
		return ResultGenerator.genFailResult("更新地址失败");	
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
	
	@GetMapping("/address/{addressId}")
	@ApiOperation(value = "获取收货地址详情", notes = "传参为地址id")
	public Result<NewBeeMallUserAddressVO> getAddressDetailById(@PathVariable Long addressId, @TokenToMallUser MallUser loginMallUser) {
		MallUserAddress MallUserAddress = addressService.getAddressDetailById(addressId);
		NewBeeMallUserAddressVO userAddressVO = new NewBeeMallUserAddressVO();
		BeanUtil.copyProperties(MallUserAddress, userAddressVO);
		if (userAddressVO.getUserId() != loginMallUser.getUserId()) {
			return ResultGenerator.genFailResult("在您的账户中并没保存该地址");
		}
		return ResultGenerator.genSuccessResult(userAddressVO);
		
	}
	
	@GetMapping("/address/default")
	public Result<MallUserAddress> getDefaultAddress(@TokenToMallUser MallUser loginMallUser) {
		MallUserAddress addressVO = addressService.getDefaultAddressById(loginMallUser.getUserId());
		return ResultGenerator.genSuccessResult(addressVO);
	}
	
    @DeleteMapping("/address/{addressId}")
    @ApiOperation(value = "删除收货地址", notes = "传参为地址id")
    public Result deleteAddressById(@PathVariable Long addressId) {
    	Boolean result = addressService.deleteById(addressId);
    	if(result) {
    		return ResultGenerator.genSuccessResult();
    	}
    	return ResultGenerator.genFailResult("删除失败");
    }

}
