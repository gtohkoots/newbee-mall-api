package ltd.newbee.mall.newbeemallapi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallIndexConfigGoodsVO;
import ltd.newbee.mall.newbeemallapi.dao.NewBeeMallGoodsMapper;
import ltd.newbee.mall.newbeemallapi.dao.indexConfigMapper;
import ltd.newbee.mall.newbeemallapi.entity.IndexConfig;
import ltd.newbee.mall.newbeemallapi.entity.NewBeeMallGoods;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallIndexConfigService;
import ltd.newbee.mall.newbeemallapi.util.BeanUtil;

@Service
public class NewBeeMallIndexConfigServiceImpl implements NewBeeMallIndexConfigService {
	
	@Autowired
	indexConfigMapper configMapper;
	
	@Autowired
	NewBeeMallGoodsMapper goodsMapper;

	@Override
	public List<NewBeeMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
		//3为热销 4是新品
		List<IndexConfig> indexConfigs = configMapper.findIndexConfigsByTypeAndNum(configType, number);
		List<NewBeeMallIndexConfigGoodsVO> return_list =new ArrayList<NewBeeMallIndexConfigGoodsVO>();
		if(!CollectionUtils.isEmpty(indexConfigs)) {
			//收集所有的物品id
			List<Long> goodsId = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
			//从数据库调取物品
			List<NewBeeMallGoods> goods = goodsMapper.selectByPrimaryKeys(goodsId);
			
			return_list = BeanUtil.copyList(goods, NewBeeMallIndexConfigGoodsVO.class);
			
			//处理文字过多的情况
			for(NewBeeMallIndexConfigGoodsVO item: return_list) {
				String goodsIntro = item.getGoodsIntro();
				String goodsName = item.getGoodsName();
				if (goodsIntro.length() >= 32) {
					goodsIntro = goodsIntro.substring(0, 32) + "....";
					item.setGoodsIntro(goodsIntro);
				}
				if (goodsName.length() > 20) {
					goodsName = goodsName.substring(0, 20) + "....";
					item.setGoodsName(goodsName);
				}
			}
			
		}
		return return_list;
	}

	/*
	 * @Override public List<NewBeeMallIndexConfigGoodsVO>
	 * getConfigGoodsesForIndex(int configType, int number) {
	 * List<NewBeeMallIndexConfigGoodsVO> newBeeMallIndexConfigGoodsVOS = new
	 * ArrayList<>(number); List<IndexConfig> indexConfigs =
	 * configMapper.findIndexConfigsByTypeAndNum(configType, number);
	 * if(!CollectionUtils.isEmpty(indexConfigs)) { //取出所有goodsid List<Long> goodsId
	 * =
	 * indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList(
	 * )); List<NewBeeMallGoods> newBeeMallGoods =
	 * goodsMapper.selectByPrimaryKeys(goodsId); newBeeMallIndexConfigGoodsVOS =
	 * BeanUtil.copyList(newBeeMallGoods, NewBeeMallIndexConfigGoodsVO.class);
	 * for(NewBeeMallIndexConfigGoodsVO item: newBeeMallIndexConfigGoodsVOS) {
	 * String goodsName = item.getGoodsName(); String goodsIntro =
	 * item.getGoodsIntro(); if (goodsName.length() > 22) { goodsName =
	 * goodsName.substring(0,22) + "....."; item.setGoodsName(goodsName); } if
	 * (goodsIntro.length() > 30) { goodsIntro = goodsIntro.substring(0,30) +
	 * "....."; item.setGoodsIntro(goodsIntro); } } } return
	 * newBeeMallIndexConfigGoodsVOS; }
	 */

}
