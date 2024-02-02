package com.example.auth.service;

import com.example.auth.configuration.EmailConfiguration;
import com.example.auth.model.UserEntity;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final EmailConfiguration emailConfiguration;

    @Value("${front.url}")
    private String frontendUrl;

    @Value("classpath:static/mail-activate.html")
    private Resource activeTemplate;
    @Value("classpath:static/reset-password.html")
    private Resource recoveryTemplate;

    public void sendActivation(UserEntity user){
        log.info("--START sendActivation");
        try {
            String html = Files.toString(activeTemplate.getFile(), Charsets.UTF_8);
            html = html.replace("https://google.com",frontendUrl+"/activate/"+user.getUuid());
            emailConfiguration.sendMail(user.getEmail(), html,"Account activation",true);
        } catch (IOException e){
            log.info("Cant send mail");
            throw new RuntimeException(e);
        }
        log.info("--STOP sendActivation");
    }

    public void sendPasswordRecovery(UserEntity user,String uid){
        try {
            log.info("--START sendPasswordRecovery");
            String html = Files.toString(recoveryTemplate.getFile(), Charsets.UTF_8);
            html = html.replace("https://google.com",frontendUrl+"/recover-password/"+uid);
            emailConfiguration.sendMail(user.getEmail(), html,"Password Recovery.",true);
        } catch (IOException e){
            log.info("Cant send mail");
            throw new RuntimeException(e);
        }
        log.info("--STOP sendPasswordRecovery");
    }

}
