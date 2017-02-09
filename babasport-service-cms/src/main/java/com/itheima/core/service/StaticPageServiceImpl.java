package com.itheima.core.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 静态化程序
 * @author lx
 *
 */
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware{

	//声明
	private Configuration conf;
	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.conf = freeMarkerConfigurer.getConfiguration();
	}



	//主程序
	public void index(Map<String,Object> root,String id){
		//输出路径
		//全路径 
		String path = getPath("/html/product/" + id  + ".html");
		File f = new File(path);
		File parentFile = f.getParentFile();
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		//模板 +  数据模型 = 输出
		Writer out = null;
		try {
			//读
			Template template = conf.getTemplate("product.html");
			//写
			out = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
			//处理方法
			template.process(root, out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(null != out){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	//全路径
	public String getPath(String path){
		return servletContext.getRealPath(path);
	}

	//声明
	private ServletContext servletContext;
	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		this.servletContext = servletContext;
	}
}
