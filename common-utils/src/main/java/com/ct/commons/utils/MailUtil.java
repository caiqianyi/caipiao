package com.ct.commons.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 发送邮件工具包
 * @author xhl
 *
 */
public class MailUtil {

	public static boolean  sendMail(String smtp,String fromUser,String fromPassword,String mailTitle,String mailBody,String mailTo[]){
		boolean flag=true;
		
		Properties prop = new Properties();
		prop.setProperty("mail.host", smtp);
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.smtp.auth", "true");
		// 使用JavaMail发送邮件的5个步骤
		// 1、创建session
		Session session = Session.getInstance(prop);
		// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
		session.setDebug(true);
		// 2、通过session得到transport对象
		try {
			Transport ts = session.getTransport();
			// 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
			ts.connect(smtp, fromUser, fromPassword);
			// 4、创建邮件
			Message message = createSimpleMail(session,fromUser,mailTitle,mailBody,mailTo);
			// 5、发送邮件
			ts.sendMessage(message, message.getAllRecipients());
			ts.close();
		} catch (NoSuchProviderException e) {
			flag=false;
		} catch (MessagingException e) {
			flag=false;
		} catch (Exception e) {
			flag=false;
		}
		
		return flag;
	}
	
	
	/**
	 * 多个发件人转化string
	 * @param mailArray
	 * @return
	 */
	private static String getMailList(String[] mailArray) {
		StringBuffer toList = new StringBuffer();
		int length = mailArray.length;
		if (mailArray != null && length < 2) {
			toList.append(mailArray[0]);
		} else {
			for (int i = 0; i < length; i++) {
				toList.append(mailArray[i]);
				if (i != (length - 1)) {
					toList.append(",");
				}
			}
		}
		return toList.toString();
	}
	
	public static MimeMessage createSimpleMail(Session session,String fromUser,String mailTitle,String mailBody,String[] mailTo) throws Exception {
		// 创建邮件对象
		MimeMessage message = new MimeMessage(session);
		// 指明邮件的发件人
		message.setFrom(new InternetAddress(fromUser));
		
		String toList = getMailList(mailTo);  
		InternetAddress[] iaToList = new InternetAddress().parse(toList);  

		message.setRecipients(Message.RecipientType.TO, iaToList);
		// 邮件的标题
		message.setSubject(mailTitle);
		
		// 邮件的文本内容		
		message.setContent(mailBody, "text/html;charset=UTF-8");
		// 返回创建好的邮件对象
		return message;
	}
}
