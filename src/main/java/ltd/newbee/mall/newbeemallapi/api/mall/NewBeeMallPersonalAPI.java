package ltd.newbee.mall.newbeemallapi.api.mall;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.newbee.mall.newbeemallapi.api.mall.param.MallUserLoginParam;
import ltd.newbee.mall.newbeemallapi.api.mall.param.MallUserRegisterParam;
import ltd.newbee.mall.newbeemallapi.api.mall.param.MallUserUpdateParam;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallUserVO;
import ltd.newbee.mall.newbeemallapi.common.Constants;
import ltd.newbee.mall.newbeemallapi.config.annotation.TokenToMallUser;
import ltd.newbee.mall.newbeemallapi.entity.MallUser;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallUserService;
import ltd.newbee.mall.newbeemallapi.util.BeanUtil;
import ltd.newbee.mall.newbeemallapi.util.NumberUtil;
import ltd.newbee.mall.newbeemallapi.util.Result;
import ltd.newbee.mall.newbeemallapi.util.ResultGenerator;

@RestController
@Api(value = "v1", tags = "2.新蜂商城用户操作相关接口")
public class NewBeeMallPersonalAPI {
	
	@Resource
	NewBeeMallUserService newBeeMallUserService;
	
	private static final Logger logger = LoggerFactory.getLogger(NewBeeMallPersonalAPI.class);
	
	@GetMapping("")
	@ApiOperation(value = "测试")
	public String test() {
		return "Server Testing";
	}
	
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ApiOperation(value = "登录接口", notes = "返回token")
	public Result<String> login(@RequestBody MallUserLoginParam mallUserLoginParam) {
        if (!NumberUtil.isPhone(mallUserLoginParam.getLoginName())) {
            return ResultGenerator.genFailResult("请输入手机号!");
        }	
        String loginResult = newBeeMallUserService.login(mallUserLoginParam.getLoginName(),mallUserLoginParam.getPasswordMd5());
        
        logger.info("login api,loginName={},loginResult={}", mallUserLoginParam.getLoginName(), loginResult);
        
        if(loginResult.length() == Constants.TOKEN_LENGTH) {
        	Result result = ResultGenerator.genSuccessResult(loginResult);
        	return result;    	
        }
        return ResultGenerator.genFailResult(loginResult);      	
	}
	
	@RequestMapping(value = "/user/logout", method = RequestMethod.POST)
	@ApiOperation(value = "登出接口", notes = "清除token")
	public Result logout(@TokenToMallUser MallUser loginMallUser) {
		if (newBeeMallUserService.deletTokeneByPrimaryKey(loginMallUser.getUserId())) {
			logger.info("logout api,loginMallUser={}", loginMallUser.getUserId());
			return ResultGenerator.genSuccessResult();
		}
		return ResultGenerator.genFailResult("登出失败");
	}
	
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ApiOperation(value = "注册接口")
	public Result register(@RequestBody MallUserRegisterParam mallUserRegisterParam) {
		if (!NumberUtil.isPhone(mallUserRegisterParam.getLoginName())) {
			return ResultGenerator.genFailResult("请用手机号注册!");
		}
		String registerResult = newBeeMallUserService.register(mallUserRegisterParam.getLoginName(), mallUserRegisterParam.getPassword());
		
		logger.info("register api,loginName={},loginResult={}", mallUserRegisterParam.getLoginName(), registerResult);
		if(registerResult.equals("注册成功!")) {
			return ResultGenerator.genSuccessResult();
		}
		return ResultGenerator.genFailResult(registerResult);
	}
	
	@RequestMapping(value="/user/info", method = RequestMethod.PUT)
	@ApiOperation(value = "更新用户信息")
	public Result updateUserInfo(@RequestBody @ApiParam("用户信息") MallUserUpdateParam mallUserUpdateParam, @TokenToMallUser MallUser loginMallUser) {
		Boolean result = newBeeMallUserService.updateUserInfo(mallUserUpdateParam, loginMallUser.getUserId());
		if (result) {
			return ResultGenerator.genSuccessResult();
		}
		return ResultGenerator.genFailResult("更新出现问题!");
	}
	
	@RequestMapping(value = "/user/info", method= RequestMethod.GET)
	@ApiOperation(value = "获取用户信息", notes = "")
	public Result<NewBeeMallUserVO> getUserDetail(@TokenToMallUser MallUser loginMallUser) {
		NewBeeMallUserVO newbeeUser = new NewBeeMallUserVO();
		BeanUtil.copyProperties(loginMallUser, newbeeUser);
		System.out.println(newbeeUser.toString());
		return ResultGenerator.genSuccessResult(newbeeUser);
	}
}
