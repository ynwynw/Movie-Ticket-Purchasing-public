package com.franky.movie.controller.common;
/**
 * 图片统一查看控制器
 */
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.franky.movie.bean.Result;

@RequestMapping("photo")
@Controller
public class PhotoController {
	
	@Autowired
	private ResourceLoader resourceLoader;
	@Value("${movie.upload.photo.path}")
	private String uploadPhotoPath;//文件保存位置
	
	/**
	 * 系统统一的图片查看方法
	 * @param filename
	 * @return
	 */
	@RequestMapping(value="/view")
	@ResponseBody
	public ResponseEntity<?> viewPhoto(@RequestParam(name="filename",required=true)String filename){
		Resource resource = resourceLoader.getResource("file:" + uploadPhotoPath + filename); 
		try {
			return ResponseEntity.ok(resource);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * 图片同意裁剪方法
	 * @param filename
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	@RequestMapping(value="/cut_photo")
	@ResponseBody
	public Result<String> cutPhoto(@RequestParam(name="filename",required=true)String filename,
			@RequestParam(name="x",required=true)Integer x,
			@RequestParam(name="y",required=true)Integer y,
			@RequestParam(name="width",required=true)Integer width,
			@RequestParam(name="height",required=true)Integer height
			){
		try {
			String filepath = uploadPhotoPath + filename;
			Thumbnails
			.of(filepath)
			.sourceRegion(x, y,width, height)
			.size(width, height)
			.toFile(filepath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Result.success(filename);
	}
}
