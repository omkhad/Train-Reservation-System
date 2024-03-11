package com.reservation.controller;

import com.reservation.model.ContactUs;
import com.reservation.model.Otp;
import com.reservation.model.User;
import com.reservation.repository.UserRepository;
import com.reservation.service.EmailService;
import com.reservation.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Random;

@Controller
public class HomeController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public static String MY_GENERATED_OTP;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

    public static String TO = "omkarkhade76@gmail.com";
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String go_home(Model model) {
        model.addAttribute("title", "Home - Train Reservation Portal");
        return "usermain";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Home - Train Reservation Portal");
        return "home";
    }

    @RequestMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("title", "Registration - Train Reservation Portal");
        model.addAttribute("user", new User());
        return "user_registration";
    }

    @RequestMapping("/admin_login")
    public String admin_login(Model model) {
        model.addAttribute("title", "Registration - Train Reservation Portal");
        return "admin_login";
    }
    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "Registration - Train Reservation Portal");
        return "about";
    }
    @RequestMapping("/alert")
    public String alert(Model model) {
        model.addAttribute("title", "Registration - Train Reservation Portal");
        return "alert";
    }
    @RequestMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "Contact - Train Reservation Portal");
        model.addAttribute("contact", new ContactUs());
        return "contact";
    }

    @RequestMapping("/contact_us")
    public String contactUs(@ModelAttribute("contact") ContactUs contactUs,
                            HttpSession session){
        ContactUs cont = new ContactUs();
        cont.setFullName(contactUs.getFullName());
        cont.setEmail(contactUs.getEmail());
        cont.setContactNo(contactUs.getContactNo());
        cont.setComments(contactUs.getComments());
        emailService.sendEmail("PASSENGER RESERVATION ENQUIRY", """
                \nPassenger Name - """+cont.getFullName()+"""
                \nPassenger Email - """+cont.getEmail()+"""
                \nPassenger Contact - """+cont.getContactNo()+"""
                \nPassenger Query - """+cont.getComments()+"""
                """, TO);
        session.setAttribute("msg_alert","Enquiry Successfully Submitted.");
        return "redirect:/";
    }

    @RequestMapping(value = "/do_registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("user") User user, Model model,
                               HttpServletResponse res, HttpSession session,
                               @RequestParam("image") MultipartFile file) throws IOException {
        boolean f = userService.checkEmail(user.getEmailID());
        if (f) {
            System.out.println("Email id already Exist");
            session.setAttribute("msg_alert", "Email Id already Existed!!");
            return "redirect:/registration";
        } else {
            System.out.println("User details : " + user);
            String password = passwordEncoder.encode(user.getPassword());
            user.setRole("ROLE_USER");
            user.setEnable(true);
            user.setPassword(password);
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());
            model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
            user.setProfile(file.getOriginalFilename());
            User savedUser = this.userService.createUser(user);
            if (savedUser == null) {
                System.out.println("User null found");
                return "redirect:/registration";
            } else {
                session.setAttribute("msg_alert", "Account Successfully Created.");
                model.addAttribute("user", new User());
                return "redirect:/home";
            }
        }
    }

//    @RequestMapping(value = "/do_admin_registration")
//    public String admin_registration(Model model, HttpSession session) {
//        User user = new User();
//        user.setFirstName("Admin");
//        user.setLastName("Dashboard");
//        user.setEmailID("admin@dashboard.com");
//        user.setContact("986735345");
//        user.setAddress("Admin Address");
//        String password = passwordEncoder.encode("admin123");
//        user.setRole("ROLE_ADMIN");
//        user.setEnable(true);
//        user.setPassword(password);
//
//        boolean f = userService.checkEmail(user.getEmailID());
//        if (f) {
//            System.out.println("Email id already Exist");
//            session.setAttribute("msg_alert", "Admin Details already Existed!!");
//            return "redirect:/home";
//        }
//        else {
//            User savedUser = this.userService.createUser(user);
//            if (savedUser == null) {
//                System.out.println("User null found");
//                return "redirect:/registration";
//            } else {
//                session.setAttribute("msg_alert", "successfully register!");
//                System.out.println("User found");
//                model.addAttribute("user", new User());
//                return "redirect:/home";
//            }
//        }
//    }

    @RequestMapping("/send_otp")
    public String sendOTP(Model model){
        Random rand = new Random();
        int i = rand.nextInt(10000);
        MY_GENERATED_OTP = ""+i;
        emailService.sendEmail("Admin OTP", "Your One-Time Password is - "+MY_GENERATED_OTP, TO);
        model.addAttribute("otp", new Otp());
        return "send_OTP";
    }

    @RequestMapping("/verify_opt")
    public String verifyOTP(@ModelAttribute("otp") Otp otp, HttpSession session, Model model){
        if(otp.getOtp().equals(MY_GENERATED_OTP)){
            System.out.println("OTP VERIFIED");
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Dashboard");
            user.setEmailID("admin@gmail.com");
            user.setContact("986735345");
            user.setAddress("Admin Address");
            String password = passwordEncoder.encode("admin");
            user.setRole("ROLE_ADMIN");
            user.setEnable(true);
            user.setPassword(password);

            boolean f = userService.checkEmail(user.getEmailID());
            if (f) {
                System.out.println("Email id already Exist");
                session.setAttribute("msg_alert", "Admin Details already Existed!! Details are sent to the register admin mail ID.");
                emailService.sendEmail("Admin Credentials", """
                        Your Admin Details \s
                        Username - admin@dashboard.com \n
                        Password - admin123
                        """, TO);
                return "redirect:/home";
            }
            else {
                User savedUser = this.userService.createUser(user);
                if (savedUser == null) {
                    System.out.println("User null found");
                    return "redirect:/registration";
                } else {
                    session.setAttribute("msg_alert", "Admin Credentials Created Successfully. Details are sent to the registered admin email ID.");
                    System.out.println("User found");
                    model.addAttribute("user", new User());
                    emailService.sendEmail("Admin Credentials", """
                        Your Admin Details \s
                        Username - admin@gmail.com \n
                        Password - admin
                        """, TO);
                    return "redirect:/home";
                }
            }
        }
        else{
            System.out.println("OTP NOT VERIFIED");
            return "redirect:/home";
        }
    }

}
