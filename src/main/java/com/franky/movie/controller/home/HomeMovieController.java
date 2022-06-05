package com.franky.movie.controller.home;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.franky.movie.bean.Result;
import com.franky.movie.entity.common.CinemaHallSession;
import com.franky.movie.entity.common.Order;
import com.franky.movie.service.common.CinemaHallSeatService;
import com.franky.movie.service.common.CinemaHallSessionService;
import com.franky.movie.service.common.CinemaService;
import com.franky.movie.service.common.MovieCommentService;
import com.franky.movie.service.common.MovieService;
import com.franky.movie.service.common.NewsService;
import com.franky.movie.service.common.OrderItemService;
import com.franky.movie.service.common.OrderService;
/**
 * 前台电影控制器
 * @author frank
 *
 */
@RequestMapping("/home/movie")
@Controller
public class HomeMovieController {

	@Autowired
	private MovieService movieService;
	@Autowired
	private MovieCommentService movieCommentService;
	@Autowired
	private NewsService newsService;
	@Autowired
	private CinemaHallSessionService cinemaHallSessionService;
	@Autowired
	private CinemaService cinemaService;
	@Autowired
	private CinemaHallSeatService cinemaHallSeatService;
	@Value("${movie.select.seat.max.num}")
	private Integer selectedSeatMax;//最大选座数
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;
	/**
	 * 电影列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	public String list(Model model,@RequestParam(name="type",defaultValue="0") Integer type){
		model.addAttribute("movieList", type == 0 ? movieService.findShowList() : movieService.findFutureList());
		model.addAttribute("type", type);
		model.addAttribute("topNewsList", newsService.findTop());
		model.addAttribute("topMoneyMovieList", movieService.findTopMoneyList());
		return "home/movie/list";
	}
	
	/**
	 * 电影详情页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(Model model,@RequestParam(name="id",required=true) Long id){
		model.addAttribute("movie", movieService.findById(id));
		model.addAttribute("topMovieList", movieService.findTopList(5));
		model.addAttribute("distinctCinemaHallSessionList", cinemaHallSessionService.findDistinctCinemaByMovieId(id));
		model.addAttribute("distinctShowDateCinemaHallSessionList", cinemaHallSessionService.findDistinctShowDateByMovieId(id));
		model.addAttribute("commentList", movieCommentService.findByMovie(id));
		return "home/movie/detail";
	}
	
	/**
	 * 选座页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/select_seat")
	public String selectSeat(Model model,@RequestParam(name="id",required=true) Long id){
		CinemaHallSession cinemaHallSession = cinemaHallSessionService.findById(id);
		model.addAttribute("cinemaHallSession", cinemaHallSession);
		model.addAttribute("cinemaHallSeatList", cinemaHallSeatService.findAll(cinemaHallSession.getCinemaHall().getId()));
		model.addAttribute("selectedSeatMax", selectedSeatMax);
		List<Order> findByCinemaHallSession = orderService.findByCinemaHallSession(id);
		model.addAttribute("orderSeatList", JSONArray.toJSONString(orderItemService.findOrderItemSeatIds(findByCinemaHallSession)));
		return "home/movie/select_seat";
	}
	
	/**
	 * 获取指定电影、指定影院、指定时间下的场次
	 * @param model
	 * @param mid
	 * @param cid
	 * @param showDate
	 * @return
	 */
	@RequestMapping("/get_show_session")
	public String getShowSession(Model model,
			@RequestParam(name="mid",required=true) Long mid,
			@RequestParam(name="cid",required=true) Long cid,
			@RequestParam(name="showDate",required=true) String showDate){
		model.addAttribute("cinemaHallSessionList", cinemaHallSessionService.findByMovieIdAndCinemaIdAndShowDate(mid, cid, showDate));
		return "home/movie/get_show_session";
	}
	
	/**
	 * 统计电影上映场次
	 * @param movieId
	 * @return
	 */
	@RequestMapping("/get_show_stats")
	@ResponseBody
	public Result<List<Integer>> getShowStats(@RequestParam(name="mid",required=true) Long movieId) {
		List<Integer> ret = new ArrayList<Integer>();
		List<Integer> showTotal = cinemaHallSessionService.getShowTotal(movieId);
		if(showTotal == null || showTotal.size() <= 0){
			ret.add(0);
			ret.add(0);
			return Result.success(ret);
		}
		ret.add(showTotal.size());//上映的影院数
		//计算场次数
		int totalSession = 0;
		for(int i=0; i< showTotal.size();i++){
			totalSession += Integer.parseInt(showTotal.get(i)+"");
		}
		ret.add(totalSession);
		return Result.success(ret);
	}
}
