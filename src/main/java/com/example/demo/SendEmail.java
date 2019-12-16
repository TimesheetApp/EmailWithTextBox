package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service
class SendEmail {

    private TemplateEngine templateEngine; //Library imported

    @Autowired
    Environment environment; //Library imported

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired //constructor
    public SendEmail(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    private Properties GetProperties() {  //properties class imported

        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", environment.getProperty("mail.smtp.starttls.enable"));
        properties.put("mail.smtp.auth", environment.getProperty("mail.smtp.auth"));
        properties.put("mail.smtp.host", environment.getProperty("mail.smtp.host"));
        properties.put("mail.smtp.port", environment.getProperty("mail.smtp.port"));
        properties.put("mail.smtp.ssl.trust", environment.getProperty("mail.smtp.ssl.trust"));

        return properties;

    }

    private Session GetSession() {

        final String username = "supersupervisor89@gmail.com";
        final String password = "superpassword";

        //Create session
        Session session = Session.getInstance(GetProperties(), new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        return session;
    }

    public String buildTemplateWithContent(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mailtemplate", context);
    }

    public Model SendSimpleEmail(Model model, @PathVariable("id") long userId,
                                 EmailSet emailSet) {
        try {
            MimeMessage message = new MimeMessage(GetSession());

            //Get current user email
            User currentUser = userService.getAuthenticatedUser();
            String currentUserEmail = currentUser.getEmail();
            model.addAttribute("email", currentUserEmail);

            //Send email to the currentUser's email (In this case the supervisor)
            message.setFrom(new InternetAddress(currentUserEmail));
            
            //Find employee's email - We need to use the repository that stores de employee email
            User empEmail = userRepository.findById(userId).get();
            String email = empEmail.getEmail();
            model.addAttribute("emails", email);
            
            
            //Set the employee email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));



            //email Subject
            message.setSubject("Timesheet Status");

            //Email content - Hard coded message
            //message.setText("Your timesheet has been rejected");

            //Email content - Non hardcoded message
            message.setText(emailSet.getReasonText(), "UTF-8");

            System.out.println(emailSet.getReasonText());


            Transport.send(message);

            return model;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}



