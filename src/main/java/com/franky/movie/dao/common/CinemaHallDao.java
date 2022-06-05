package com.franky.movie.dao.common;
/**
 * 影厅信息管理数据库操作层
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.franky.movie.entity.common.CinemaHall;
@Repository
public interface CinemaHallDao extends JpaRepository<CinemaHall, Long> {
	
	List<CinemaHall> findByCinemaId(Long cinemaId);
}
