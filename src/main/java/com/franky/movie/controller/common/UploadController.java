package com.franky.movie.controller.common;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.franky.movie.bean.CodeMsg;
import com.franky.movie.bean.Result;
import com.franky.movie.util.StringUtil;

/**
 * 公用的上传类
 * @author frank
 *
 */
@RequestMapping("/upload")
@Controller
public class UploadController {

	@Value("${movie.upload.photo.sufix}")
	private String uploadPhotoSufix;
	
	@Value("${movie.upload.photo.maxsize}")
	private long uploadPhotoMaxSize;
	
	@Value("${movie.upload.photo.path}")
	private String uploadPhotoPath;//图片保存位置
	
	@Value("${movie.upload.video.sufix}")
	private String uploadVideoSufix;
	
	@Value("${movie.upload.video.maxsize}")
	private long uploadVideoMaxSize;
	
	@Value("${movie.upload.video.path}")
	private String uploadVideoPath;//视频保存位置
	
	private Logger log = LoggerFactory.getLogger(UploadController.class);
	
	/**
	 * 图片统一上传类
	 * @param photo
	 * @return
	 */
	@RequestMapping(value="/upload_photo",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> uploadPhoto(@RequestParam(name="photo",required=true)MultipartFile photo){
		//判断文件类型是否是图片
		String originalFilename = photo.getOriginalFilename();
		//获取文件后缀
		String suffix = originalFilename.substring(originalFilename.lastIndexOf("."),originalFilename.length());
		if(!uploadPhotoSufix.contains(suffix.toLowerCase())){
			return Result.error(CodeMsg.UPLOAD_PHOTO_SUFFIX_ERROR);
		}
		if(photo.getSize()/1024 > uploadPhotoMaxSize){
			CodeMsg codeMsg = CodeMsg.UPLOAD_PHOTO_ERROR;
			codeMsg.setMsg("图片大小不能超过" + (uploadPhotoMaxSize/1024) + "M");
			return Result.error(codeMsg);
		}
		//准备保存文件
		File filePath = new File(uploadPhotoPath);
		if(!filePath.exists()){
			//若不存在文件夹，则创建一个文件夹
			filePath.mkdir();
		}
		filePath = new File(uploadPhotoPath + "/" + StringUtil.getFormatterDate(new Date(), "yyyyMMdd"));
		//判断当天日期的文件夹是否存在，若不存在，则创建
		if(!filePath.exists()){
			//若不存在文件夹，则创建一个文件夹
			filePath.mkdir();
		}
		String filename = StringUtil.getFormatterDate(new Date(), "yyyyMMdd") + "/" + System.currentTimeMillis() + suffix;
		try {
			photo.transferTo(new File(uploadPhotoPath+"/"+filename));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("图片上传成功，保存位置：" + uploadPhotoPath + filename);
		return Result.success(filename);
	}
	
	/**
	 * 上传视频
	 * @param video
	 * @return
	 */
	@RequestMapping(value="/upload_video",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> uploadVideo(@RequestParam(name="video",required=true)MultipartFile video){
		//判断文件类型是否是视频
		String originalFilename = video.getOriginalFilename();
		//获取文件后缀
		String suffix = originalFilename.substring(originalFilename.lastIndexOf("."),originalFilename.length());
		if(!uploadVideoSufix.contains(suffix.toLowerCase())){
			return Result.error(CodeMsg.UPLOAD_VIDEO_ERROR);
		}
		if(video.getSize()/1024 > uploadVideoMaxSize){
			CodeMsg codeMsg = CodeMsg.UPLOAD_PHOTO_ERROR;
			codeMsg.setMsg("视频大小不能超过" + (uploadVideoMaxSize/1024) + "M");
			return Result.error(codeMsg);
		}
		//准备保存文件
		File filePath = new File(uploadVideoPath);
		if(!filePath.exists()){
			//若不存在文件夹，则创建一个文件夹
			filePath.mkdir();
		}
		filePath = new File(uploadVideoPath + "/" + StringUtil.getFormatterDate(new Date(), "yyyyMMdd"));
		//判断当天日期的文件夹是否存在，若不存在，则创建
		if(!filePath.exists()){
			//若不存在文件夹，则创建一个文件夹
			filePath.mkdir();
		}
		String filename = StringUtil.getFormatterDate(new Date(), "yyyyMMdd") + "/" + System.currentTimeMillis() + suffix;
		try {
			video.transferTo(new File(uploadVideoPath+"/"+filename));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("视频上传成功，保存位置：" + uploadVideoPath + filename);
		return Result.success(filename);
	}
}
