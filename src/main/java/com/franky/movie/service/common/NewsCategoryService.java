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
import com.franky.movie.dao.common.NewsCategoryDao;
import com.franky.movie.entity.common.NewsCategory;

/**
 * 新闻分类信息service层
 * @author frank
 *
 */
@Service
public class NewsCategoryService {
	
	@Autowired
	private NewsCategoryDao newsCategoryDao;
	
	/**
	 * 当newsCategory的id不为空时进行编辑，当id为空时则进行添加
	 * @param newsCategory
	 * @return
	 */
	public NewsCategory save(NewsCategory newsCategory){
		return newsCategoryDao.save(newsCategory);
	}
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public NewsCategory find(Long id){
		return newsCategoryDao.find(id);
	}
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(Long id){
		newsCategoryDao.deleteById(id);
	}
	
	/**
	 * 分页查找分类信息
	 * @param newsCategory
	 * @param pageBean
	 * @return
	 */
	public PageBean<NewsCategory> findPage(NewsCategory newsCategory,PageBean<NewsCategory> pageBean){
		ExampleMatcher withMatcher = ExampleMatcher.matching().withMatcher("name", GenericPropertyMatchers.contains());
		//withMatcher = withMatcher.withIgnorePaths("status","sex");
		Example<NewsCategory> example = Example.of(newsCategory, withMatcher);
		Pageable pageable = PageRequest.of(pageBean.getCurrentPage()-1, pageBean.getPageSize());
		Page<NewsCategory> findAll = newsCategoryDao.findAll(example, pageable);
		pageBean.setContent(findAll.getContent());
		pageBean.setTotal(findAll.getTotalElements());
		pageBean.setTotalPage(findAll.getTotalPages());
		return pageBean;
	}

	/**
	 * 获取所有的分类列表
	 * @return
	 */
	public List<NewsCategory> findAll() {
		// TODO Auto-generated method stub
		return newsCategoryDao.findAll();
	}
}
