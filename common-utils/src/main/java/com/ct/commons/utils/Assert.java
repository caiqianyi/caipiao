package com.ct.commons.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.ct.common.config.PropertiesManager;
import com.ct.commons.exception.I18nMessageException;

public abstract class Assert {
	
	private final static String profile = "/verify.properties";

	/**
	 * Assert a boolean expression, throwing {@code I18nMessageException}
	 * if the test result is {@code false}.
	 * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
	 * @param expression a boolean expression
	 * @param errcode the exception errcode to use if the assertion fails
	 * @throws I18nMessageException if expression is {@code false}
	 */
	public static void isTrue(boolean expression, Integer errcode,String message) {
		if (!expression) {
			throw new I18nMessageException(errcode,message);
		}
	}

	/**
	 * Assert a boolean expression, throwing {@code I18nMessageException}
	 * if the test result is {@code false}.
	 * <pre class="code">Assert.isTrue(i &gt; 0);</pre>
	 * @param expression a boolean expression
	 * @throws I18nMessageException if expression is {@code false}
	 */
	public static void isTrue(boolean expression) {
		isTrue(expression, 500 , "[Assertion failed] - this expression must be true");
	}

	/**
	 * Assert that an object is {@code null} .
	 * <pre class="code">Assert.isNull(value, "The value must be null");</pre>
	 * @param object the object to check
	 * @param errcode the exception errcode to use if the assertion fails
	 * @throws I18nMessageException if the object is not {@code null}
	 */
	public static void isNull(Object object, Integer errcode,String message) {
		if (object != null) {
			throw new I18nMessageException(errcode,message);
		}
	}

	/**
	 * Assert that an object is {@code null} .
	 * <pre class="code">Assert.isNull(value);</pre>
	 * @param object the object to check
	 * @throws I18nMessageException if the object is not {@code null}
	 */
	public static void isNull(Object object) {
		isNull(object, 500 , "[Assertion failed] - the object argument must be null");
	}

	/**
	 * Assert that an object is not {@code null} .
	 * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
	 * @param object the object to check
	 * @param errcode the exception errcode to use if the assertion fails
	 * @throws I18nMessageException if the object is {@code null}
	 */
	public static void notNull(Object object, Integer errcode , String message) {
		if (object == null) {
			throw new I18nMessageException(errcode,message);
		}
	}

	/**
	 * Assert that an object is not {@code null} .
	 * <pre class="code">Assert.notNull(clazz);</pre>
	 * @param object the object to check
	 * @throws I18nMessageException if the object is {@code null}
	 */
	public static void notNull(Object object) {
		notNull(object, 500 , "[Assertion failed] - this argument is required; it must not be null");
	}

	/**
	 * Assert that the given String is not empty; that is,
	 * it must not be {@code null} and not the empty String.
	 * <pre class="code">Assert.hasLength(name, "Name must not be empty");</pre>
	 * @param text the String to check
	 * @param errcode the exception errcode to use if the assertion fails
	 * @see StringUtils#hasLength
	 */
	public static void hasLength(String text, Integer errcode, String message) {
		if (!StringUtils.hasLength(text)) {
			throw new I18nMessageException(errcode,message);
		}
	}

