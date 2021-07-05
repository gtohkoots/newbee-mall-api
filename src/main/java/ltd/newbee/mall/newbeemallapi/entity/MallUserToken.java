package ltd.newbee.mall.newbeemallapi.entity;

import java.util.Date;

import lombok.Data;

@Data
public class MallUserToken {
    private Long userId;

    private String token;

    private Date updateTime;

    private Date expireTime;

}
