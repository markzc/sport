package com.itheima.core.service.product;

import org.springframework.stereotype.Service;

import com.itheima.common.fdfs.FastDFSUtils;

@Service("uploadService")
public class UploadServiceImpl implements UploadService{

	
	//保存到分布式文件系统FastDFS上去  Java接口  连接分布  服务提供方
	public String uploadPic(byte[] pic,String name,long size) throws Exception{
		//上传FastDFS的Java接口
		return FastDFSUtils.uploadPic(pic, name, size);
	}
}
