package ltd.newbee.mall.newbeemallapi.api.mall.param;

import lombok.Data;

@Data
public class UpdateCartItemParam {

	private Long cartItemId;
	
	private int goodsCount;
}
