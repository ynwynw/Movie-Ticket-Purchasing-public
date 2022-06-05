package com.franky.movie.controller.admin;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.franky.movie.bean.CodeMsg;
import com.franky.movie.bean.Result;
import com.franky.movie.entity.common.Area;
import com.franky.movie.service.common.AreaService;

/**
 * 地域管理控制器
 * @author frank
 *
 */
@RequestMapping("/admin/area")
@Controller
public class AreaController {

	@Autowired
	private AreaService areaService;
	
	/**
	 * 地域列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(Model model){
		model.addAttribute("proviceList", areaService.getAllProvince());
		model.addAttribute("cityList", areaService.getAllCity());
		model.addAttribute("districtList", areaService.getAllDistrict());
		return "admin/area/list";
	}
	
	/**
	 * 地域添加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(Model model){
		model.addAttribute("proviceList", areaService.getAllProvince());
		return "admin/area/add";
	}
	
	/**
	 * 地域编辑页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(Model model,@RequestParam(name="id",required=true)Long id){
		model.addAttribute("proviceList", areaService.getAllProvince());
		model.addAttribute("cityList", areaService.getAllCity());
		model.addAttribute("area", areaService.findById(id));
		return "admin/area/edit";
	}
	
	/**
	 * 添加地域表单提交
	 * @param area
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> add(Area area){
		if(area == null){
			return Result.error(CodeMsg.DATA_ERROR);
		}
		if(StringUtils.isEmpty(area.getName())){
			return Result.error(CodeMsg.ADMIN_AREA_NAME_EMPTY);
		}
		//判断是否是编辑
		if(area.getId() != null && area.getId() > 0){
			Area findById = areaService.findById(area.getId());
			area.setCreateTime(findById.getCreateTime());
		}
		//表示数据合法，可以保存到数据库
		if(areaService.save(area) == null){
			return Result.error(CodeMsg.ADMIN_AREA_SAVE_ERROR);
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
			areaService.delete(id);
		} catch (Exception e) {
			return Result.error(CodeMsg.ADMIN_AREA_DELETE_ERROR);
		}
		return Result.success(true);
	}
	
	/**
	 * 根据省份id获取城市列表
	 * @param pid
	 * @return
	 */
	@RequestMapping(value="/get_citys",method=RequestMethod.POST)
	@ResponseBody
	public Result<List<Area>> getCitys(@RequestParam(name="pid",required=true)Long pid){
		return Result.success(areaService.getAllCity(pid));
	}
	
	/**
	 * 根据城市id获取城市区
	 * @param cid
	 * @return
	 */
	@RequestMapping(value="/get_districts",method=RequestMethod.POST)
	@ResponseBody
	public Result<List<Area>> getDistricts(@RequestParam(name="cid",required=true)Long cid){
		return Result.success(areaService.getAllDistrict(cid));
	}
}
