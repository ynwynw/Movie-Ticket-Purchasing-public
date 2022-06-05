package com.franky.movie.dao.common;
/**
 * 支付记录信息管理数据库操作层
 */
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.franky.movie.entity.common.PayLog;
@Repository
public interface PayLogDao extends JpaRepository<PayLog, Long> {
	
	@Query("select pl from PayLog pl where pl.id = :id")
	PayLog find(@Param("id")Long id);
	
	PayLog findBySn(String sn);
	
	List<PayLog> findByAccountIdOrderByUpdateTimeDesc(Long accountId);
	
	List<PayLog> findByCreateTimeGreaterThan(Date createTime);
	
	Long countByStatus(Integer status);
}
