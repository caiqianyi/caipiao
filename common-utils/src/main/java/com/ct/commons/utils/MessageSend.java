package com.ct.commons.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

//HTTPS版本短信发送DEMO
public class MessageSend {

	private static HttpClient httpclient;

	public  static void main(String[] args) throws Exception {

		//sendValidateMsgNew("15313815224");
//		sendNotice("15313815224","26352");
//		sendWDAcc("15201441734", "tan", "123");
	}
	
	/**
	 * 调用短信服务，发送短信验证码
	 * @param tel
	 * @return
	 * @throws Exception
	 */
	public static String sendValidateMsgNew(String tel) throws Exception{
		HttpClientUtil hu = new HttpClientUtil();
		Map<String, Object> postData = new HashMap<String, Object>();
		String url = PropertiesUtil.getPropertie("msgSendValidate","/remote_service_config.properties");//"http://msgapi.ixianlai.com/utilservice1/msg/sendMsg";
		String sign =  MD5.md5(tel+"50001"+"a2f2dc1e2d2881c3fb314d5d472b9100");
		postData.put("tel", tel);
		postData.put("sign", sign);
		postData.put("cst_id", "50001");
	    String	returnJson = hu.doPostRequest(url, postData);
	    System.out.println(returnJson);
		return returnJson;
	}	
	
	/**
	 * 短信验证
	 * @param tel
	 * @param verCode
	 * @return
	 * @throws Exception
	 */
	public static String validateChkCode(String tel,String verCode) throws Exception {
		String url = PropertiesUtil.getPropertie("msgChkCodeValidate","/remote_service_config.properties");
		Map<String, Object> postData = new HashMap<String, Object>();
		postData.put("tel", tel);
		postData.put("verCode", verCode);
		HttpClientUtil hu = new HttpClientUtil();
		String	returnJson = hu.doPostRequest(url, postData);
		Map<String, String> retMap = JsonUtil.json2Map(returnJson);
		String is_success = (String) retMap.get("is_success");
		System.out.println(is_success);
		return returnJson;
	}
	
	
	/**
	 * 发送手机验证码
	 * @throws Exception
	 */
	public static String sendValidateMsg(String tel,String validate) throws Exception{
		httpclient = new SSLClient();
		String url = "https://dx.ipyy.net/sms.aspx";
		String accountName="AF00025";							//改为实际账号名
		String password="AF0002585";								//改为实际发送密码
		
		//String text ="【闲徕游戏】"+validate+"（动态验证码），10分钟内有效，请尽快操作，进行验证！"; 
		String text ="【闲徕游戏】"+validate+"（动态验证码），10分钟内有效，请尽快操作。 如开通代理请将验证码告知我司人员，以便尽快开通。"; 
		
		String returncode="";
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("action","send"));
		nvps.add(new BasicNameValuePair("userid", ""));
		nvps.add(new BasicNameValuePair("account", accountName)); 	
		nvps.add(new BasicNameValuePair("password", password));		
		nvps.add(new BasicNameValuePair("mobile", tel));		//多个手机号用逗号分隔
		nvps.add(new BasicNameValuePair("content", text));
		nvps.add(new BasicNameValuePair("sendTime", ""));
		nvps.add(new BasicNameValuePair("extno", ""));

