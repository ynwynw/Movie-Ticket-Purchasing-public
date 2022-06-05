package com.franky.movie.dao.common;
/**
 * 新闻信息管理数据库操作层
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.franky.movie.entity.common.News;
@Repository
public interface NewsDao extends JpaRepository<News, Long> {
	
	@Query("select n from News n where n.id = :id")
	News find(@Param("id")Long id);
	
	List<News> findTop2ByOrderByViewNumberDesc();
	
	List<News> findByNewsCategoryId(Long newsCategoryId);
}
