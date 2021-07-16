package ltd.newbee.mall.newbeemallapi.dao;

import org.apache.ibatis.annotations.Mapper;

import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallOrderAddress;

@Mapper
public interface NewBeeMallOrderAddressMapper {

	int insertSelective(NewBeeMallOrderAddress orderAddress);

}
