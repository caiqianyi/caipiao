package com.ct.commons.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

/**
 * 一些常用的工具类
 * @author xhl
 *
 */

public class CommonUtil {
	 
	/**
	 * 获得远程充值接口sign
	 * @param map
	 * @param scrit
	 * @return
	 */
	public static String getMD5Sign(TreeMap<String,Object> map,String scrit){
		String secret="";
		for (Map.Entry<String, Object> entry : map.entrySet()) {  
			secret+=entry.getKey();
			secret+=entry.getValue();
		}
		
		return MD5.md5(secret+scrit);		
	}
	
	
	/**
	 * 新版获得远程充值接口sign
	 * @param map
	 * @param scrit
	 * @param charset
	 * @return
	 */
	public static String getNewMD5Sign(TreeMap<String,Object> map,String scrit,String charset){
		String secret="";
		for (Map.Entry<String, Object> entry : map.entrySet()) {  
			secret+=entry.getKey();
			secret+=entry.getValue();
		}
		
		return MD5.md5(secret+scrit,charset);		
	}
	
	/**
	 * 得到md5加密密码
	 * @param str
	 * @return
	 */
	public static String getMd5Password(String str){
		if(!"".equals(str) && str!=null){
			return MD5.md5(str);
		}
		return null;
	}
	
	/**
	 * 获得登录验证码
	 * @param output
	 * @return
	 */
    public static String getVerifyCode(ByteArrayOutputStream output) {
		String code = "";
		for (int i = 0; i < 4; i++) {
			code += randomChar();
		}
		int width = 70;
		int height = 25;
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Font font = new Font("Times New Roman", Font.PLAIN, 20);
		Graphics2D g = bi.createGraphics();
		g.setFont(font);
		Color color = new Color(66, 2, 82);
		g.setColor(color);
		g.setBackground(new Color(226, 226, 240));
		g.clearRect(0, 0, width, height);
		FontRenderContext context = g.getFontRenderContext();
		Rectangle2D bounds = font.getStringBounds(code, context);
		double x = (width - bounds.getWidth()) / 2;
		double y = (height - bounds.getHeight()) / 2;
		double ascent = bounds.getY();
		double baseY = y - ascent;
		g.drawString(code, (int) x, (int) baseY);
		g.dispose();
		try {
			ImageIO.write(bi, "jpg", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}

	private static char randomChar() {
		Random r = new Random();
		String s = Constant.SYS_VERIFYCODE_FORM;
		return s.charAt(r.nextInt(s.length()));
	}
	
	/**
	 * @param files    要打包的文件数组
	 * @param pathzip  打包的文件绝对地址
	 * @return 打包的文件绝对地址
	 * @throws IOException
	 */
	public static String zipFile(List<File> files, String pathzip) throws IOException{
		byte[] buffer = new byte[1024];
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(pathzip));
		// 需要同时下载的两个文件result.txt ，source.txt
		for (int i = 0; i < files.size(); i++) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(files.get(i));
			} catch (FileNotFoundException e) {
				continue;
			}
			out.putNextEntry(new ZipEntry(files.get(i).getName()));
			int len;
			// 读入需要下载的文件的内容，打包到zip文件
			while ((len = fis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			out.closeEntry();
			fis.close();
		}
		out.close();
		return pathzip;
	}
	
	 /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
     try {
    	 Integer.parseInt(value);
    	 return true;
     } catch (NumberFormatException e) {
    	 return false;
     }
    }
}
