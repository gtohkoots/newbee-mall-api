package ltd.newbee.mall.newbeemallapi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import ltd.newbee.mall.newbeemallapi.api.mall.param.SaveCartItemParam;
import ltd.newbee.mall.newbeemallapi.api.mall.param.UpdateCartItemParam;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.newbeemallapi.common.Constants;
import ltd.newbee.mall.newbeemallapi.common.NewBeeMallException;
import ltd.newbee.mall.newbeemallapi.dao.NewBeeMallGoodsMapper;
import ltd.newbee.mall.newbeemallapi.dao.NewBeeMallShoppingCartItemMapper;
import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallGoods;
import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallShoppingCartItem;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallShoppingCartService;
import ltd.newbee.mall.newbeemallapi.util.BeanUtil;

@Service
public class NewBeeMallShoppingCartServiceImpl implements NewBeeMallShoppingCartService{
	
	@Autowired
	NewBeeMallShoppingCartItemMapper newBeeMallShoppingCartItemMapper;
	
	@Autowired 
	NewBeeMallGoodsMapper goodsMapper;

	@Override
	public String saveNewBeeMallCartItem(SaveCartItemParam saveCartItemParam, Long userId) {
		NewBeeMallShoppingCartItem item = newBeeMallShoppingCartItemMapper.selectByUserIdAndGoodsId(userId, saveCartItemParam.getGoodsId());
		
		if (item != null) {
			return "已存在！无需重复添加！";
		}
		
		NewBeeMallGoods good = goodsMapper.selectByPrimaryKey(saveCartItemParam.getGoodsId());
		
		if (good == null) {
			return "物品不存在";
		}
		int totalItem = newBeeMallShoppingCartItemMapper.selectCountByUserId(userId);
        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() < 1) {
            return "商品数量不能小于 1 ！";
        }
        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return "超出单个商品的最大数量";
        }
        
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return "超出购物车最大数量";
        }
        
        NewBeeMallShoppingCartItem cartItem = new NewBeeMallShoppingCartItem();
        BeanUtil.copyProperties(saveCartItemParam, cartItem);
        cartItem.setUserId(userId);
        if (newBeeMallShoppingCartItemMapper.insertSelective(cartItem) > 0) {
        	return "success";
        }
		return "error";
	}

	@Override
	public List<NewBeeMallShoppingCartItemVO> getMyShoppingCartItems(Long userId) {
		List<NewBeeMallShoppingCartItemVO> result = new ArrayList<>();
		List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItem = newBeeMallShoppingCartItemMapper.selectByUserId(userId, 20);
		//System.out.println("This is the item list in cart: " + newBeeMallShoppingCartItem.toString());
		return getNewBeeMallShoppingCartItemVOS(result,newBeeMallShoppingCartItem);
	}

	private List<NewBeeMallShoppingCartItemVO> getNewBeeMallShoppingCartItemVOS(
			List<NewBeeMallShoppingCartItemVO> result, List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItem) {
		if(!CollectionUtils.isEmpty(newBeeMallShoppingCartItem)) {
			List<Long> goodsId = newBeeMallShoppingCartItem.stream().map(NewBeeMallShoppingCartItem::getGoodsId).collect(Collectors.toList());
			List<NewBeeMallGoods> goodsList = goodsMapper.selectByPrimaryKeys(goodsId);
			Map<Long, NewBeeMallGoods> map = new HashMap<>();
			if(!CollectionUtils.isEmpty(goodsList)) {
				map = goodsList.stream().collect(Collectors.toMap(NewBeeMallGoods::getGoodsId, Function.identity(),(existing, replacement) -> existing));
			}
			for(NewBeeMallShoppingCartItem item: newBeeMallShoppingCartItem) {
				NewBeeMallShoppingCartItemVO vObj = new NewBeeMallShoppingCartItemVO();
				BeanUtil.copyProperties(item, vObj);
				if(map.containsKey(item.getGoodsId())) {
					NewBeeMallGoods mallGood = map.get(item.getGoodsId());
					vObj.setGoodsCoverImg(mallGood.getGoodsCoverImg());
					vObj.setSellingPrice(mallGood.getSellingPrice());
					String name = mallGood.getGoodsName();
					//字符过长
					if (name.length() > 20) {
						name = name.substring(0,20) + "....";
					}
					vObj.setGoodsName(name);
					result.add(vObj);
				}
			}
		}
		return result;
	}

	@Override
	public NewBeeMallShoppingCartItem getMallItemById(Long newBeeMallShoppingCartItemId) {
		return newBeeMallShoppingCartItemMapper.selectByPrimaryKey(newBeeMallShoppingCartItemId);
	}

	@Override
	public Boolean deleteById(Long newBeeMallShoppingCartItemId) {
		return newBeeMallShoppingCartItemMapper.deleteByPrimaryKey(newBeeMallShoppingCartItemId) > 0;
	}

	@Override
	public String updateNewBeeMallShoppingCartItem(UpdateCartItemParam updateCartItemParam, Long userId) {
		NewBeeMallShoppingCartItem item = newBeeMallShoppingCartItemMapper.selectByPrimaryKey(updateCartItemParam.getCartItemId());
		if (item == null) {
			return "物品不存在";
		}
		if (updateCartItemParam.getGoodsCount() > 5) {
			return "物品数量超过要求";
		}
		if (!item.getUserId().equals(userId)) {
			return "物品并非您购买";
		}
		
		item.setGoodsCount(updateCartItemParam.getGoodsCount());
		item.setUpdateTime(new Date());
		
		int result = newBeeMallShoppingCartItemMapper.updateByPrimaryKeySelective(item);
		if (result > 0) {
			return "success";
		}
		return "error";
	}

	@Override
	public List<NewBeeMallShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long userId) {
		List<NewBeeMallShoppingCartItemVO> NewBeeMallShoppingCartItemVOs = new ArrayList<>();
		
		List<NewBeeMallShoppingCartItem> newBeeMallCartItems= newBeeMallShoppingCartItemMapper.selectByUserIdandCartItemsId(cartItemIds, userId);
		
		if (CollectionUtils.isEmpty(newBeeMallCartItems)) {
			NewBeeMallException.fail("返回值为空");
		}
		
		if (newBeeMallCartItems.size() != cartItemIds.size()) {
			NewBeeMallException.fail("参数异常");
		}
		return getNewBeeMallShoppingCartItemVOS(NewBeeMallShoppingCartItemVOs, newBeeMallCartItems);
	}

}
