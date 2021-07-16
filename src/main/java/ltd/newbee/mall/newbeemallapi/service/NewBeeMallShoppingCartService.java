package ltd.newbee.mall.newbeemallapi.service;

import java.util.List;

import ltd.newbee.mall.newbeemallapi.api.mall.param.SaveCartItemParam;
import ltd.newbee.mall.newbeemallapi.api.mall.param.UpdateCartItemParam;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallShoppingCartItem;

public interface NewBeeMallShoppingCartService {

	String saveNewBeeMallCartItem(SaveCartItemParam saveCartItemParam, Long userId);

	List<NewBeeMallShoppingCartItemVO> getMyShoppingCartItems(Long userId);

	NewBeeMallShoppingCartItem getMallItemById(Long newBeeMallShoppingCartItemId);

	Boolean deleteById(Long newBeeMallShoppingCartItemId);

	String updateNewBeeMallShoppingCartItem(UpdateCartItemParam updateCartItemParam, Long userId);

	List<NewBeeMallShoppingCartItemVO> getCartItemsForSettle(List<Long> asList, Long userId);

}
