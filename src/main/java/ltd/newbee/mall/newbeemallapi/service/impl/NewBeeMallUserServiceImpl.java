package ltd.newbee.mall.newbeemallapi.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ltd.newbee.mall.newbeemallapi.api.mall.param.MallUserUpdateParam;
import ltd.newbee.mall.newbeemallapi.common.NewBeeMallException;
import ltd.newbee.mall.newbeemallapi.dao.MallUserMapper;
import ltd.newbee.mall.newbeemallapi.dao.NewBeeMallUserTokenMapper;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallUserService;
import ltd.newbee.mall.newbeemallapi.util.NumberUtil;
import ltd.newbee.mall.newbeemallapi.util.SystemUtil;
import ltd.newbee.mall.newbeemallapi.entity.MallUser;
import ltd.newbee.mall.newbeemallapi.entity.MallUserToken;

@Service
public class NewBeeMallUserServiceImpl implements NewBeeMallUserService{
	
	@Resource
	MallUserMapper mallUserMapper;
	
	@Resource
	NewBeeMallUserTokenMapper newBeeMallUserTokenMapper;

	@Override
	public String login(String loginName, String passwordMD5) {
		MallUser user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
		if (user != null) {
			if (user.getLockedFlag() == 1) {
				return "您已被禁用!";
			}
			String token = getNewToken(System.currentTimeMillis() + "",user.getUserId());
			MallUserToken mallUserToken = newBeeMallUserTokenMapper.selectByPrimaryKey(user.getUserId());
			
			Date now = new Date();
			
			Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);//过期时间 48 小时
			if (mallUserToken == null) {
				MallUserToken insertItem = new MallUserToken();
				insertItem.setExpireTime(expireTime);
				insertItem.setUpdateTime(now);
				insertItem.setToken(token);
				insertItem.setUserId(user.getUserId());
				if(newBeeMallUserTokenMapper.insertSelective(insertItem) > 0) {
					return token;
				}
			} else {
				mallUserToken.setUpdateTime(now);
				mallUserToken.setExpireTime(expireTime);
				mallUserToken.setToken(token);
				if(newBeeMallUserTokenMapper.updateByPrimaryKeySelective(mallUserToken) > 0) {
					return token;
				}
			}
		}
		
		return "您尚未注册/密码错误!";
		
	}

	private String getNewToken(String time, Long userId) {
		String src = time + userId + NumberUtil.genRandomNum(4);
		return SystemUtil.genToken(src);
	}

	@Override
	public boolean deletTokeneByPrimaryKey(Long userId) {
		return newBeeMallUserTokenMapper.deleteTokenByPrimaryKey(userId) > 0;
	}

	@Override
	public String register(String loginName, String password) {
		if (mallUserMapper.selectByLoginName(loginName) != null) {
			return "您的手机号已被注册!";
		}
		MallUser user = new MallUser();
		user.setLoginName(loginName);
		user.setPasswordMd5(password);
		user.setNickName("昵称");
		user.setIntroduceSign("个性签名");
		if (mallUserMapper.insertSelective(user) > 0) {
			return "注册成功!";
		}
		return "注册失败!";
	}


	@Override
	public Boolean updateUserInfo(MallUserUpdateParam mallUser, Long userId) {
		MallUser user = mallUserMapper.selectByPrimaryKey(userId);
		if (user == null) {
			NewBeeMallException.fail("用户不存在");
		}
		user.setNickName(mallUser.getNickName());
		user.setIntroduceSign(mallUser.getIntroduceSign());
		if (mallUserMapper.updateUserInfo(user) > 0 ) {
			return true;
		}
		return false;
	}

}
