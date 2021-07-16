package ltd.newbee.mall.newbeemallapi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallOrderDetailVO;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallOrderItemVO;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallOrderListVO;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.newbeemallapi.common.NewBeeMallException;
import ltd.newbee.mall.newbeemallapi.common.NewBeeMallOrderStatusEnum;
import ltd.newbee.mall.newbeemallapi.common.PayTypeEnum;
import ltd.newbee.mall.newbeemallapi.common.ServiceResultEnum;
import ltd.newbee.mall.newbeemallapi.dao.NewBeeMallGoodsMapper;
import ltd.newbee.mall.newbeemallapi.dao.NewBeeMallOrderAddressMapper;
import ltd.newbee.mall.newbeemallapi.dao.NewBeeMallOrderItemMapper;
import ltd.newbee.mall.newbeemallapi.dao.NewBeeMallOrderMapper;
import ltd.newbee.mall.newbeemallapi.dao.NewBeeMallShoppingCartItemMapper;
import ltd.newbee.mall.newbeemallapi.entity.MallUser;
import ltd.newbee.mall.newbeemallapi.entity.MallUserAddress;
import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallGoods;
import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallOrder;
import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallOrderAddress;
import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallOrderItem;
import ltd.newbee.mall.newbeemallapi.entity.StockNumDTO;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallOrderService;
import ltd.newbee.mall.newbeemallapi.util.BeanUtil;
import ltd.newbee.mall.newbeemallapi.util.NumberUtil;
import ltd.newbee.mall.newbeemallapi.util.PageQueryUtil;
import ltd.newbee.mall.newbeemallapi.util.PageResult;
import static java.util.stream.Collectors.groupingBy;

@Service
public class NewBeeMallOrderServiceImpl implements NewBeeMallOrderService {
	
	@Autowired
	NewBeeMallGoodsMapper goodsMapper;
	
	@Autowired
	NewBeeMallShoppingCartItemMapper itemMapper;
	
	@Autowired
	NewBeeMallOrderMapper orderMapper;
	
	@Autowired
	NewBeeMallOrderItemMapper orderItemMapper;
	
	@Autowired
	NewBeeMallOrderAddressMapper orderAddressMapper;

