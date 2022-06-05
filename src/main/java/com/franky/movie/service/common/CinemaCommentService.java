package com.franky.movie.service.common;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.franky.movie.bean.PageBean;
import com.franky.movie.dao.common.CinemaCommentDao;
import com.franky.movie.entity.common.Cinema;
import com.franky.movie.entity.common.CinemaComment;

/**
 * 影院评价信息service层
 * @author frank
 *
 */
@Service
public class CinemaCommentService {
	
	@Autowired
	private CinemaCommentDao cinemaCommentDao;
	@Autowired
	private CinemaService cinemaService;
	/**
	 * 当cinemaComment的id不为空时进行编辑，当id为空时则进行添加
	 * @param cinemaComment
	 * @return
	 */
	public CinemaComment save(CinemaComment cinemaComment){
		return cinemaCommentDao.save(cinemaComment);
	}
	
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public CinemaComment findById(Long id){
		return cinemaCommentDao.getOne(id);
	}
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(Long id){
		cinemaCommentDao.deleteById(id);
	}
	
	/**
	 * 分页查找影院评价列表信息
	 * @param cinemaComment
	 * @param pageBean
	 * @return
	 */
	public PageBean<CinemaComment> findPage(CinemaComment cinemaComment,PageBean<CinemaComment> pageBean){
		ExampleMatcher withMatcher = ExampleMatcher.matching().withMatcher("content", GenericPropertyMatchers.contains());
		//withMatcher = withMatcher.withIgnorePaths("isShow");
		Example<CinemaComment> example = Example.of(cinemaComment, withMatcher);
		Sort sort = Sort.by(Direction.DESC, "createTime");
		Pageable pageable = PageRequest.of(pageBean.getCurrentPage()-1, pageBean.getPageSize(),sort);
		Page<CinemaComment> findAll = cinemaCommentDao.findAll(example, pageable);
		pageBean.setContent(findAll.getContent());
		pageBean.setTotal(findAll.getTotalElements());
		pageBean.setTotalPage(findAll.getTotalPages());
		return pageBean;
	}
	
	/**
	 * 获取所有的影院评价列表
	 * @return
	 */
	public List<CinemaComment> findAll(){
		return cinemaCommentDao.findAll();
	}
	
	/**
	 * 根据用户id查找
	 * @param accountId
	 * @return
	 */
	public List<CinemaComment> findByAccount(Long accountId){
		return cinemaCommentDao.findByAccountIdOrderByCreateTimeDesc(accountId);
	}
	
	/**
	 * 根据影院id查找
	 * @param cinemaId
	 * @return
	 */
	public List<CinemaComment> findByCinema(Long cinemaId){
		return cinemaCommentDao.findByCinemaIdOrderByCreateTimeDesc(cinemaId);
	}
	
	/**
	 * 添加评论，放到一个事物中，防止出现错误后不会滚
	 * @param cinemaComment
	 */
	@Transactional
	public CinemaComment addComment(CinemaComment cinemaComment){
		//将评价内容插入到数据库
		cinemaComment = save(cinemaComment);
		//重新计算影院的平均评分并更新
		Cinema cinema = cinemaService.findById(cinemaComment.getCinema().getId());
		//先计算出总的评分
		BigDecimal totalRate = cinema.getRate().multiply(new BigDecimal(cinema.getRateCount()));
		cinema.setRateCount(cinema.getRateCount()+1);
		BigDecimal newRate = totalRate.add(cinemaComment.getRate()).divide(new BigDecimal(cinema.getRateCount()),2);
		cinema.setRate(newRate);
		cinemaService.save(cinema);
		cinemaComment.setCinema(cinema);
		return cinemaComment;
	}
}
