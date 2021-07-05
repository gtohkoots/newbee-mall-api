package ltd.newbee.mall.newbeemallapi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import ltd.newbee.mall.newbeemallapi.entity.MallUser;

@Mapper
public interface MallUserMapper {

	MallUser selectByLoginNameAndPasswd(@Param("loginName") String loginName, @Param("password") String passwordMD5);

	MallUser selectByPrimaryKey(Long userId);

	MallUser selectByLoginName(String loginName);

	int insertSelective(MallUser user);

	int updateUserInfo(MallUser user);	

	
}
