package ltd.newbee.mall.newbeemallapi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import ltd.newbee.mall.newbeemallapi.entity.Carousel;

@Mapper
public interface CarouselMapper {
	
	List<Carousel> findCarouselByNum(@Param("number") int number);

}
