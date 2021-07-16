package ltd.newbee.mall.newbeemallapi.service;

import java.util.List;

import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallOrderDetailVO;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.newbeemallapi.entity.MallUser;
import ltd.newbee.mall.newbeemallapi.entity.MallUserAddress;
import ltd.newbee.mall.newbeemallapi.util.PageQueryUtil;
import ltd.newbee.mall.newbeemallapi.util.PageResult;

public interface NewBeeMallOrderService {

	String saveOrder(MallUser loginMallUser, MallUserAddress userAddress,
			List<NewBeeMallShoppingCartItemVO> itemsForSale);

	PageResult getMyOrders(PageQueryUtil pageUtil);

	String paySuccess(String orderNo, int payType);

	NewBeeMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

	String cancelOrder(String orderNo, Long userId);

}
