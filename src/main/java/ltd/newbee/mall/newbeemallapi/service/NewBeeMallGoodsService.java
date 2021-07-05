package ltd.newbee.mall.newbeemallapi.service;

import java.util.List;

import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallGoods;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallSearchGoodsVO;
import ltd.newbee.mall.newbeemallapi.util.PageQueryUtil;
import ltd.newbee.mall.newbeemallapi.util.PageResult;
import ltd.newbee.mall.newbeemallapi.util.Result;

public interface NewBeeMallGoodsService {

	PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil);

	NewBeeMallGoods getNewBeeGoodsById(Long goodsId);

}
