package com.itheima.core.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.itheima.common.web.Constants;
import com.itheima.core.service.product.UploadService;

/**
 * 上传图片
 * @author lx
 *
 */
@Controller
public class UploadController {

	@Autowired
	private UploadService uploadService;
	//上传品牌的图片
	@RequestMapping(value = "/upload/uploadPic.do")
	public void uploadPic(MultipartFile pic,HttpServletRequest request,HttpServletResponse response) throws Exception{
		System.out.println(pic.getOriginalFilename());
		
	/*	String path = "/upload/" + pic.getOriginalFilename();
		String url = request.getSession().getServletContext().getRealPath(path);
		pic.transferTo(new File(url));*/
		
		//保存到分布式文件系统FastDFS上去  Java接口  连接分布  服务提供方
		String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
		
		
		JSONObject jo = new JSONObject();
		jo.put("path", Constants.IMG_URL + path);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(jo.toString());
		
	}
	//上传商品多张图片
	@RequestMapping(value = "/upload/uploadPics.do")
	public @ResponseBody 
	List<String> uploadPics(@RequestParam(required = false) MultipartFile[] pics) throws IOException, Exception{
		//路径 结果集
		List<String> urls = new ArrayList<String>();
		//遍历 
		for (MultipartFile pic : pics) {
			//保存到分布式文件系统FastDFS上去  Java接口  连接分布  服务提供方  
			String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
			urls.add(Constants.IMG_URL + path);
		}
		
		//返回结果集JSON格式返回
		return urls;
	}
	//上传图片
	@RequestMapping(value = "/upload/uploadFck.do")
	public void uploadFck(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//无敌版接收图片   图片肯定在Request域中  只有图片或文件
		MultipartRequest mr = (MultipartRequest)request;
		Map<String, MultipartFile> fileMap = mr.getFileMap();
		Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
		for (Entry<String, MultipartFile> entry : entrySet) {
			MultipartFile pic = entry.getValue();
			//保存到分布式文件系统FastDFS上去  Java接口  连接分布  服务提供方
			String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
			JSONObject jo = new JSONObject();
			jo.put("url", Constants.IMG_URL + path);
			jo.put("error", 0);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(jo.toString());
		}
	}
}