	/**
	 * Assert that the given String is not empty; that is,
	 * it must not be {@code null} and not the empty String.
	 * <pre class="code">Assert.hasLength(name);</pre>
	 * @param text the String to check
	 * @see StringUtils#hasLength
	 */
	public static void hasLength(String text) {
		hasLength(text, 500 , "[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not
	 * be {@code null} and must contain at least one non-whitespace character.
	 * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
	 * @param text the String to check
	 * @param errcode the exception errcode to use if the assertion fails
	 * @see StringUtils#hasText
	 */
	public static void hasText(String text, Integer errcode, String message) {
		if (!StringUtils.hasText(text)) {
			throw new I18nMessageException(errcode);
		}
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not
	 * be {@code null} and must contain at least one non-whitespace character.
	 * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
	 * @param text the String to check
	 * @see StringUtils#hasText
	 */
	public static void hasText(String text) {
		hasText(text,500,
				"[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	/**
	 * Assert that the given text does not contain the given substring.
	 * <pre class="code">Assert.doesNotContain(name, "rod", "Name must not contain 'rod'");</pre>
	 * @param textToSearch the text to search
	 * @param substring the substring to find within the text
	 * @param errcode the exception errcode to use if the assertion fails
	 */
	public static void doesNotContain(String textToSearch, String substring, Integer errcode, String message) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
				textToSearch.contains(substring)) {
			throw new I18nMessageException(errcode,message);
		}
	}

	/**
	 * Assert that the given text does not contain the given substring.
	 * <pre class="code">Assert.doesNotContain(name, "rod");</pre>
	 * @param textToSearch the text to search
	 * @param substring the substring to find within the text
	 */
	public static void doesNotContain(String textToSearch, String substring) {
		doesNotContain(textToSearch, substring,500,
				"[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
	}

	/**
	 * Assert that an array has elements; that is, it must not be
	 * {@code null} and must have at least one element.
	 * <pre class="code">Assert.notEmpty(array, "The array must have elements");</pre>
	 * @param array the array to check
	 * @param errcode the exception errcode to use if the assertion fails
	 * @throws I18nMessageException if the object array is {@code null} or has no elements
	 */
	public static void notEmpty(String str, Integer errcode,String message) {
		if (org.apache.commons.lang.StringUtils.isEmpty(str)) {
			throw new I18nMessageException(errcode,message);
		}
	}
	
	/**
	 * Assert that an array has elements; that is, it must not be
	 * {@code null} and must have at least one element.
	 * <pre class="code">Assert.notEmpty(array, "The array must have elements");</pre>
	 * @param array the array to check
	 * @param errcode the exception errcode to use if the assertion fails
	 * @throws I18nMessageException if the object array is {@code null} or has no elements
	 */
	public static void notEmpty(Object[] array, Integer errcode) {
		if (ObjectUtils.isEmpty(array)) {
			throw new I18nMessageException(errcode);
		}
	}

	/**
	 * Assert that an array has no null elements.
	 * Note: Does not complain if the array is empty!
	 * <pre class="code">Assert.noNullElements(array, "The array must have non-null elements");</pre>
	 * @param array the array to check
	 * @param errcode the exception errcode to use if the assertion fails
	 * @throws I18nMessageException if the object array contains a {@code null} element
	 */
	public static void noNullElements(Object[] array, Integer errcode) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw new I18nMessageException(errcode);
				}
			}
		}
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * {@code null} and must have at least one element.
	 * <pre class="code">Assert.notEmpty(collection, "Collection must have elements");</pre>
	 * @param collection the collection to check
	 * @param errcode the exception errcode to use if the assertion fails
	 * @throws I18nMessageException if the collection is {@code null} or has no elements
	 */
	public static void notEmpty(Collection<?> collection, Integer errcode) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new I18nMessageException(errcode);
		}
	}

	/**
	 * Assert that a Map has entries; that is, it must not be {@code null}
	 * and must have at least one entry.
	 * <pre class="code">Assert.notEmpty(map, "Map must have entries");</pre>
	 * @param map the map to check
	 * @param errcode the exception errcode to use if the assertion fails
	 * @throws I18nMessageException if the map is {@code null} or has no entries
	 */
	public static void notEmpty(Map<?, ?> map, Integer errcode, String message) {
		if (CollectionUtils.isEmpty(map)) {
			throw new I18nMessageException(errcode,message);
		}
	}

	/**
	 * Assert a boolean expression, throwing {@code I18nMessageException}
	 * if the test result is {@code false}. Call isTrue if you wish to
	 * throw I18nMessageException on an assertion failure.
	 * <pre class="code">Assert.state(id == null, "The id property must not already be initialized");</pre>
	 * @param expression a boolean expression
	 * @param errcode the exception errcode to use if the assertion fails
	 * @throws I18nMessageException if expression is {@code false}
	 */
	public static void state(boolean expression, Integer errcode) {
		if (!expression) {
			throw new I18nMessageException(errcode);
		}
	}
	
	/**
	 * 校验手机号
	 * @param tel
	 * @return false true
	 */
	public static void isMobile(String tel){
		boolean flag = false;
		if(StringUtils.isEmpty(tel)){
			throw new I18nMessageException(10002, "手机号不能为空!");
		}
		try {
			Properties prop = PropertiesManager.smartloadProperties(profile);
			String check = prop.getProperty("pattern.mobile"); 
			if(tel.contains("-")) {
				String code = tel.split("-")[0];
				check = prop.getProperty("pattern.mobile."+code);
			}
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(tel.replace("-", ""));
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		if(!flag){
			throw new I18nMessageException(10002, "手机号格式不正确!");
		}
	}
	
	/**
	 * 校验密码规则
	 * @param pwd
	 * @return false true
	 */
	public static boolean isCorrectPwd(String pwd){
		boolean flag = false;
		try {
			Properties prop = PropertiesManager.smartloadProperties(profile);
			String check = prop.getProperty("pattern.pwd");
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(pwd);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
}