	@Override
	public String saveOrder(MallUser loginMallUser, MallUserAddress userAddress,
			List<NewBeeMallShoppingCartItemVO> itemsForSale) {
		List<Long> cartIds = itemsForSale.stream().map(NewBeeMallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
		List<Long> goodIds = itemsForSale.stream().map(NewBeeMallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
		List<NewBeeMallGoods> goods = goodsMapper.selectByPrimaryKeys(goodIds);
		//查看是否有下架的商品
		for (NewBeeMallGoods item: goods) {
			if (item.getGoodsSellStatus() != 0) {
				NewBeeMallException.fail("无法结算,因为有已下架商品!");
			}
		}
		Map<Long, NewBeeMallGoods> map = goods.stream().collect(Collectors.toMap(NewBeeMallGoods::getGoodsId, Function.identity(),(entity1,entity2)->entity1));
		//检查库存
		for(NewBeeMallShoppingCartItemVO item: itemsForSale) {
			if (item.getGoodsCount() > map.get(item.getGoodsId()).getStockNum()) {
				NewBeeMallException.fail(item.getGoodsName()+ "下单数量超过了库存");
			}
		}
		if(!CollectionUtils.isEmpty(goods) && !CollectionUtils.isEmpty(cartIds)) {
			//从购物车数据库删除数据
			if(itemMapper.deleteBatch(cartIds) > 0) {
				List<StockNumDTO> stock = BeanUtil.copyList(itemsForSale, StockNumDTO.class);
				int updateResult = goodsMapper.updateStockNum(stock);
				if (updateResult < 1) {
					NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
				}
				String orderNo = NumberUtil.genOrderNo();
				int price = 0;
				
				NewBeeMallOrder order = new NewBeeMallOrder();
				order.setUserId(loginMallUser.getUserId());
				order.setOrderNo(orderNo);
				
				for (NewBeeMallShoppingCartItemVO item: itemsForSale) {
					price += item.getGoodsCount() * item.getSellingPrice();
				}
				
				if (price < 1) {
					NewBeeMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
				}
				order.setTotalPrice(price);
				String extraInfo = "附加信息";
				order.setExtraInfo(extraInfo);
				//生成订单并保存记录
				if (orderMapper.insertSelective(order) > 0) {
					NewBeeMallOrderAddress orderAddress = new NewBeeMallOrderAddress();
					BeanUtil.copyProperties(userAddress, orderAddress);
					orderAddress.setOrderId(order.getOrderId());
					
					List<NewBeeMallOrderItem> newBeeMallOrderItems = new ArrayList<>();
					for(NewBeeMallShoppingCartItemVO item: itemsForSale) { 
						NewBeeMallOrderItem newBeeItem =  new NewBeeMallOrderItem();
						BeanUtil.copyProperties(item, newBeeItem);
						newBeeItem.setOrderId(order.getOrderId());
						newBeeMallOrderItems.add(newBeeItem);
					}
					if (orderItemMapper.insertBatch(newBeeMallOrderItems) > 0 && orderAddressMapper.insertSelective(orderAddress) > 0) {
						return orderNo;
					}
					NewBeeMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
				}
				NewBeeMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
			}
			NewBeeMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
		}
		NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
		return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
	}

	@Override
	public PageResult getMyOrders(PageQueryUtil pageUtil) {
		int total = orderMapper.getMyOrders(pageUtil);
		List<NewBeeMallOrder> newBeeMallOrders = orderMapper.findNewBeeMallOrderList(pageUtil);
		System.out.println(total + " is the total record");
		System.out.println(newBeeMallOrders.toString() + " is the list returned");
		List<NewBeeMallOrderListVO> orderListVO = new ArrayList<>();
		if(total > 0) {
			orderListVO = BeanUtil.copyList(newBeeMallOrders, NewBeeMallOrderListVO.class);
			for (NewBeeMallOrderListVO item: orderListVO) {
				item.setOrderStatusString(NewBeeMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(item.getOrderStatus()).getName());
			}
			List<Long> orderIds = newBeeMallOrders.stream().map(NewBeeMallOrder::getOrderId).collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(orderIds)) {
				List<NewBeeMallOrderItem> orderItems = orderItemMapper.selectByOrderIds(orderIds);
				Map<Long, List<NewBeeMallOrderItem>> itemMap = orderItems.stream().collect(groupingBy(NewBeeMallOrderItem::getOrderId));
				for(NewBeeMallOrderListVO olv: orderListVO) {
					if(itemMap.containsKey(olv.getOrderId())) {
						List<NewBeeMallOrderItem> tempList = itemMap.get(olv.getOrderId());
						List<NewBeeMallOrderItemVO> listToReturn = BeanUtil.copyList(tempList, NewBeeMallOrderItemVO.class);
						olv.setNewBeeMallOrderItemVOS(listToReturn);
					}
				}
			}
		}
		PageResult result = new PageResult(total,pageUtil.getLimit(),pageUtil.getPage(),orderListVO);
		return result;
	}

	@Override
	public String paySuccess(String orderNo, int payType) {
		NewBeeMallOrder newBeeMallOrder = orderMapper.selectByOrderNo(orderNo);
		if (newBeeMallOrder != null) {
			if (newBeeMallOrder.getOrderStatus().intValue() != 0) {
				return "无需支付此订单";
			}
			newBeeMallOrder.setPayStatus((byte) 1);
			newBeeMallOrder.setPayType((byte)payType);
			newBeeMallOrder.setOrderStatus((byte) 1);
			newBeeMallOrder.setPayTime(new Date());
			newBeeMallOrder.setUpdateTime(new Date());
			
			if (orderMapper.updateByPrimaryKeySelective(newBeeMallOrder) > 0) {
				return "success";
			}
			else {
				return "database error";
			}
			
		}
		return "订单不存在";
	}

	@Override
	public NewBeeMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
		NewBeeMallOrder newBeeMallOrder = orderMapper.selectByOrderNo(orderNo);
		if (newBeeMallOrder == null) {
			NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
		}
		if (!userId.equals(newBeeMallOrder.getUserId())) {
			NewBeeMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
		}
		List<NewBeeMallOrderItem> orderItems = orderItemMapper.selectByOrderId(newBeeMallOrder.getOrderId());
		
		//如果返回了订单中的物品
		if (!CollectionUtils.isEmpty(orderItems)) {
			NewBeeMallOrderDetailVO detailVO = new NewBeeMallOrderDetailVO();
			List<NewBeeMallOrderItemVO> itemVO = BeanUtil.copyList(orderItems, NewBeeMallOrderItemVO.class);
			BeanUtil.copyProperties(newBeeMallOrder, detailVO);
			detailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(detailVO.getPayType()).getName());
			detailVO.setOrderStatusString(NewBeeMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(detailVO.getOrderStatus()).getName());
			detailVO.setNewBeeMallOrderItemVOS(itemVO);
			return detailVO;
		}
		else {
			NewBeeMallException.fail(ServiceResultEnum.ORDER_ITEM_NULL_ERROR.getResult());
		}
		
		return null;
	}

	@Override
	public String cancelOrder(String orderNo, Long userId) {
		NewBeeMallOrder order = orderMapper.selectByOrderNo(orderNo);
		System.out.println("UserId from param: " + userId);
		System.out.println("UserId from db: " + order.getUserId());
		if (order != null) {
			if (order.getUserId() != (userId)) {
				NewBeeMallException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
			}
            if (order.getOrderStatus().intValue() == NewBeeMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus()
                    || order.getOrderStatus().intValue() == NewBeeMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()
                    || order.getOrderStatus().intValue() == NewBeeMallOrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus()
                    || order.getOrderStatus().intValue() == NewBeeMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            
            List<Long> orderToDelete = new ArrayList<>();
            orderToDelete.add(order.getOrderId());
            if (orderMapper.closeOrder(orderToDelete, -1) > 0) {
            	return "success";
            }
            else {
            	return "database error";
            }
			
		}
		return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
	}

}