		post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));

		HttpResponse response = httpclient.execute(post);

		try {
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			// 将字符转化为XML
			Document doc = DocumentHelper.parseText(EntityUtils.toString(entity, "UTF-8"));
			// 获取根节点
			Element rootElt = doc.getRootElement();
			// 获取根节点下的子节点的值
			String returnstatus = rootElt.elementText("returnstatus").trim();
			String message = rootElt.elementText("message").trim();
			String remainpoint = rootElt.elementText("remainpoint").trim();
			String taskID = rootElt.elementText("taskID").trim();
			String successCounts = rootElt.elementText("successCounts").trim();

			returncode=returnstatus;
			EntityUtils.consume(entity);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return returncode;
	}
	
	/**
	 * 发送手机验证码
	 * @throws Exception
	 */
	public static String sendFindPswMsg(String tel,String validate) throws Exception{
		httpclient = new SSLClient();
		String url = "https://dx.ipyy.net/sms.aspx";
		String accountName="AF00025";							//改为实际账号名
		String password="AF0002585";								//改为实际发送密码
		
//		String text ="【闲徕游戏】"+validate+"（动态验证码），10分钟内有效，请不要把验证码泄露给其他人。如非本人操作，可不用理会！ ";
		String text ="【闲徕游戏】"+validate+"（动态验证码），10分钟内有效，请不要把验证码泄露给其他人。如非本人操作，可不用理会！"; 
		
		String returncode="";
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("action","send"));
		nvps.add(new BasicNameValuePair("userid", ""));
		nvps.add(new BasicNameValuePair("account", accountName)); 	
		nvps.add(new BasicNameValuePair("password", password));		
		nvps.add(new BasicNameValuePair("mobile", tel));		//多个手机号用逗号分隔
		nvps.add(new BasicNameValuePair("content", text));
		nvps.add(new BasicNameValuePair("sendTime", ""));
		nvps.add(new BasicNameValuePair("extno", ""));

		post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));

		HttpResponse response = httpclient.execute(post);

		try {
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			// 将字符转化为XML
			Document doc = DocumentHelper.parseText(EntityUtils.toString(entity, "UTF-8"));
			// 获取根节点
			Element rootElt = doc.getRootElement();
			// 获取根节点下的子节点的值
			String returnstatus = rootElt.elementText("returnstatus").trim();
			String message = rootElt.elementText("message").trim();
			String remainpoint = rootElt.elementText("remainpoint").trim();
			String taskID = rootElt.elementText("taskID").trim();
			String successCounts = rootElt.elementText("successCounts").trim();

			returncode=returnstatus;
			EntityUtils.consume(entity);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return returncode;
	}
	
	/**
	 * 发送手密码
	 * @throws Exception
	 */
	public static String sendPsw(String tel,String psw) throws Exception{
		httpclient = new SSLClient();
		String url = "https://dx.ipyy.net/sms.aspx";
		String accountName="AF00025";							//改为实际账号名
		String password="AF0002585";								//改为实际发送密码
		
		String text ="【闲徕游戏】尊敬的用户，您的初始密码是"+psw+"，请妥善保存，勿泄露给他人。登录系统，在我的信息中可以进行修改！  ";
		
		String returncode="";
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("action","send"));
		nvps.add(new BasicNameValuePair("userid", ""));
		nvps.add(new BasicNameValuePair("account", accountName)); 	
		nvps.add(new BasicNameValuePair("password", password));		
		nvps.add(new BasicNameValuePair("mobile", tel));		//多个手机号用逗号分隔
		nvps.add(new BasicNameValuePair("content", text));
		nvps.add(new BasicNameValuePair("sendTime", ""));
		nvps.add(new BasicNameValuePair("extno", ""));

		post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));

		HttpResponse response = httpclient.execute(post);

		try {
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			// 将字符转化为XML
			Document doc = DocumentHelper.parseText(EntityUtils.toString(entity, "UTF-8"));
			// 获取根节点
			Element rootElt = doc.getRootElement();
			// 获取根节点下的子节点的值
			String returnstatus = rootElt.elementText("returnstatus").trim();
			String message = rootElt.elementText("message").trim();
			String remainpoint = rootElt.elementText("remainpoint").trim();
			String taskID = rootElt.elementText("taskID").trim();
			String successCounts = rootElt.elementText("successCounts").trim();

			returncode=returnstatus;
			EntityUtils.consume(entity);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return returncode;
	}
	
	/**
	 * 调用远程接口，发送通知
	 * @author xhl
	 * @throws Exception 
	 *
	 */
	public static String sendNotice(String tel,String psw) throws Exception{
		HttpClientUtil hu = new HttpClientUtil();
		Map<String, Object> postData = new HashMap<String, Object>();
		String url = PropertiesUtil.getPropertie("msgNotice","/remote_service_config.properties");
		String sign =  MD5.md5(tel+"55555"+"3529632d9f0cb3c87951baf93b94bf0c");
		postData.put("tel", tel);
		postData.put("sign", sign);
		postData.put("cst_id", "55555");
		String content="尊敬的用户，您的初始密码是"+psw+"，请妥善保存，勿泄露给他人。登录系统，在我的信息中可以进行修改！  ";
		postData.put("content", content);
	    String	returnJson = hu.doPostRequest(url, postData);
	    System.out.println(returnJson);
		return returnJson;
	}
	
	/**
	 * 调用远程接口，发送通知（通用）
	 * @author xhb
	 * @throws Exception 
	 *
	 */
	public static String sendCommonNotice(String tel, String content) throws Exception{
		HttpClientUtil hu = new HttpClientUtil();
		Map<String, Object> postData = new HashMap<String, Object>();
		String url = PropertiesUtil.getPropertie("msgNotice","/remote_service_config.properties");
		String sign =  MD5.md5(tel+"55555"+"3529632d9f0cb3c87951baf93b94bf0c");
		postData.put("tel", tel);
		postData.put("sign", sign);
		postData.put("cst_id", "55555");
//		String content="尊敬的用户，您的初始密码是"+psw+"，请妥善保存，勿泄露给他人。登录系统，在我的信息中可以进行修改！  ";
		postData.put("content", content);
	    String	returnJson = hu.doPostRequest(url, postData);
	    System.out.println(returnJson);
		return returnJson;
	}
	
	/**
	 * 调用远程接口，发送微店账号
	 * @author tanhb
	 * @throws Exception 
	 *
	 */
	public static String sendWDAcc(String tel,String acc,String pwd) throws Exception{
		HttpClientUtil hu = new HttpClientUtil();
		Map<String, Object> postData = new HashMap<String, Object>();
		String url = PropertiesUtil.getPropertie("msgNotice","/remote_service_config.properties");
		String sign =  MD5.md5(tel+"55555"+"3529632d9f0cb3c87951baf93b94bf0c");
		postData.put("tel", tel);
		postData.put("sign", sign);
		postData.put("cst_id", "55555");
		String content="恭喜您成功开通微店账号："+acc+" 密码："+pwd+"，请尽快在：https://huodong.weidian.com/h/fxpwd/index.html 修改密码，如有疑问请联系闲来官方客服";
		postData.put("content", content);
		String	returnJson = hu.doPostRequest(url, postData);
		System.out.println(returnJson);
		return returnJson;
	}
	
	/**
	 * 通用短信调用
	 * @author tanhb
	 * @throws Exception 
	 *
	 */
	public static String sendCommonMess(String tel,String content) throws Exception{
		HttpClientUtil hu = new HttpClientUtil();
		Map<String, Object> postData = new HashMap<String, Object>();
		String url = PropertiesUtil.getPropertie("msgNotice","/remote_service_config.properties");
		String sign =  MD5.md5(tel+"55555"+"3529632d9f0cb3c87951baf93b94bf0c");
		postData.put("tel", tel);
		postData.put("sign", sign);
		postData.put("cst_id", "55555");
		postData.put("content", content);
		String	returnJson = hu.doPostRequest(url, postData);
		System.out.println(returnJson);
		return returnJson;
	}
}



class SSLClient extends DefaultHttpClient {
	public SSLClient() throws Exception {
		super();
		SSLContext ctx = SSLContext.getInstance("TLS");
		X509TrustManager tm = new X509TrustManager() {

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// TODO Auto-generated method stub

			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// TODO Auto-generated method stub

			}

			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}

		};
		ctx.init(null, new TrustManager[] { tm }, null);

		SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		ClientConnectionManager ccm = this.getConnectionManager();
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", 443, ssf));
	}
}