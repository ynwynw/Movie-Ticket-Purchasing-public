package com.franky.movie.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.franky.movie.bean.PageBean;
import com.franky.movie.dao.common.CinemaDao;
import com.franky.movie.entity.common.Cinema;

/**
 * 电影院信息service层
 * @author frank
 *
 */
@Service
public class CinemaService {
	
	@Autowired
	private CinemaDao cinemaDao;
	
	/**
	 * 当cinema的id不为空时进行编辑，当id为空时则进行添加
	 * @param cinema
	 * @return
	 */
	public Cinema save(Cinema cinema){
		return cinemaDao.save(cinema);
	}
	
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public Cinema findById(Long id){
		return cinemaDao.getOne(id);
	}
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(Long id){
		cinemaDao.deleteById(id);
	}
	
	/**
	 * 分页查找电影院列表信息
	 * @param cinema
	 * @param pageBean
	 * @return
	 */
	public PageBean<Cinema> findPage(Cinema cinema,PageBean<Cinema> pageBean){
		ExampleMatcher withMatcher = ExampleMatcher.matching().withMatcher("name", GenericPropertyMatchers.contains());
		withMatcher = withMatcher.withIgnorePaths("rate","rateCount");
		Example<Cinema> example = Example.of(cinema, withMatcher);
		Pageable pageable = PageRequest.of(pageBean.getCurrentPage()-1, pageBean.getPageSize());
		Page<Cinema> findAll = cinemaDao.findAll(example, pageable);
		pageBean.setContent(findAll.getContent());
		pageBean.setTotal(findAll.getTotalElements());
		pageBean.setTotalPage(findAll.getTotalPages());
		return pageBean;
	}
	
	/**
	 * 获取所有的影院列表
	 * @return
	 */
	public List<Cinema> findAll(){
		return cinemaDao.findAll();
	}
	
	/**
	 * 获取指定地域下的所有影院
	 * @param areaId
	 * @return
	 */
	public List<Cinema> findAll(Long areaId){
		return cinemaDao.findByAreaCityId(areaId);
	}
	
	/**
	 * 获取热门影院列表
	 * @return
	 */
	public List<Cinema> findTopList(){
		return cinemaDao.findTop6ByOrderByRateDesc();
	}
	
	/**
	 * 返回影院总数
	 * @return
	 */
	public long count(){
		return cinemaDao.count();
	}
}
