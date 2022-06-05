package com.franky.movie.controller.admin;

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
import com.franky.movie.entity.common.News;
import com.franky.movie.entity.common.NewsCategory;
import com.franky.movie.service.common.NewsCategoryService;
import com.franky.movie.service.common.NewsService;
import com.franky.movie.util.ValidateEntityUtil;

/**
 * 新闻管理控制器
 * @author frank
 *
 */
@RequestMapping("/admin/news")
@Controller
public class NewsController {

	@Autowired
	private NewsCategoryService newsCategoryService;
	@Autowired
	private NewsService newsService;
	
	/**
	 * 新闻列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(News news,PageBean<News> pageBean,Model model){
		model.addAttribute("pageBean", newsService.findPage(news, pageBean));
		model.addAttribute("title", news.getTitle());
		return "admin/news/list";
	}
	
	/**
	 * 新闻添加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(Model model){
		model.addAttribute("newsCategoryList", newsCategoryService.findAll());
		return "admin/news/add";
	}
	
	/**
	 * 新闻编辑页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(Model model,@RequestParam(name="id",required=true)Long id){
		model.addAttribute("news", newsService.find(id));
		model.addAttribute("newsCategoryList", newsCategoryService.findAll());
		return "admin/news/edit";
	}
	
	/**
	 * 添加新闻表单提交
	 * @param news
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> add(News news){
		if(news == null){
			return Result.error(CodeMsg.DATA_ERROR);
		}
		CodeMsg validate = ValidateEntityUtil.validate(news);
		if(validate.getCode() != CodeMsg.SUCCESS.getCode()){
			return Result.error(validate);
		}
		//判断是否是编辑
		if(news.getId() != null && news.getId() > 0){
			NewsCategory findById = newsCategoryService.find(news.getId());
			news.setCreateTime(findById.getCreateTime());
		}
		//表示数据合法，可以保存到数据库
		if(newsService.save(news) == null){
			return Result.error(CodeMsg.ADMIN_NEWS_SAVE_ERROR);
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
			newsService.delete(id);
		} catch (Exception e) {
			return Result.error(CodeMsg.ADMIN_NEWS_DELETE_ERROR);
		}
		return Result.success(true);
	}
	
	
}
