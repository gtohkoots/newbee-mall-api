package ltd.newbee.mall.newbeemallapi.service;

import java.util.List;

import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallUserAddressVO;
import ltd.newbee.mall.newbeemallapi.entity.MallUserAddress;

public interface NewBeeMallUserAddressService {

	List<NewBeeMallUserAddressVO> getMyAddresses(Long userId);

	Boolean saveUserAddress(MallUserAddress userAddress);

}
