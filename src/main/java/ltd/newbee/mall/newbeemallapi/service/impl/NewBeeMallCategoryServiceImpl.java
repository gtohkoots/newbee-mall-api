package ltd.newbee.mall.newbeemallapi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;

import ltd.newbee.mall.newbeemallapi.api.mall.vo.NewBeeMallIndexCategoryVO;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.SecondLevelCategoryVO;
import ltd.newbee.mall.newbeemallapi.api.mall.vo.ThirdLevelCategoryVO;
import ltd.newbee.mall.newbeemallapi.dao.GoodsCategoryMapper;
import ltd.newbee.mall.newbeemallapi.entity.GoodsCategory;
import ltd.newbee.mall.newbeemallapi.service.NewBeeMallCategoryService;
import ltd.newbee.mall.newbeemallapi.util.BeanUtil;

@Service
public class NewBeeMallCategoryServiceImpl implements NewBeeMallCategoryService {
	
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;

	@Override
	public List<NewBeeMallIndexCategoryVO> getCategoriesForIndex() {
		List<NewBeeMallIndexCategoryVO> newBeeMallIndexCategoryVOS = new ArrayList<>();
		
		// First Level
		List<Long> levelOne = new ArrayList<Long>();
		levelOne.add((long) 0);
		List<GoodsCategory> firstLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(levelOne, 1, 10);
		if (!CollectionUtils.isEmpty(firstLevelCategories)) {
			List<Long> firstParentIds = firstLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
			
			//Second Level
			List<GoodsCategory> secondLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(firstParentIds, 2, 0);
			if (!CollectionUtils.isEmpty(secondLevelCategories)) {
				List<Long> secondParentIds = secondLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
				
				//Third Level
				List<GoodsCategory> thirdLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(secondParentIds, 3, 0);
				if (!CollectionUtils.isEmpty(thirdLevelCategories)) {
					//根据parentId将thirdlevelCategories分组
					Map<Long,List<GoodsCategory>> thirdLevelCategoryMap = thirdLevelCategories.stream().collect(groupingBy(GoodsCategory::getParentId));
					List<SecondLevelCategoryVO> secondLevelCategoryVOS = new ArrayList<>();
					
					for (GoodsCategory seondLevelItem: secondLevelCategories) {
						SecondLevelCategoryVO secondLevelCategoryVO = new SecondLevelCategoryVO();
						BeanUtil.copyProperties(seondLevelItem, secondLevelCategoryVO);
						//如果该二级分类下有数据则放入 secondLevelCategoryVOS 对象中
						if (thirdLevelCategoryMap.containsKey(secondLevelCategoryVO.getCategoryId())) {
							//根据二级分类的id取出thirdLevelCategoryMap分组中的三级分类list
							List<GoodsCategory> tempGoodsCategories = thirdLevelCategoryMap.get(secondLevelCategoryVO.getCategoryId());
							secondLevelCategoryVO.setThirdLevelCategoryVOS(BeanUtil.copyList(tempGoodsCategories, ThirdLevelCategoryVO.class));
							secondLevelCategoryVOS.add(secondLevelCategoryVO);
						}
					}
					// 到这里二级数据已经处理完毕
					
					//开始处理一级数据
					if (!CollectionUtils.isEmpty(secondLevelCategoryVOS)) {
						Map<Long, List<SecondLevelCategoryVO>> secondLevelCategoryMap = secondLevelCategoryVOS.stream().collect(groupingBy(SecondLevelCategoryVO::getParentId));
						
						for ( GoodsCategory firstLevelItem : firstLevelCategories ) {
							NewBeeMallIndexCategoryVO newBeeMallIndexCategoryVO = new NewBeeMallIndexCategoryVO();
							BeanUtil.copyProperties(firstLevelItem, newBeeMallIndexCategoryVO);
							if(secondLevelCategoryMap.containsKey(newBeeMallIndexCategoryVO.getCategoryId())) {
								List<SecondLevelCategoryVO> tempGoodsFirstLevel = secondLevelCategoryMap.get(newBeeMallIndexCategoryVO.getCategoryId());
								newBeeMallIndexCategoryVO.setSecondLevelCategoryVOS(BeanUtil.copyList(tempGoodsFirstLevel, SecondLevelCategoryVO.class));
								newBeeMallIndexCategoryVOS.add(newBeeMallIndexCategoryVO);
							}
						}
					}
				}
			}
			return newBeeMallIndexCategoryVOS;
		}
		else {
			return null;
		}
	}

	@Override
	public List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel) {
		return goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(parentIds, categoryLevel, 0);
	}
	
	

}
