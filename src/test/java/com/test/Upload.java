package com.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
public class Upload {

	private String accessKey;
	private String secretKey;
	private String bucket;
	public Upload(String accessKey, String secretKey,String bucket)
	{
		this.setAccessKey(accessKey);
		this.setSecretKey(secretKey);
		this.setBucket(bucket);
	}
	public String getToken()
	{
		Auth auth = Auth.create(this.accessKey, this.secretKey);
		return auth.uploadToken(this.bucket);
	}
	public String getToken(String key)
	{
		Auth auth = Auth.create(this.accessKey, this.secretKey);
		return auth.uploadToken(this.bucket,key);
	}
	public String getAccessKey() {
		return this.accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getSecretKey() {
		return this.secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public void setBucket(String bucket)
	{
		this.bucket = bucket;
	}
	public String uploadLocalFile(String localFilePath)
	{
		//构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone0());
		//...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		//...生成上传凭证，然后准备上传
		//如果是Windows情况下，格式是 D:\\qiniu\\test.png
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = null;
		try {
		    Response response = uploadManager.put(localFilePath, key, this.getToken());
		    //解析上传成功的结果
		    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
		    System.out.println(putRet.key);
		    System.out.println(putRet.hash);
		} catch (QiniuException ex) {
		    Response r = ex.response;
		    System.err.println(r.toString());
		    try {
		        System.err.println(r.bodyString());
		    } catch (QiniuException ex2) {
		        //ignore
		    }
		}
		return localFilePath;
		
	}
	
}
