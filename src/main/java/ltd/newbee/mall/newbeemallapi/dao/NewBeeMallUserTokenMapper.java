package ltd.newbee.mall.newbeemallapi.dao;

import org.apache.ibatis.annotations.Mapper;

import ltd.newbee.mall.newbeemallapi.entity.MallUserToken;

@Mapper
public interface NewBeeMallUserTokenMapper {
	
	MallUserToken selectByPrimaryKey(Long userId);

	int insertSelective(MallUserToken insertItem);

	int updateByPrimaryKeySelective(MallUserToken mallUserToken);

	MallUserToken selectByToken(String token);

	int deleteTokenByPrimaryKey(Long userId);
	
}
