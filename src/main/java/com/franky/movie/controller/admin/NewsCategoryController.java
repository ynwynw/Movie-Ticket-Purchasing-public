package com.franky.movie.controller.admin;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.franky.movie.bean.CodeMsg;
import com.franky.movie.bean.PageBean;
import com.franky.movie.bean.Result;
import com.franky.movie.entity.common.NewsCategory;
import com.franky.movie.service.common.NewsCategoryService;

/**
 * 新闻分类管理控制器
 * @author frank
 *
 */
@RequestMapping("/admin/news_category")
@Controller
public class NewsCategoryController {

	@Autowired
	private NewsCategoryService newsCategoryService;
	
	/**
	 * 新闻分类列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(NewsCategory newsCategory,PageBean<NewsCategory> pageBean,Model model){
		model.addAttribute("pageBean", newsCategoryService.findPage(newsCategory, pageBean));
		model.addAttribute("name", newsCategory.getName());
		return "admin/news_category/list";
	}
	
	/**
	 * 新闻分类添加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(Model model){
		return "admin/news_category/add";
	}
	
	/**
	 * 新闻分类编辑页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(Model model,@RequestParam(name="id",required=true)Long id){
		model.addAttribute("newsCategory", newsCategoryService.find(id));
		return "admin/news_category/edit";
	}
	
	/**
	 * 添加新闻分类表单提交
	 * @param newsCategory
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> add(NewsCategory newsCategory){
		if(newsCategory == null){
			return Result.error(CodeMsg.DATA_ERROR);
		}
		if(StringUtils.isEmpty(newsCategory.getName())){
			return Result.error(CodeMsg.ADMIN_NEWS_CATEGORY_NAME_EMPTY);
		}
		//判断是否是编辑
		if(newsCategory.getId() != null && newsCategory.getId() > 0){
			NewsCategory findById = newsCategoryService.find(newsCategory.getId());
			newsCategory.setCreateTime(findById.getCreateTime());
		}
		//表示数据合法，可以保存到数据库
		if(newsCategoryService.save(newsCategory) == null){
			return Result.error(CodeMsg.ADMIN_NEWS_CATEGORY_SAVE_ERROR);
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
			newsCategoryService.delete(id);
		} catch (Exception e) {
			return Result.error(CodeMsg.ADMIN_NEWS_CATEGORY_DELETE_ERROR);
		}
		return Result.success(true);
	}
	
	
}
