package ltd.newbee.mall.newbeemallapi.api.mall;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallIndexCategoryVO;
import ltd.newbee.mall.newbeemallapi.common.NewBeeMallException;
import ltd.newbee.mall.newbeemallapi.util.Result;
import ltd.newbee.mall.newbeemallapi.util.ResultGenerator;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallCategoryService;

@RestController
@Api(value = "v1", tags = "3.新蜂商城种类接口")
public class NewBeeMallGoodsCategoryAPI {
	
	@Resource
	NewBeeMallCategoryService newBeeMallCategoryService;
	
	@RequestMapping( value="/categories", method  = RequestMethod.GET)
	@ApiOperation(value = "获取分类数据", notes = "分类页面使用")
	public Result<List<NewBeeMallIndexCategoryVO>> getCategories() {
		List<NewBeeMallIndexCategoryVO> categories = newBeeMallCategoryService.getCategoriesForIndex();
		if (CollectionUtils.isEmpty(categories)) {
			NewBeeMallException.fail("拉取种类数据失败！");
		}
		
		return ResultGenerator.genSuccessResult(categories);
		
	}

}
