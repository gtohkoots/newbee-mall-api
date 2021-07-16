package ltd.newbee.mall.newbeemallapi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallOrderItem;

@Mapper
public interface NewBeeMallOrderItemMapper {

	int insertBatch(List<NewBeeMallOrderItem> newBeeMallOrderItems);

	List<NewBeeMallOrderItem> selectByOrderIds(List<Long> orderIds);

	List<NewBeeMallOrderItem> selectByOrderId(Long orderId);

}
