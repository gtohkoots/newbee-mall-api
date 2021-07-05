package ltd.newbee.mall.newbeemallapi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallGoods;
import ltd.newbee.mall.newbeemallapi.util.PageQueryUtil;

@Mapper
public interface NewBeeMallGoodsMapper {

	List<NewBeeMallGoods> selectByPrimaryKeys(List<Long> goodsId);

	List<NewBeeMallGoods> findNewBeeMallGoodsBySearch(PageQueryUtil pageUtil);

	int findTotalNewBeeMallGoodsBySearch(PageQueryUtil pageUtil);

	NewBeeMallGoods getNewBeeGoodsById(Long goodsId);

}
