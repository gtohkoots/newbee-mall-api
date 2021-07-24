package ltd.newbee.mall.newbeemallapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallIndexCarouselVO;
import ltd.newbee.mall.newbeemallapi.dao.CarouselMapper;
import ltd.newbee.mall.newbeemallapi.entity.Carousel;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallCarouselService;
import ltd.newbee.mall.newbeemallapi.util.BeanUtil;

@Service
public class NewBeeMallCarouselServiceImpl implements NewBeeMallCarouselService 
{
	@Autowired
	CarouselMapper carouselMapper;

	@Override
	public List<NewBeeMallIndexCarouselVO> getCarouselsForIndex(int number) {
		//调取carousel
		List<Carousel> carousels = carouselMapper.findCarouselByNum(number);
		//创建返回的List
		List<NewBeeMallIndexCarouselVO> return_list = new ArrayList<NewBeeMallIndexCarouselVO>();
		if(!CollectionUtils.isEmpty(carousels)) {
			return_list = BeanUtil.copyList(carousels, NewBeeMallIndexCarouselVO.class);
		}
		return return_list;
	}

	/*
	 * @Override public List<NewBeeMallIndexCarouselVO> getCarouselsForIndex(int
	 * number) { List<NewBeeMallIndexCarouselVO> newBeeCarouselList = new
	 * ArrayList<>(number); List<Carousel> carousels =
	 * carouselMapper.findCarouselByNum(number);
	 * if(!CollectionUtils.isEmpty(carousels)) { newBeeCarouselList =
	 * BeanUtil.copyList(carousels, NewBeeMallIndexCarouselVO.class); } return
	 * newBeeCarouselList; }
	 */

}
