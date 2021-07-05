package ltd.newbee.mall.newbeemallapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallSearchGoodsVO;
import ltd.newbee.mall.newbeemallapi.dao.NewBeeMallGoodsMapper;
import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallGoods;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallGoodsService;
import ltd.newbee.mall.newbeemallapi.util.BeanUtil;
import ltd.newbee.mall.newbeemallapi.util.PageQueryUtil;
import ltd.newbee.mall.newbeemallapi.util.PageResult;
import ltd.newbee.mall.newbeemallapi.util.Result;

@Service
public class NewBeeMallGoodsServiceImpl implements NewBeeMallGoodsService {

	@Autowired
	private NewBeeMallGoodsMapper goodsMapper;
	
	@Override
	public PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil) {
		List<NewBeeMallGoods> goodsList = goodsMapper.findNewBeeMallGoodsBySearch(pageUtil);
		int total = goodsMapper.findTotalNewBeeMallGoodsBySearch(pageUtil);
		List<NewBeeMallSearchGoodsVO> returnList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(goodsList)) {
			returnList = BeanUtil.copyList(goodsList, NewBeeMallSearchGoodsVO.class);
			for(NewBeeMallSearchGoodsVO item: returnList) {
				String goodsName = item.getGoodsName();
				String goodsIntro = item.getGoodsIntro();
				if (goodsName.length() > 20) {
					goodsName = goodsName.substring(0,20) + "....";
					item.setGoodsName(goodsName);
				}
				if(goodsIntro.length() > 28) {
					goodsIntro = goodsIntro.substring(0,28) + "....";
					item.setGoodsIntro(goodsIntro);
				}
			}
		}
		PageResult result = new PageResult(total,pageUtil.getLimit(),pageUtil.getPage(),returnList);
		return result;
	}

	@Override
	public NewBeeMallGoods getNewBeeGoodsById(Long goodsId) {
		return goodsMapper.getNewBeeGoodsById(goodsId);
	}


}
