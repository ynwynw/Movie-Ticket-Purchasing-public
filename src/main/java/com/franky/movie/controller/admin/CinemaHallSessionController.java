package com.franky.movie.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.franky.movie.bean.CinemaSessionType;
import com.franky.movie.bean.CodeMsg;
import com.franky.movie.bean.PageBean;
import com.franky.movie.bean.Result;
import com.franky.movie.entity.common.CinemaHallSession;
import com.franky.movie.service.common.CinemaHallService;
import com.franky.movie.service.common.CinemaHallSessionService;
import com.franky.movie.service.common.CinemaService;
import com.franky.movie.service.common.MovieService;
import com.franky.movie.util.StringUtil;
import com.franky.movie.util.ValidateEntityUtil;

/**
 * 排片场次管理控制器
 * @author frank
 *
 */
@RequestMapping("/admin/cinema_hall_session")
@Controller
public class CinemaHallSessionController {

	@Autowired
	private CinemaService cinemaService;
	@Autowired
	private CinemaHallService cinemaHallService;
	@Autowired
	private MovieService movieService;
	@Autowired
	private CinemaHallSessionService cinemaHallSessionService;
	
	/**
	 * 排片场次列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(Model model,CinemaHallSession cinemaHallSession,PageBean<CinemaHallSession> pageBean){
		model.addAttribute("pageBean", cinemaHallSessionService.findPage(cinemaHallSession, pageBean));
		model.addAttribute("showDate",cinemaHallSession.getShowDate());
		return "admin/cinema_hall_session/list";
	}
	
	/**
	 * 排片场次添加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(Model model){
		model.addAttribute("cinemaList", cinemaService.findAll());
		model.addAttribute("movieList", movieService.findAll());
		model.addAttribute("cinemaSessionTypeList", CinemaSessionType.values());
		return "admin/cinema_hall_session/add";
	}
	
	/**
	 * 排片场次编辑页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(Model model,@RequestParam(name="id",required=true)Long id){
		model.addAttribute("cinemaHallSession", cinemaHallSessionService.findById(id));
		model.addAttribute("movieList", movieService.findAll());
		model.addAttribute("cinemaList", cinemaService.findAll());
		model.addAttribute("cinemaSessionTypeList", CinemaSessionType.values());
		return "admin/cinema_hall_session/edit";
	}
	
	/**
	 * 添加排片场次表单提交
	 * @param cinemaHallSession
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> add(CinemaHallSession cinemaHallSession){
		if(cinemaHallSession == null){
			return Result.error(CodeMsg.DATA_ERROR);
		}
		CodeMsg validate = ValidateEntityUtil.validate(cinemaHallSession);
		if(validate.getCode() != CodeMsg.SUCCESS.getCode()){
			return Result.error(validate);
		}
		//判断是否是编辑
		if(cinemaHallSession.getId() != null && cinemaHallSession.getId() > 0){
			CinemaHallSession findById = cinemaHallSessionService.findById(cinemaHallSession.getId());
			cinemaHallSession.setCreateTime(findById.getCreateTime());
		}
		//生成散场时间
		String startTime = cinemaHallSession.getShowDate() + " " + cinemaHallSession.getShowTime();
		cinemaHallSession.setStartTime(startTime);
		cinemaHallSession.setEndTime(StringUtil.getFormatterDate(startTime, "yyyy-MM-dd HH:mm", Integer.valueOf(cinemaHallSession.getMovie().getTime())));
		if(cinemaHallSessionService.isExistHall(cinemaHallSession.getId(),cinemaHallSession.getCinemaHall().getId(), cinemaHallSession.getShowDate(), startTime,cinemaHallSession.getEndTime())){
			return Result.error(CodeMsg.ADMIN_CINEMA_HALL_SESSION_SAVE_EXIST);
		}
		//表示数据合法，可以保存到数据库
		if(cinemaHallSessionService.save(cinemaHallSession) == null){
			return Result.error(CodeMsg.ADMIN_CINEMA_HALL_SESSION_SAVE_ERROR);
		}
		return Result.success(true);
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> delete(@RequestParam(name="id",required=true)Long id){
		try {
			cinemaHallSessionService.delete(id);
		} catch (Exception e) {
			return Result.error(CodeMsg.ADMIN_CINEMA_HALL_SESSION_DELETE_ERROR);
		}
		return Result.success(true);
	}
	
	
}
