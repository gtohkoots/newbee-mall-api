package ltd.newbee.mall.newbeemallapi.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SaveOrderParam {

	@ApiModelProperty("地址id")
	private Long addressId;
	
	@ApiModelProperty("订单项id数组")
	private Long[] cartItemIds;
}
