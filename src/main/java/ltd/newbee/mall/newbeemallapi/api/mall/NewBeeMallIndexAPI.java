package ltd.newbee.mall.newbeemallapi.api.mall;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.IndexInfoVO;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallCarouselService;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallIndexConfigService;
import ltd.newbee.mall.newbeemallapi.util.Result;
import ltd.newbee.mall.newbeemallapi.util.ResultGenerator;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallIndexCarouselVO;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallIndexConfigGoodsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "v1", tags = "1.新蜂商城首页接口")
public class NewBeeMallIndexAPI {
	
	@Resource
	private NewBeeMallCarouselService newBeecarouselService;
	
	@Resource
	private NewBeeMallIndexConfigService newBeeMallIndexConfigService;
	
	@RequestMapping(value="/index-infos", method = RequestMethod.GET)
	@ApiOperation(value = "获取首页数据", notes = "轮播图、新品、推荐等")
	public Result<IndexInfoVO> IndexInfo() {
		IndexInfoVO indexInfo = new IndexInfoVO();
		List<NewBeeMallIndexCarouselVO> carousel = newBeecarouselService.getCarouselsForIndex(5);
		List<NewBeeMallIndexConfigGoodsVO> hotGoods = newBeeMallIndexConfigService.getConfigGoodsesForIndex(3, 4);
		List<NewBeeMallIndexConfigGoodsVO> newGoods = newBeeMallIndexConfigService.getConfigGoodsesForIndex(4, 5);
		List<NewBeeMallIndexConfigGoodsVO> recommendGoods = newBeeMallIndexConfigService.getConfigGoodsesForIndex(5, 10);
		indexInfo.setCarousels(carousel);
		indexInfo.setHotGoodses(hotGoods);
		indexInfo.setNewGoodses(newGoods);
		indexInfo.setRecommendGoodses(recommendGoods);
		return ResultGenerator.genSuccessResult(indexInfo);
	}

	
}
