package com.franky.movie.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.franky.movie.dao.common.AreaDao;
import com.franky.movie.entity.common.Area;

/**
 * 地域信息service层
 * @author frank
 *
 */
@Service
public class AreaService {
	
	@Autowired
	private AreaDao areaDao;
	
	/**
	 * 当area的id不为空时进行编辑，当id为空时则进行添加
	 * @param area
	 * @return
	 */
	public Area save(Area area){
		return areaDao.save(area);
	}
	
	/**
	 * 获取所有的省份列表
	 * @return
	 */
	public List<Area> getAllProvince(){
		return areaDao.findByProvinceIdIsNull();
	}
	
	/**
	 * 获取所有的可见省份列表
	 * @return
	 */
	public List<Area> getAllEnableProvince(){
		return areaDao.findByIsShowAndProvinceIdIsNull(true);
	}
	
	/**
	 * 获取所有的城市列表
	 * @return
	 */
	public List<Area> getAllCity(){
		return areaDao.findByProvinceIdNotNullAndCityIdIsNull();
	}
	
	/**
	 * 获取所有的可用城市列表
	 * @return
	 */
	public List<Area> getAllEnableCity(){
		return areaDao.findByIsShowAndProvinceIdNotNullAndCityIdIsNull(true);
	}
	
	/**
	 * 获取指定省份的所有城市列表
	 * @param provinceId
	 * @return
	 */
	public List<Area> getAllCity(Long provinceId){
		return areaDao.findByProvinceIdAndCityIdIsNull(provinceId);
	}
	
	/**
	 * 获取所有的区列表
	 * @return
	 */
	public List<Area> getAllDistrict(){
		return areaDao.findByProvinceIdNotNullAndCityIdNotNull();
	}
	
	/**
	 * 获取指定城市所有的区列表
	 * @return
	 */
	public List<Area> getAllDistrict(Long cityId){
		return areaDao.findByCityId(cityId);
	}
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public Area findById(Long id){
		return areaDao.find(id);
	}
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(Long id){
		areaDao.deleteById(id);
	}
}
