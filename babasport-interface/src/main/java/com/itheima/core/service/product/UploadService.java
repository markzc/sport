package com.itheima.core.service.product;

public interface UploadService {
	
	
	//保存到分布式文件系统FastDFS上去  Java接口  连接分布  服务提供方
	public String uploadPic(byte[] pic,String name,long size) throws Exception;

}
