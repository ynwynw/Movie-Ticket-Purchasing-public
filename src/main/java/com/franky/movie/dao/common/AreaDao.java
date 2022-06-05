package com.franky.movie.dao.common;
/**
 * 地域信息管理数据库操作层
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.franky.movie.entity.common.Area;
@Repository
public interface AreaDao extends JpaRepository<Area, Long> {
	
	@Query("select a from Area a where a.id = :id")
	Area find(@Param("id")Long id);
	
	//获取所有的省份
	List<Area> findByProvinceIdIsNull();
	//获取所有指定状态的省份
	List<Area> findByIsShowAndProvinceIdIsNull(Boolean isShow);
	//获取所有的城市
	List<Area> findByProvinceIdNotNullAndCityIdIsNull();
	//获取所有指定状态的城市
	List<Area> findByIsShowAndProvinceIdNotNullAndCityIdIsNull(Boolean isShow);
	//获取指定省份下的所有城市列表
	List<Area> findByProvinceIdAndCityIdIsNull(Long provinceId);
	//获取所有的区
	List<Area> findByProvinceIdNotNullAndCityIdNotNull();
	
	//获取指定城市所有的区
	List<Area> findByCityId(Long cityId);
}
