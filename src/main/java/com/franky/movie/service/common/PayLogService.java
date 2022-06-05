package com.franky.movie.service.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.franky.movie.bean.PageBean;
import com.franky.movie.bean.PayLogStats;
import com.franky.movie.dao.common.PayLogDao;
import com.franky.movie.entity.common.PayLog;
import com.franky.movie.util.StringUtil;

/**
 * 支付记录信息service层
 * @author frank
 *
 */
@Service
public class PayLogService {
	
	@Autowired
	private PayLogDao payLogDao;
	
	/**
	 * 当payLog的id不为空时进行编辑，当id为空时则进行添加
	 * @param payLog
	 * @return
	 */
	public PayLog save(PayLog payLog){
		return payLogDao.save(payLog);
	}
	
	/**
	 * 根据编号查找
	 * @param sn
	 * @return
	 */
	public PayLog find(String sn){
		return payLogDao.findBySn(sn);
	}
	
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public PayLog findById(Long id){
		return payLogDao.find(id);
	}
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(Long id){
		payLogDao.deleteById(id);
	}
	
	/**
	 * 根据用户id查询
	 * @param accountId
	 * @return
	 */
	public List<PayLog> findAll(Long accountId){
		return payLogDao.findByAccountIdOrderByUpdateTimeDesc(accountId);
	}
	
	/**
	 * 搜索制定时间内的支付记录
	 * @param createTime
	 * @return
	 */
	public List<PayLog> findAll(Date createTime){
		return payLogDao.findByCreateTimeGreaterThan(createTime);
	}
	
	/**
	 * 分页查找支付记录
	 * @param payLog
	 * @param pageBean
	 * @return
	 */
	public PageBean<PayLog> findPage(PayLog payLog,PageBean<PayLog> pageBean){
		ExampleMatcher withMatcher = ExampleMatcher.matching().withMatcher("sn", GenericPropertyMatchers.contains());
		withMatcher = withMatcher.withIgnorePaths("status");
		Example<PayLog> example = Example.of(payLog, withMatcher);
		Sort sort = Sort.by(Direction.DESC, "updateTime");
		Pageable pageable = PageRequest.of(pageBean.getCurrentPage()-1, pageBean.getPageSize(),sort);
		Page<PayLog> findAll = payLogDao.findAll(example, pageable);
		pageBean.setContent(findAll.getContent());
		pageBean.setTotal(findAll.getTotalElements());
		pageBean.setTotalPage(findAll.getTotalPages());
		return pageBean;
	}
	
	/**
	 * 返回总记录数
	 * @return
	 */
	public long count(){
		return payLogDao.count();
	}
	
	/**
	 * 统计支付成功数
	 * @return
	 */
	public long countPaySuccess(){
		return payLogDao.countByStatus(PayLog.status_paid);
	}
	
	/**
	 * 统计总的支付记录数
	 * @param beforeDays
	 * @return
	 */
	public List<PayLogStats> statsAll(int beforeDays){
		return statsByStatus(beforeDays, -1, false);
	}
	
	/**
	 * 统计已支付的记录数
	 * @param beforeDays
	 * @return
	 */
	public List<PayLogStats> statsPaid(int beforeDays){
		List<PayLogStats> statsAll = statsByStatus(beforeDays, -1, false);
		List<PayLogStats> statsPaid = statsByStatus(beforeDays, PayLog.status_paid, true);
		List<PayLogStats> statsPaidNew = new ArrayList<PayLogStats>();
		for(PayLogStats payLogStatsAll : statsAll){
			boolean flag = true;
			for(PayLogStats payLogStatsPaid : statsPaid){
				if(payLogStatsAll.getDay().equals(payLogStatsPaid.getDay())){
					statsPaidNew.add(payLogStatsPaid);
					flag = true;
					break;
				}
				flag = false;
			}
			if(!flag){
				PayLogStats payLogStats = new PayLogStats();
				payLogStats.setDay(payLogStatsAll.getDay());
				payLogStats.setNum(0);
				statsPaidNew.add(payLogStats);
			}
		}
		return statsPaidNew;
	}
	
	public List<PayLogStats> statsByStatus(int beforeDays,int status,boolean isStatus){
		List<PayLog> findAll = findAll(StringUtil.getBeforeDaysDate(new Date(), beforeDays));
		Map<String, Integer> xLabel = new TreeMap<String, Integer>();
		for(PayLog payLog : findAll){
			String day = StringUtil.getFormatterDate(payLog.getCreateTime(), "yyyy-MM-dd");
			if(isStatus){
				if(status == payLog.getStatus()){
					setStatsData(xLabel, day);
				}
			}else{
				setStatsData(xLabel, day);
			}
		}
		List<PayLogStats> payLogStatsList = new ArrayList<PayLogStats>();
		for(Entry<String, Integer> entry : xLabel.entrySet()){
			PayLogStats payLogStats = new PayLogStats();
			payLogStats.setDay(entry.getKey());
			payLogStats.setNum(entry.getValue());
			payLogStatsList.add(payLogStats);
		}
		return payLogStatsList;
	}
	
	private void setStatsData(Map<String, Integer> xLabel,String day){
		Integer integer = xLabel.get(day);
		if(integer == null){
			xLabel.put(day, 1);
		}else{
			xLabel.put(day, integer + 1);
		}
	}
}
