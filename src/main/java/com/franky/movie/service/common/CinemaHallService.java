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
import com.franky.movie.dao.common.CinemaHallDao;
import com.franky.movie.entity.common.CinemaHall;

/**
 * 影厅信息service层
 * @author frank
 *
 */
@Service
public class CinemaHallService {
	
	@Autowired
	private CinemaHallDao cinemaHallDao;
	
	/**
	 * 当cinemaHall的id不为空时进行编辑，当id为空时则进行添加
	 * @param cinemaHall
	 * @return
	 */
	public CinemaHall save(CinemaHall cinemaHall){
		return cinemaHallDao.save(cinemaHall);
	}
	
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public CinemaHall findById(Long id){
		return cinemaHallDao.getOne(id);
	}
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(Long id){
		cinemaHallDao.deleteById(id);
	}
	
	/**
	 * 分页查找影厅列表信息
	 * @param cinemaHall
	 * @param pageBean
	 * @return
	 */
	public PageBean<CinemaHall> findPage(CinemaHall cinemaHall,PageBean<CinemaHall> pageBean){
		ExampleMatcher withMatcher = ExampleMatcher.matching().withMatcher("name", GenericPropertyMatchers.contains());
		//withMatcher = withMatcher.withIgnorePaths("status","sex");
		Example<CinemaHall> example = Example.of(cinemaHall, withMatcher);
		Pageable pageable = PageRequest.of(pageBean.getCurrentPage()-1, pageBean.getPageSize());
		Page<CinemaHall> findAll = cinemaHallDao.findAll(example, pageable);
		pageBean.setContent(findAll.getContent());
		pageBean.setTotal(findAll.getTotalElements());
		pageBean.setTotalPage(findAll.getTotalPages());
		return pageBean;
	}
	
	/**
	 * 获取所有的影院列表
	 * @return
	 */
	public List<CinemaHall> findAll(){
		return cinemaHallDao.findAll();
	}
	
	/**
	 * 查询某个影院下的所有影厅
	 * @param cinemaId
	 * @return
	 */
	public List<CinemaHall> findAll(Long cinemaId){
		return cinemaHallDao.findByCinemaId(cinemaId);
	}
}
