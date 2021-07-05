package ltd.newbee.mall.newbeemallapi.config;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ltd.newbee.mall.newbeemallapi.common.Constants;
import ltd.newbee.mall.newbeemallapi.config.handler.TokenToMallUserMethodArgumentResolver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;


@Configuration
public class NeeBeeMallWebMvcConfigurer implements WebMvcConfigurer{
	
    @Autowired
    private TokenToMallUserMethodArgumentResolver tokenToMallUserMethodArgumentResolver;
	
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
        registry.addResourceHandler("/goods-img/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
    }
    
    /**
     * @param argumentResolvers
     * @tip @TokenToMallUser @TokenToAdminUser 注解处理方法
     */
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(tokenToMallUserMethodArgumentResolver);
    }
    
    /**
     * 跨域配置
     *
     * @param registry
     */
	@Override
	public void addCorsMappings(CorsRegistry registery) {
		registery.addMapping("/**").allowedOrigins("*")
        .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
        .allowCredentials(true).maxAge(3600);
	}
	

}
