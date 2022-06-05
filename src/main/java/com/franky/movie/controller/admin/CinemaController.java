package com.franky.movie.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.franky.movie.bean.CinemaServices;
import com.franky.movie.bean.CodeMsg;
import com.franky.movie.bean.PageBean;
import com.franky.movie.bean.Result;
import com.franky.movie.entity.common.Area;
import com.franky.movie.entity.common.Cinema;
import com.franky.movie.entity.common.CinemaComment;
import com.franky.movie.service.common.AreaService;
import com.franky.movie.service.common.CinemaCommentService;
import com.franky.movie.service.common.CinemaService;
import com.franky.movie.util.ValidateEntityUtil;

/**
 * 电影院管理控制器
 * @author frank
 *
 */
@RequestMapping("/admin/cinema")
@Controller
public class CinemaController {

	@Autowired
	private CinemaService cinemaService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private CinemaCommentService cinemaCommentService;
	
	/**
	 * 电影院列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(Model model,Cinema cinema,PageBean<Cinema> pageBean){
		model.addAttribute("pageBean", cinemaService.findPage(cinema, pageBean));
		model.addAttribute("name",cinema.getName());
		return "admin/cinema/list";
	}
	
	/**
	 * 电影院添加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(Model model){
		model.addAttribute("provinceList", areaService.getAllProvince());
		model.addAttribute("cinemaServiceList", CinemaServices.values());
		return "admin/cinema/add";
	}
	
	/**
	 * 电影院编辑页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(Model model,@RequestParam(name="id",required=true)Long id){
		Cinema cinema = cinemaService.findById(id);
		model.addAttribute("cinema", cinema);
		model.addAttribute("provinceList", areaService.getAllProvince());
		model.addAttribute("cityList", areaService.getAllCity(cinema.getArea().getProvinceId()));
		model.addAttribute("districtList", areaService.getAllDistrict(cinema.getArea().getCityId()));
		model.addAttribute("cinemaServiceList", CinemaServices.values());
		return "admin/cinema/edit";
	}
	
	/**
	 * 添加电影院表单提交
	 * @param cinema
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> add(Cinema cinema){
		if(cinema == null){
			return Result.error(CodeMsg.DATA_ERROR);
		}
		CodeMsg validate = ValidateEntityUtil.validate(cinema);
		if(validate.getCode() != CodeMsg.SUCCESS.getCode()){
			return Result.error(validate);
		}
		//判断是否是编辑
		if(cinema.getId() != null && cinema.getId() > 0){
			Cinema findById = cinemaService.findById(cinema.getId());
			cinema.setCreateTime(findById.getCreateTime());
			cinema.setRate(findById.getRate());
			cinema.setRateCount(findById.getRateCount());
		}
		if(cinema.getArea() != null){
			Area province = areaService.findById(cinema.getArea().getProvinceId());
			Area city = areaService.findById(cinema.getArea().getCityId());
			String address = "";
			if(province != null){
				address = province.getName();
			}
			if(city != null){
				address += city.getName();
			}
			cinema.setAddress(address+cinema.getAddress());
		}
		//表示数据合法，可以保存到数据库
		if(cinemaService.save(cinema) == null){
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
			cinemaService.delete(id);
		} catch (Exception e) {
			return Result.error(CodeMsg.ADMIN_CINEMA_DELETE_ERROR);
		}
		return Result.success(true);
	}
	
	/**
	 * ----------影院评价管理-------------
	 */
	/**
	 * 影院评价列表
	 * @param model
	 * @param cinemaComment
	 * @param pageBean
	 * @return
	 */
	@RequestMapping(value="/comment_list")
	public String list(Model model,CinemaComment cinemaComment,PageBean<CinemaComment> pageBean){
		model.addAttribute("pageBean", cinemaCommentService.findPage(cinemaComment, pageBean));
		model.addAttribute("content",cinemaComment.getContent());
		return "admin/cinema/comment_list";
	}
	
	/**
	 * 删除评价
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete_comment",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> deleteComment(@RequestParam(name="id",required=true)Long id){
		cinemaCommentService.delete(id);
		return Result.success(true);
	}
}
