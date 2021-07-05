package ltd.newbee.mall.newbeemallapi.api.mall.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NewBeeMallIndexCarouselVO implements Serializable {
	
	@ApiModelProperty("轮播图图片地址")
	private String carouselUrl;
	
	@ApiModelProperty("轮播图图片地址")
	private String redirectUrl;

}
