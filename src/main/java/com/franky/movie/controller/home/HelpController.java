package com.franky.movie.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 前台首页
 * @author frank
 *
 */
@RequestMapping("/home/help")
@Controller
public class HelpController {

	/**
	 * 帮助中心
	 * @param model
	 * @return
	 */
	@RequestMapping("/use-help")
	public String index(Model model){
		return "home/help/use-help";
	}
	/**
	 * 帮助中心其他相关页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/item/{item}")
	public String aboutUs(Model model,@PathVariable String item){
		return "home/help/" + item;
	}
	
	
}
