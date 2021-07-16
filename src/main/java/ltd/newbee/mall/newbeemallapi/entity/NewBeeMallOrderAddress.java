package ltd.newbee.mall.newbeemallapi.entity;

import lombok.Data;

@Data
public class NewBeeMallOrderAddress {
    private Long orderId;

    private String userName;

    private String userPhone;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String detailAddress;	
}
