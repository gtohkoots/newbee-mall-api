package ltd.newbee.mall.newbeemallapi.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ThirdLevelCategoryVO {
	
    @ApiModelProperty("当前三级分类id")
    private Long categoryId;

    @ApiModelProperty("当前分类级别")
    private Byte categoryLevel;

    @ApiModelProperty("当前三级分类名称")
    private String categoryName;

}
