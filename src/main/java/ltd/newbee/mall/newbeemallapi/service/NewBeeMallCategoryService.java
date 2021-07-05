package ltd.newbee.mall.newbeemallapi.service;

import java.util.List;

import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallIndexCategoryVO;
import ltd.newbee.mall.newbeemallapi.entity.GoodsCategory;

public interface NewBeeMallCategoryService {

	List<NewBeeMallIndexCategoryVO> getCategoriesForIndex();
	
	List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);

}
