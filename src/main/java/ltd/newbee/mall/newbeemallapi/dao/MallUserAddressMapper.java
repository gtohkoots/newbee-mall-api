package ltd.newbee.mall.newbeemallapi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import ltd.newbee.mall.newbeemallapi.entity.MallUserAddress;

@Mapper
public interface MallUserAddressMapper {

	List<MallUserAddress> findMyAddressList(Long userId);

	MallUserAddress getDefaultAddress(Long userId);

	int updateByPrimaryKeySelective(MallUserAddress defaultAddress);

	int insertSelective(MallUserAddress userAddress);

}
