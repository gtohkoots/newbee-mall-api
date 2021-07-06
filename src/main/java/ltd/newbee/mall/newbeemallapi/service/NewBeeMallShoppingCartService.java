package ltd.newbee.mall.newbeemallapi.service;

import java.util.List;

import ltd.newbee.mall.newbeemallapi.api.mall.param.SaveCartItemParam;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallShoppingCartItemVO;

public interface NewBeeMallShoppingCartService {

	String saveNewBeeMallCartItem(SaveCartItemParam saveCartItemParam, Long userId);

	List<NewBeeMallShoppingCartItemVO> getMyShoppingCartItems(Long userId);


}
