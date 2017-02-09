package com.itheima.common.fdfs;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

/**
 * 上传图片
 * @author lx
 *
 */
public class FastDFSUtils {

	
	//上传
	public static String uploadPic(byte[] pic,String name,long size) throws Exception{
		 
		ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
		//FastDFS
		ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
		//连接Tracker
		TrackerClient trackerClient = new TrackerClient();
		//保存了Stoager的地址
		TrackerServer trackerServer = trackerClient.getConnection();
		//连接Stoager了
		StorageServer storageServer = null;//扩展功能
		StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
		String ext = FilenameUtils.getExtension(name);
		//meta 信息  
		NameValuePair[] meta_list = new NameValuePair[3];
		meta_list[0] = new NameValuePair("filename",name);
		meta_list[1] = new NameValuePair("fileext",ext);
		meta_list[2] = new NameValuePair("filesize",String.valueOf(size));
		
		//上传图片    http://192.168.200.128/
		//      group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
		String path = storageClient1.upload_file1(pic, ext, meta_list);
		return path;
	}
}
