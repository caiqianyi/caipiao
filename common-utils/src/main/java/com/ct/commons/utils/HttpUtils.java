package com.ct.commons.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http请求
 * @author liyan on 16/11/28
 */
public class HttpUtils {

	private final static Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

	// 多线程并发
	private static MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
	private static HttpClient httpClient = new HttpClient(httpConnectionManager);

	// 默认配置
	static {

		//每主机最大连接数和总共最大连接数，通过hosfConfiguration设置host来区分每个主机

		httpClient.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(60);

		httpClient.getHttpConnectionManager().getParams().setMaxTotalConnections(1000);

		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);

		httpClient.getHttpConnectionManager().getParams().setSoTimeout(2000);

		httpClient.getHttpConnectionManager().getParams().setTcpNoDelay(true);

		httpClient.getHttpConnectionManager().getParams().setLinger(1000);

		//失败的情况下会进行3次尝试,成功之后不会再尝试
		httpClient.getHttpConnectionManager().getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

	}

	public static String executeWithHttpGet(String thirdURL, int connectionTimeout) throws IOException {
		HttpMethod httpMethod = new GetMethod(thirdURL);
		URI uri = null;
		try {
			uri = httpMethod.getURI();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(">>>>--{}", uri.toString());
			}
			if (connectionTimeout > 0) {
				httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
			}
			int statusCode = httpClient.executeMethod(httpMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.error("执行HttpGet方法出错：[{}]", statusCode);
				throw new HttpException("执行HttpGet方法出错：" + statusCode);
			}
			String result = httpMethod.getResponseBodyAsString();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("<<<<--{}", result);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("访问[{}]发生异常：", uri.toString(), e);
			throw new IOException("执行HttpClient发生异常");
		} finally {
			httpMethod.releaseConnection();
		}
	}

	public static String executeWithHttpGet(String thirdURL) throws IOException {
		HttpMethod httpMethod = new GetMethod(thirdURL);
		URI uri = null;
		try {
			uri = httpMethod.getURI();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(">>>>--{}", uri.toString());
			}
			int statusCode = httpClient.executeMethod(httpMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.error("执行HttpGet方法出错：[{}]", statusCode);
				throw new HttpException("执行HttpGet方法出错：" + statusCode);
			}
			String result = httpMethod.getResponseBodyAsString();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("<<<<--{}", result);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("访问[{}]发生异常：", uri.toString(), e);
			throw new IOException("执行HttpClient发生异常");
		} finally {
			httpMethod.releaseConnection();
		}
	}

	public static String executeWithHttpGet(String thirdURL, Map<String, Object> params, int connectionTimeout) throws IOException {
		HttpMethod httpMethod = new GetMethod(thirdURL);
		URI uri = null;
		NameValuePair[] nvps = convertMapToNameValuePair(params);
		if (nvps !=null && (nvps.length > 0)) {
			httpMethod.setQueryString(nvps);
		}
		try {
			uri = httpMethod.getURI();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(">>>>--{}", uri.toString());
			}
			if (connectionTimeout > 0) {
				httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
			}
			int statusCode = httpClient.executeMethod(httpMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.error("执行HttpGet方法出错：[{}]", statusCode);
				throw new HttpException("执行HttpGet方法出错：" + statusCode);
			}
			String result = httpMethod.getResponseBodyAsString();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("<<<<--{}", result);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("访问[{}]发生异常：", uri.toString(), e);
			throw new IOException("执行HttpClient发生异常");
		} finally {
			httpMethod.releaseConnection();
		}
	}

	public static String executeWithHttpGet(String thirdURL, Map<String, Object> params) throws IOException {
		HttpMethod httpMethod = new GetMethod(thirdURL);
		URI uri = null;
		NameValuePair[] nvps = convertMapToNameValuePair(params);
		if (nvps !=null && (nvps.length > 0)) {
			httpMethod.setQueryString(nvps);
		}
		try {
			uri = httpMethod.getURI();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(">>>>--{}", uri.toString());
			}
			int statusCode = httpClient.executeMethod(httpMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.error("执行HttpGet方法出错：[{}]", statusCode);
				throw new HttpException("执行HttpGet方法出错：" + statusCode);
			}
			String result = httpMethod.getResponseBodyAsString();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("<<<<--{}", result);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("访问[{}]发生异常：", uri.toString(), e);
			throw new IOException("执行HttpClient发生异常");
		} finally {
			httpMethod.releaseConnection();
		}
	}

	public static String executeWithHttpPost(String thirdURL, Map<String, Object> params, String charSet, int connectionTimeout) throws IOException {
		PostMethod postMethod = new PostMethod(thirdURL);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);
		URI uri = null;
		NameValuePair[] nvps = convertMapToNameValuePair(params);
		if (nvps !=null && (nvps.length > 0)) {
			postMethod.setRequestBody(nvps);
		}
		try {
			uri = postMethod.getURI();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(">>>>--{}", uri.toString());
			}
			if (connectionTimeout > 0) {
				httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
			}
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.error("执行HttpGet方法出错：[{}]", statusCode);
				throw new HttpException("执行HttpGet方法出错：" + statusCode);
			}
			String result = postMethod.getResponseBodyAsString();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("<<<<--{}", result);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("访问[{}]发生异常：", uri.toString(), e);
			throw new IOException("执行HttpClient发生异常");
		} finally {
			postMethod.releaseConnection();
		}
	}

	public static String executeWithHttpPost(String thirdURL, Map<String, Object> params, String charSet) throws IOException {
		PostMethod postMethod = new PostMethod(thirdURL);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);
		URI uri = null;
		NameValuePair[] nvps = convertMapToNameValuePair(params);
		if (nvps !=null && (nvps.length > 0)) {
			postMethod.setRequestBody(nvps);
		}
		try {
			uri = postMethod.getURI();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(">>>>--{}", uri.toString());
			}
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.error("执行HttpGet方法出错：[{}]", statusCode);
				throw new HttpException("执行HttpGet方法出错：" + statusCode);
			}
			String result = postMethod.getResponseBodyAsString();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("<<<<--{}", result);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("访问[{}]发生异常：", uri.toString(), e);
			throw new IOException("执行HttpClient发生异常");
		} finally {
			postMethod.releaseConnection();
		}
	}


	public static NameValuePair[] convertMapToNameValuePair(Map<String, Object> params) {
		if (params == null) {
			return null;
		}
		Set<String> paramsSet = params.keySet();
		NameValuePair[] nvps = new NameValuePair[paramsSet.size()];
		int i = 0;
		for (Iterator<String> it = paramsSet.iterator(); it.hasNext(); i++) {
			String name = it.next();
			Object value = params.get(name);
			nvps[i] = new NameValuePair(name, value.toString());
		}
		return nvps;
	}



}
