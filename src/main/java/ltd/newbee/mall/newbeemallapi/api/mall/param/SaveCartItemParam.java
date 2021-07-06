package ltd.newbee.mall.newbeemallapi.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SaveCartItemParam {

	@ApiModelProperty("商品数量")
	private Long goodsCount;
	
	@ApiModelProperty("商品id")
	private Long goodsId;
}
