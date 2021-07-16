package ltd.newbee.mall.newbeemallapi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallOrder;
import ltd.newbee.mall.newbeemallapi.util.PageQueryUtil;

@Mapper
public interface NewBeeMallOrderMapper {

	int insertSelective(NewBeeMallOrder order);

	int getMyOrders(PageQueryUtil pageUtil);

	List<NewBeeMallOrder> findNewBeeMallOrderList(PageQueryUtil pageUtil);

	NewBeeMallOrder selectByOrderNo(String orderNo);

	int updateByPrimaryKeySelective(NewBeeMallOrder newBeeMallOrder);

	int closeOrder(List<Long> orderToDelete, int i);

}
