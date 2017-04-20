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
		//����һ����ָ��Zone�����������
		Configuration cfg = new Configuration(Zone.zone0());
		//...���������ο���ע��
		UploadManager uploadManager = new UploadManager(cfg);
		//...�����ϴ�ƾ֤��Ȼ��׼���ϴ�
		//�����Windows����£���ʽ�� D:\\qiniu\\test.png
		//Ĭ�ϲ�ָ��key������£����ļ����ݵ�hashֵ��Ϊ�ļ���
		String key = null;
		try {
		    Response response = uploadManager.put(localFilePath, key, this.getToken());
		    //�����ϴ��ɹ��Ľ��
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
