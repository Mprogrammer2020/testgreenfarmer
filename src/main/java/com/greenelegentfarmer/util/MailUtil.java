package com.greenelegentfarmer.util;

import java.util.Optional;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.model.EmailDetails;
import com.greenelegentfarmer.repository.UserRepository;
import com.greenelegentfarmer.service.UserService;

@Component
public class MailUtil {

	@Value("${spring.mail.username}")
	private String sender;

	@Autowired
	private TemplateEngine engine;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordUtil passwordUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Async
	public void sendAccountActivationMail(User user) throws MessagingException {
		Context context=new Context();
		context.setVariable("user", user);
		String body=engine.process("mail/account_activation", context);
		sendMail(new EmailDetails(user.getEmail()).setMsgBody(body).setSubject("testing mail"));
	}

	// To send an email with attachment
	private void sendMail(EmailDetails details) throws MessagingException {
		// Creating a mime message
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;

		// Setting multipart as true for attachments to be sent
		mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		mimeMessageHelper.setFrom(sender);
		mimeMessageHelper.setTo(details.getRecipient());
		mimeMessageHelper.setText(details.getMsgBody(),true);
		mimeMessageHelper.setSubject(details.getSubject());

		// Adding the attachment
		//FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

		//mimeMessageHelper.addAttachment(file.getFilename(), file);

		// Sending the mail
		javaMailSender.send(mimeMessage);
		System.out.println("Mail sent Successfully");
	}

	//to send login credentials to newly created subadmin
	public void sendLoginCredentials(User user) throws MessagingException {
		
		String  userPassword = passwordUtil.generatePassayPassword();
		
		Context context=new Context();
		context.setVariable("user", user);
		context.setVariable("password", userPassword);
		String body=engine.process("mail/account_activation", context);
		sendMail(new EmailDetails(user.getEmail()).setMsgBody(body).setSubject("Login Credentials"));
		
		Optional<User> userOpt = userService.getByUsername(user.getEmail());
		if(userOpt.isPresent()) {
			User mailUser = userOpt.get();
			mailUser.setPassword(passwordEncoder.encode(userPassword));
			userService.add(mailUser);
		}
	}
	
	
}
