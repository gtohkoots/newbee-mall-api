package ltd.newbee.mall.newbeemallapi.service;

import ltd.newbee.mall.newbeemallapi.api.mall.param.MallUserUpdateParam;
import ltd.newbee.mall.newbeemallapi.entity.MallUser;

public interface NewBeeMallUserService {
    /**
     * 登录
     *
     * @param loginName
     * @param passwordMD5
     * @return
     */
    String login(String loginName, String passwordMD5);

	boolean deletTokeneByPrimaryKey(Long userId);

	String register(String loginName, String password);

	Boolean updateUserInfo(MallUserUpdateParam mallUserUpdateParam, Long userId);
}
