package com.franky.movie.dao.common;
/**
 * 电影评价信息管理数据库操作层
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.franky.movie.entity.common.MovieComment;
@Repository
public interface MovieCommentDao extends JpaRepository<MovieComment, Long> {
	
	List<MovieComment> findByAccountIdOrderByCreateTimeDesc(Long accountId);
	
	List<MovieComment> findByMovieIdOrderByCreateTimeDesc(Long movieId);
	
}
