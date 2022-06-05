package com.franky.movie.controller.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 文件同意下载控制器
 * @author frank
 *
 */
@RequestMapping("/download")
@Controller
public class DownloadController {

	@Value("${movie.upload.video.path}")
	private String uploadVideoPath;//视频保存位置
	
	/**
	 * 视频文件下载方法
	 * @param filename
	 * @param response
	 */
	@RequestMapping("/download_video")
	public void downloadVideoFile(@RequestParam(name="filename",required=true)String filename,HttpServletResponse response){
		filename = uploadVideoPath + filename;
		File file = new File(filename);
		//设置返回文件大小
		response.setContentLength(Integer.valueOf(file.length()+""));
		try {
			response.setContentType(Files.probeContentType(Paths.get(filename)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String suffix = filename.substring(filename.lastIndexOf(".")+1,filename.length());
		response.setHeader("Content-Disposition", "attachment;filename=" + System.currentTimeMillis() + suffix);
		writeFileToResponse(file,response);
	}
	
	/**
	 * 将文件流读出放入至指定的response
	 * @param file
	 * @param response
	 */
	private void writeFileToResponse(File file,HttpServletResponse response){
		byte[] buff = new byte[1024];
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			ServletOutputStream outputStream = response.getOutputStream();
			int i = bis.read(buff);
			while(i != -1){
				outputStream.write(buff, 0, i);
				i = bis.read(buff);
			}
			bis.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
