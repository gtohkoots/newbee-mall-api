package ltd.newbee.mall.newbeemallapi.entity;

import java.util.Date;

import lombok.Data;

@Data
public class NewBeeMallShoppingCartItem {
	
    private Long cartItemId;

    private Long userId;

    private Long goodsId;

    private Integer goodsCount;

    private Byte isDeleted;

    private Date createTime;

    private Date updateTime;
}
