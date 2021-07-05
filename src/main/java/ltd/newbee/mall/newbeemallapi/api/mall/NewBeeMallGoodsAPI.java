package ltd.newbee.mall.newbeemallapi.api.mall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.newbee.mall.newbeemallapi.config.annotation.TokenToMallUser;
import ltd.newbee.mall.newbeemallapi.entity.MallUser;
import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallGoods;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallGoodsService;
import ltd.newbee.mall.newbeemallapi.util.PageResult;
import ltd.newbee.mall.newbeemallapi.util.Result;
import ltd.newbee.mall.newbeemallapi.util.ResultGenerator;
import ltd.newbee.mall.newbeemallapi.util.BeanUtil;
import ltd.newbee.mall.newbeemallapi.util.PageQueryUtil;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallGoodsDetailVO;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallSearchGoodsVO;
import ltd.newbee.mall.newbeemallapi.common.NewBeeMallException;


@RestController
@Api(value = "v1", tags = "4.新蜂商城商品接口")
public class NewBeeMallGoodsAPI {
	
	private static final Logger logger = LoggerFactory.getLogger(NewBeeMallGoodsAPI.class);
	
	@Resource
	NewBeeMallGoodsService newBeeMallGoodsService;
	
	@RequestMapping(value="/search", method = RequestMethod.GET)
	@ApiOperation(value = "商品搜索接口", notes = "根据关键字和分类id进行搜索")
	public Result<PageResult<List<NewBeeMallSearchGoodsVO>>> search(
			@RequestParam(required = false) @ApiParam(value = "搜索关键字") String keyword, 
			@RequestParam(required = false) @ApiParam(value = "分类id") Long goodsCategoryId,
			@RequestParam(required = false) @ApiParam(value = "orderBy") String orderBy,
			@RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
			@TokenToMallUser MallUser loginMallUser) {
		
		logger.info("goods search api,keyword={},goodsCategoryId={},orderBy={},pageNumber={},userId={}", keyword, goodsCategoryId, orderBy, pageNumber, loginMallUser.getUserId());
		
		Map params = new HashMap(8);
		if (goodsCategoryId == null && keyword.equals("")) {
			NewBeeMallException.fail("非法的搜索参数");
		}
		if (pageNumber == null || pageNumber < 1) {
			pageNumber = 1;
		}
		params.put("goodsCategoryId", goodsCategoryId);
		params.put("pageNumber", pageNumber);
		params.put("limit", 10);
		if(!StringUtils.isEmpty(keyword)) {
			params.put("keyword", keyword);
		}
		if(!StringUtils.isEmpty(orderBy)) {
			params.put("orderBy", orderBy);
		}
        //搜索上架状态下的商品
        params.put("goodsSellStatus", 0);
        
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        
        System.out.println(pageUtil.toString());
		return ResultGenerator.genSuccessResult(newBeeMallGoodsService.searchNewBeeMallGoods(pageUtil));
	}
	
	@RequestMapping(value = "/goods/detail/{goodsId}", method= RequestMethod.GET)
	@ApiOperation(value = "商品详情接口", notes = "传参为商品id")
	public Result<NewBeeMallGoodsDetailVO> goodsDetail(@ApiParam(value = "商品id") @PathVariable("goodsId") Long goodsId) {
		if(goodsId < 1) {
			return ResultGenerator.genFailResult("错误参数");
		}
		NewBeeMallGoods good = newBeeMallGoodsService.getNewBeeGoodsById(goodsId);
		if(good == null) {
			return ResultGenerator.genFailResult("拉取物品失败!");
		}
		NewBeeMallGoodsDetailVO newBeeMallGoodsDetailVO = new NewBeeMallGoodsDetailVO();
		
		BeanUtil.copyProperties(good, newBeeMallGoodsDetailVO);
		newBeeMallGoodsDetailVO.setGoodsCarouselList(good.getGoodsCarousel().split(","));
		return ResultGenerator.genSuccessResult(newBeeMallGoodsDetailVO);
	}
	
	
	

}
