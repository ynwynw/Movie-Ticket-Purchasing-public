package com.franky.movie.dao.common;
/**
 * 影院评价信息管理数据库操作层
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.franky.movie.entity.common.CinemaComment;
@Repository
public interface CinemaCommentDao extends JpaRepository<CinemaComment, Long> {
	
	List<CinemaComment> findByAccountIdOrderByCreateTimeDesc(Long accountId);
	
	List<CinemaComment> findByCinemaIdOrderByCreateTimeDesc(Long cinemaId);
	
}
