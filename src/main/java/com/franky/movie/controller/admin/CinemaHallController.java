package com.franky.movie.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.franky.movie.bean.CodeMsg;
import com.franky.movie.bean.PageBean;
import com.franky.movie.bean.Result;
import com.franky.movie.entity.common.CinemaHall;
import com.franky.movie.entity.common.CinemaHallSeat;
import com.franky.movie.service.common.CinemaHallSeatService;
import com.franky.movie.service.common.CinemaHallService;
import com.franky.movie.service.common.CinemaService;
import com.franky.movie.util.ValidateEntityUtil;

/**
 * 影厅管理控制器
 * @author frank
 *
 */
@RequestMapping("/admin/cinema_hall")
@Controller
public class CinemaHallController {

	@Autowired
	private CinemaService cinemaService;
	@Autowired
	private CinemaHallService cinemaHallService;
	@Autowired
	private CinemaHallSeatService cinemaHallSeatService;
	
	/**
	 * 影厅列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(Model model,CinemaHall cinemaHall,PageBean<CinemaHall> pageBean){
		model.addAttribute("pageBean", cinemaHallService.findPage(cinemaHall, pageBean));
		model.addAttribute("name",cinemaHall.getName());
		return "admin/cinema_hall/list";
	}
	
	/**
	 * 影厅添加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(Model model){
		model.addAttribute("cinemaList", cinemaService.findAll());
		return "admin/cinema_hall/add";
	}
	
	/**
	 * 影厅编辑页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(Model model,@RequestParam(name="id",required=true)Long id){
		model.addAttribute("cinemaHall", cinemaHallService.findById(id));
		model.addAttribute("cinemaList", cinemaService.findAll());
		return "admin/cinema_hall/edit";
	}
	
	/**
	 * 添加影厅表单提交
	 * @param cinemaHall
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> add(CinemaHall cinemaHall){
		if(cinemaHall == null){
			return Result.error(CodeMsg.DATA_ERROR);
		}
		CodeMsg validate = ValidateEntityUtil.validate(cinemaHall);
		if(validate.getCode() != CodeMsg.SUCCESS.getCode()){
			return Result.error(validate);
		}
		//判断是否是编辑
		if(cinemaHall.getId() != null && cinemaHall.getId() > 0){
			CinemaHall findById = cinemaHallService.findById(cinemaHall.getId());
			cinemaHall.setCreateTime(findById.getCreateTime());
			cinemaHall.setMaxX(findById.getMaxX());
			cinemaHall.setMaxY(findById.getMaxY());
		}
		//表示数据合法，可以保存到数据库
		if(cinemaHallService.save(cinemaHall) == null){
			return Result.error(CodeMsg.ADMIN_CINEMA_SAVE_ERROR);
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
			cinemaHallService.delete(id);
		} catch (Exception e) {
			return Result.error(CodeMsg.ADMIN_CINEMA_HALL_DELETE_ERROR);
		}
		return Result.success(true);
	}
	
	/**
	 * 编辑/生成影厅座位
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit_seat",method=RequestMethod.GET)
	public String editSeat(Model model,@RequestParam(name="id",required=true)Long id){
		CinemaHall cinemaHall = cinemaHallService.findById(id);
		model.addAttribute("cinemaHall", cinemaHall);
		List<CinemaHallSeat> findAll = cinemaHallSeatService.findAll(cinemaHall.getId());
		model.addAttribute("cinemaHallSeats", findAll.size() > 0 ? findAll : null);
		return "admin/cinema_hall/edit_seat";
	}
	
	/**
	 * 座位保存
	 * @param seats
	 * @return
	 */
	@RequestMapping(value="/edit_seat",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> editSeat(@RequestParam(name="seats",required=true)String seats){
		List<CinemaHallSeat> seatList = JSONObject.parseArray(seats, CinemaHallSeat.class);
		cinemaHallSeatService.saveAll(seatList);
		return Result.success(true);
	}
	
	/**
	 * 根据影院id查找影厅
	 * @param cid
	 * @return
	 */
	@RequestMapping(value="/get_cinema_halls",method=RequestMethod.POST)
	@ResponseBody
	public Result<List<CinemaHall>> getCinemaHalls(@RequestParam(name="cid",required=true)Long cid){
		return Result.success(cinemaHallService.findAll(cid));
	}
}
