package ltd.newbee.mall.newbeemallapi.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallUserAddressVO;
import ltd.newbee.mall.newbeemallapi.dao.MallUserAddressMapper;
import ltd.newbee.mall.newbeemallapi.entity.MallUserAddress;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallUserAddressService;
import ltd.newbee.mall.newbeemallapi.util.BeanUtil;

@Service
public class NewBeeMallUserAddressServiceImpl implements NewBeeMallUserAddressService {

	@Autowired
	MallUserAddressMapper addressMapper;
	
	@Override
	public List<NewBeeMallUserAddressVO> getMyAddresses(Long userId) {
		List<MallUserAddress> myAddressList = addressMapper.findMyAddressList(userId);
		List<NewBeeMallUserAddressVO> returnList = BeanUtil.copyList(myAddressList,NewBeeMallUserAddressVO.class);
		return returnList;
	}

	@Override
	public Boolean saveUserAddress(MallUserAddress userAddress) {
		Date now = new Date();
		//添加默认地址，需要将原有的默认地址修改掉
		if(userAddress.getDefaultFlag().intValue() == 1) {
			MallUserAddress defaultAddress = addressMapper.getDefaultAddress(userAddress.getUserId());
			if (defaultAddress != null) {
				defaultAddress.setDefaultFlag((byte) 0);
				defaultAddress.setUpdateTime(now);
				int updateResult = addressMapper.updateByPrimaryKeySelective(defaultAddress);
				if (updateResult < 1) {
					return false;
				}
			}
		}
		return addressMapper.insertSelective(userAddress) > 0;
	}
	
	

}
