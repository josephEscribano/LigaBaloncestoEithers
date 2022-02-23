package quevedo.servidorLiga.service;

import jakarta.inject.Inject;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import quevedo.servidorLiga.config.Configuration;
import quevedo.servidorLiga.service.utils.ConstantesService;

import java.util.Properties;

public class MandarMail {

    private final Configuration configuration;

    @Inject
    public MandarMail(Configuration configuration) {
        this.configuration = configuration;
    }


    public void generateAndSendEmail(String to, String msg, String subject) throws MessagingException {
        Properties mailServerProperties;
        Session getMailSession;
        MimeMessage generateMailMessage;


        mailServerProperties = System.getProperties();
        mailServerProperties.put(ConstantesService.PROPERTIES_MAIL_SMTP_PORT, Integer.parseInt(ConstantesService.PROPERTIES_PUERTO));
        mailServerProperties.put(ConstantesService.PROPERTIE_MAIL_SMTP_AUTH, ConstantesService.PROPERTIE_VALUE);
        mailServerProperties.put(ConstantesService.PROPERTIE_SSL, ConstantesService.PROPERTIE_VALUE_SSL);
        mailServerProperties.put(ConstantesService.MAIL_SMTP_STARTTLS_ENABLE, ConstantesService.PROPERTIE_VALUE);


        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        generateMailMessage.setSubject(subject);
        String emailBody = msg;
        generateMailMessage.setContent(emailBody, ConstantesService.TEXT_HTML);


        Transport transport = getMailSession.getTransport(ConstantesService.SMTP);
        transport.connect(configuration.getHost(),
                configuration.getUserMail(),
                configuration.getPasswordMail());
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }
}

