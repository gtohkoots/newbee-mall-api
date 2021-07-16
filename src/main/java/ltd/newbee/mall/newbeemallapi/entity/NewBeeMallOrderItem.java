package ltd.newbee.mall.newbeemallapi.entity;

import java.util.Date;

import lombok.Data;

@Data
public class NewBeeMallOrderItem {
    private Long orderItemId;

    private Long orderId;

    private Long goodsId;

    private String goodsName;

    private String goodsCoverImg;

    private Integer sellingPrice;

    private Integer goodsCount;

    private Date createTime;
}
