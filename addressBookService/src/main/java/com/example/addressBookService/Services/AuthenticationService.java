package com.example.addressBookService.Services;

import com.example.addressBookService.DTO.AuthUserDTO;
import com.example.addressBookService.DTO.LoginDTO;
import com.example.addressBookService.DTO.PassDTO;
import com.example.addressBookService.Interfaces.IAuthInterface;
import com.example.addressBookService.Entity.AuthUser;
import com.example.addressBookService.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
@Slf4j
public class AuthenticationService implements IAuthInterface {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtTokenService jwtTokenService;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public String register(AuthUserDTO user) {
        try {
            List<AuthUser> l1 = userRepository.findAll().stream().filter(authuser -> user.getEmail().equals(authuser.getEmail())).collect(Collectors.toList());

            if (l1.size() > 0) {
                throw new RuntimeException();
            }

            //creating hashed password using bcrypt
            String hashPass = bCryptPasswordEncoder.encode(user.getPassword());

            //creating new user
            AuthUser newUser = new AuthUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), hashPass);

            //setting the new hashed password
            newUser.setHashPass(hashPass);

            //saving the user in the database
            userRepository.save(newUser);

            log.info("User saved in database : {}", getJSON(newUser));

            //sending the confirmation mail to the user
            emailService.sendEmail(user.getEmail(), "Your Account is Ready!", "UserName : " + user.getFirstName() + " " + user.getLastName() + "\nEmail : " + user.getEmail() + "\nYou are registered!\nBest Regards,\nBridgelabz Team");

            return "user registered";
        }
        catch(RuntimeException e){
            log.error("User already registered with email: {} Exception : {}", user.getEmail(), e);
        }
        return null;
    }


    public String login(LoginDTO user){
        try {
            List<AuthUser> l1 = userRepository.findAll().stream().filter(authuser -> authuser.getEmail().equals(user.getEmail())).collect(Collectors.toList());
            if (l1.size() == 0) {
                throw new RuntimeException();
            }
            AuthUser foundUser = l1.get(0);

            //matching the stored hashed password with the password provided by user

            if (!bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getHashPass())) {
                log.error("Invalid password entered for email {} where entered password is {}", user.getEmail(), user.getPassword());
                return "Invalid password";
            }

            //creating Jwt Token
            String token = jwtTokenService.createToken(foundUser.getId());

            //setting token for user login
            foundUser.setToken(token);

            //saving the current status of user in database
            userRepository.save(foundUser);

            log.info("User logged in with email {}", user.getEmail());

            return "user logged in" + "\ntoken : " + token;
        }
        catch(RuntimeException e){
            log.error("User already registered with email: {} Exception : {}", user.getEmail(), e);
        }
        return null;

    }

    public AuthUserDTO forgotPassword(PassDTO pass, String email){
        try {
            AuthUser foundUser = userRepository.findByEmail(email);

            if (foundUser == null) {
                throw new RuntimeException();
            }
            String hashpass = bCryptPasswordEncoder.encode(pass.getPassword());

            foundUser.setPassword(pass.getPassword());
            foundUser.setHashPass(hashpass);

            log.info("Hashpassword : {} for password : {} saved for user: {}", hashpass, pass.getPassword(),getJSON(foundUser));

            userRepository.save(foundUser);

            emailService.sendEmail(email, "Password Forgot Status", "Your password has been changed!");

            AuthUserDTO resDto = new AuthUserDTO(foundUser.getFirstName(), foundUser.getLastName(), foundUser.getEmail(), foundUser.getPassword(), foundUser.getId());

            return resDto;
        }
        catch(RuntimeException e){
            log.error("user not registered with email: {} Exception : {}", email, e);
        }
        return null;
    }

    public String resetPassword(String email, String currentPass, String newPass) {

        AuthUser foundUser = userRepository.findByEmail(email);
        if (foundUser == null)
            return "user not registered!";

        if (!bCryptPasswordEncoder.matches(currentPass, foundUser.getHashPass()))
            return "incorrect password!";

        String hashpass = bCryptPasswordEncoder.encode(newPass);

        foundUser.setHashPass(hashpass);
        foundUser.setPassword(newPass);

        userRepository.save(foundUser);

        log.info("Hashpassword : {} for password : {} saved for user : {}", hashpass, newPass, getJSON(foundUser));

        emailService.sendEmail(email, "Password reset status", "Your password is reset successfully");

        return "Password reset successfull!";

    }

    public String clear(){

        userRepository.deleteAll();
        log.info("all data inside db is deleted");

        return "Database cleared";
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