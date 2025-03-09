package com.example.addressBookService.Component;

import com.example.addressBookService.DTO.*;
import com.example.addressBookService.Interfaces.IAuthInterface;
import com.example.addressBookService.Services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;

@RestController
@Slf4j
public class UserController {

    @Autowired
    EmailService emailService;

    @Autowired
    IAuthInterface iAuthInterface;

    //UC9 --> For Registration of a user
    @PostMapping(path = "/register")
    public String register(@Valid @RequestBody AuthUserDTO user) throws Exception {

        log.info("Employee tried to register with body: {}", getJSON(user));

        return iAuthInterface.register(user);
    }

    //UC10 --> For User Login
    @PostMapping(path ="/login")
    public String login(@Valid @RequestBody LoginDTO user){

        log.info("Employee tried to login with body: {}", getJSON(user));

        return iAuthInterface.login(user);
    }

    //UC11 --> For sending mail to another person
    @PostMapping(path = "/sendMail")
    public String sendMail(@Valid @RequestBody MailDTO message) {

        log.info("Employee tried to send email with body: {}", getJSON(message));

        emailService.sendEmail(message.getTo(), message.getSubject(), message.getBody());

        return "Mail sent";
    }

    //UC12 --> Added Swagger Config to use Swagger at url(/swagger)


    //UC13 --> Added forgot password functionality
    @PutMapping("/forgotPassword/{email}")
    public AuthUserDTO forgotPassword(@Valid @RequestBody PassDTO pass, @Valid @PathVariable String email) throws Exception {

        log.info("Employee applied for forgot password with body: {}", getJSON(pass));

        return iAuthInterface.forgotPassword(pass, email);
    }

    //UC14 --> Added reset password functionality
    @PutMapping("/resetPassword/{email}")
    public String resetPassword(@Valid @PathVariable String email ,@Valid @RequestParam String currentPass,@Valid @RequestParam String newPass) throws Exception {

        log.info("Employee applied for forgot password with email: {}", email);

        return iAuthInterface.resetPassword(email, currentPass, newPass);
    }


    @GetMapping("/clear")
    public String clear(){
        log.info("Database clear request made ");
        return iAuthInterface.clear();
    }

    public String getJSON(Object object){
        try {
            ObjectMapper obj = new ObjectMapper();
            return obj.writeValueAsString(object);
        }
        catch(JsonProcessingException e){
            log.error("Reason : {} Exception : {}", "Conversion error from Java Object to JSON");
        }
        return null;
    }

}