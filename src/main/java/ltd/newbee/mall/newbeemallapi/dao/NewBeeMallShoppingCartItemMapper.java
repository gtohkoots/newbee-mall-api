package ltd.newbee.mall.newbeemallapi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallShoppingCartItem;

@Mapper
public interface NewBeeMallShoppingCartItemMapper {

	NewBeeMallShoppingCartItem selectByUserIdAndGoodsId(Long userId, Long goodsId);

	int selectCountByUserId(Long userId);

	int insertSelective(NewBeeMallShoppingCartItem cartItem);

	List<NewBeeMallShoppingCartItem> selectByUserId(Long userId, int limit_num);

	NewBeeMallShoppingCartItem selectByPrimaryKey(Long newBeeMallShoppingCartItemId);

	int deleteByPrimaryKey(Long newBeeMallShoppingCartItemId);

	int updateByPrimaryKeySelective(NewBeeMallShoppingCartItem item);

	List<NewBeeMallShoppingCartItem> selectByUserIdandCartItemsId(List<Long> cartItemIds, Long userId);

	int deleteBatch(List<Long> cartIds);

}
