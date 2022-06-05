package com.franky.movie.service.common;

import java.math.BigDecimal;
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;

import com.franky.movie.bean.PageBean;
import com.franky.movie.dao.common.MovieCommentDao;
import com.franky.movie.entity.common.Movie;
import com.franky.movie.entity.common.MovieComment;

/**
 * 电影评价信息service层
 * @author frank
 *
 */
@Service
public class MovieCommentService {
	
	@Autowired
	private MovieCommentDao movieCommentDao;
	@Autowired
	private MovieService movieService;
	/**
	 * 当movieComment的id不为空时进行编辑，当id为空时则进行添加
	 * @param movieComment
	 * @return
	 */
	public MovieComment save(MovieComment movieComment){
		return movieCommentDao.save(movieComment);
	}
	
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public MovieComment findById(Long id){
		return movieCommentDao.getOne(id);
	}
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(Long id){
		movieCommentDao.deleteById(id);
	}
	
	/**
	 * 分页查找电影评价列表信息
	 * @param movieComment
	 * @param pageBean
	 * @return
	 */
	public PageBean<MovieComment> findPage(MovieComment movieComment,PageBean<MovieComment> pageBean){
		ExampleMatcher withMatcher = ExampleMatcher.matching().withMatcher("content", GenericPropertyMatchers.contains());
		//withMatcher = withMatcher.withIgnorePaths("isShow");
		Example<MovieComment> example = Example.of(movieComment, withMatcher);
		Sort sort = Sort.by(Direction.DESC, "createTime");
		Pageable pageable = PageRequest.of(pageBean.getCurrentPage()-1, pageBean.getPageSize(),sort);
		Page<MovieComment> findAll = movieCommentDao.findAll(example, pageable);
		pageBean.setContent(findAll.getContent());
		pageBean.setTotal(findAll.getTotalElements());
		pageBean.setTotalPage(findAll.getTotalPages());
		return pageBean;
	}
	
	/**
	 * 获取所有的电影评价列表
	 * @return
	 */
	public List<MovieComment> findAll(){
		return movieCommentDao.findAll();
	}
	
	/**
	 * 根据用户id查找
	 * @param accountId
	 * @return
	 */
	public List<MovieComment> findByAccount(Long accountId){
		return movieCommentDao.findByAccountIdOrderByCreateTimeDesc(accountId);
	}
	
	/**
	 * 根据电影id查找
	 * @param movieId
	 * @return
	 */
	public List<MovieComment> findByMovie(Long movieId){
		return movieCommentDao.findByMovieIdOrderByCreateTimeDesc(movieId);
	}
	
	/**
	 * 添加评论，放到一个事物中，防止出现错误后不会滚
	 * @param movieComment
	 */
	@Transactional
	public MovieComment addComment(MovieComment movieComment){
		//将评价内容插入到数据库
		movieComment = save(movieComment);
		//重新计算电影的平均评分并更新
		Movie movie = movieService.findById(movieComment.getMovie().getId());
		//先计算出总的评分
		BigDecimal totalRate = movie.getRate().multiply(new BigDecimal(movie.getRateCount()));
		movie.setRateCount(movie.getRateCount()+1);
		BigDecimal newRate = totalRate.add(movieComment.getRate()).divide(new BigDecimal(movie.getRateCount()),2);
		movie.setRate(newRate);
		movieService.save(movie);
		movieComment.setMovie(movie);
		return movieComment;
	}
}
