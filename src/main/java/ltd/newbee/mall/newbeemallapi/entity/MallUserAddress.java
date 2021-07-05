package ltd.newbee.mall.newbeemallapi.entity;

import java.util.Date;

import lombok.Data;

@Data
public class MallUserAddress {
	
    private Long addressId;

    private Long userId;

    private String userName;

    private String userPhone;

    private Byte defaultFlag;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String detailAddress;

    private Byte isDeleted;

    private Date createTime;

    private Date updateTime;

}
