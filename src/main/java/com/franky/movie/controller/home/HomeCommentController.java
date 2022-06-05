package com.franky.movie.controller.home;
/**
 * 评价统一入口控制器
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.franky.movie.bean.Result;
import com.franky.movie.constant.SessionConstant;
import com.franky.movie.entity.common.Account;
import com.franky.movie.entity.common.CinemaComment;
import com.franky.movie.entity.common.MovieComment;
import com.franky.movie.service.common.CinemaCommentService;
import com.franky.movie.service.common.MovieCommentService;
import com.franky.movie.util.SessionUtil;

@RequestMapping("/home/comment")
@Controller
public class HomeCommentController {
	
	
	@Autowired
	private MovieCommentService movieCommentService;
	@Autowired
	private CinemaCommentService cinemaCommentService;
	
	
	private Logger log = LoggerFactory.getLogger(HomeCommentController.class);
	
	
	/**
	 * 提交电影评价
	 * @param movieComment
	 * @return
	 */
	@RequestMapping(value="/comment_movie",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> commentMovie(MovieComment movieComment){
		Account account = (Account)SessionUtil.get(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY);
		movieComment.setAccount(account);
		movieComment = movieCommentService.addComment(movieComment);
		log.info("评价成功，评价完后，电影【" + movieComment.getMovie().getName() + "】的最新评分为【" + movieComment.getMovie().getRate() + "】");
		return Result.success(true);
	}
	
	/**
	 * 提交影厅评价
	 * @param cinemaComment
	 * @return
	 */
	@RequestMapping(value="/comment_cinema",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> commentCinema(CinemaComment cinemaComment){
		Account account = (Account)SessionUtil.get(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY);
		cinemaComment.setAccount(account);
		cinemaComment = cinemaCommentService.addComment(cinemaComment);
		log.info("评价成功，评价完后，影院【" + cinemaComment.getCinema().getName() + "】的最新评分为【" + cinemaComment.getCinema().getRate() + "】");
		return Result.success(true);
	}
}
