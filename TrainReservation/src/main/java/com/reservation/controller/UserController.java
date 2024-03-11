package com.reservation.controller;

import com.reservation.model.Reservation;
import com.reservation.model.Train;
import com.reservation.model.User;
import com.reservation.repository.ReservationRepository;
import com.reservation.repository.TrainRepository;
import com.reservation.repository.UserRepository;
import com.reservation.service.EmailService;
import com.reservation.service.ReservationService;
import com.reservation.service.TrainService;
import com.reservation.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TrainService trainService;
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private EmailService emailService;

    private final Reservation reservation = new Reservation();
    private final Train train = new Train();
    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static String TO = "omkarkhade76@gmail.com";


    @RequestMapping("/user_home")
    public String home(Model model, Principal principal, HttpSession session) {
        String username = principal.getName();
        User user = userRepository.getUserByUsername(username);
        model.addAttribute("title", "Home - Train Reservation Portal");
        session.setAttribute("user", user);
        return "user_dashboard";
    }

    @RequestMapping("/user_view_trains")
    public String viewTrains(Model model) {
        List<Train> allTrains = trainService.getAllTrains();
        model.addAttribute("train", allTrains);
        return "user_view_trains";
    }

    @RequestMapping("/user_view_selected_train/{trainNo}")
    public String viewSelectTrain(@PathVariable("trainNo") Long trainNo, HttpSession session) {
        Train trainByTrainNo = trainService.findByTrainNo(trainNo);
        session.setAttribute("train", trainByTrainNo);
        return "view_train_details";
    }

    @RequestMapping("/user_booked_selected_train/{trainNo}")
    public String bookedTrainDetails(@PathVariable("trainNo") Long trainNo,
                                     HttpSession session, Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.getUserByUsername(username);
        Train train1 = trainService.findByTrainNo(trainNo);
        LocalDate now = LocalDate.now();
        session.setAttribute("user", user);
        session.setAttribute("train", train1);
        model.addAttribute("trainID", train1);
        model.addAttribute("date", now);
        model.addAttribute("reservation", new Reservation());
        return "book_train_now";
    }

    @RequestMapping("/user_do_reservation/{trainNo}")
    public String CompletePayment(@ModelAttribute("reservation") Reservation res,
                                  @PathVariable("trainNo") Long trainNo, Principal principal) {
        String username = principal.getName();
        User user = userRepository.getUserByUsername(username);
        Train trainDetails = trainService.findByTrainNo(trainNo);

        Integer totalSeats = trainDetails.getSeats();
        String seatCount = res.getSeatNo();
        int i = Integer.parseInt(seatCount);
        int totalAvailableSeats = totalSeats-i;
        System.out.println("Total Available Seats - " + totalAvailableSeats);
        trainDetails.setSeats(totalAvailableSeats);

        reservation.setReservationDate(res.getReservationDate());
        reservation.setSeatNo(res.getSeatNo());
        reservation.setTrainClass(res.getTrainClass());
        reservation.setTrainBerth(res.getTrainBerth());
        reservation.setTransactionID("TRD8346354GGT5566");
        reservation.setTrain(trainDetails);
        train.setId(trainDetails.getId());
        train.setSeats(totalAvailableSeats);
        train.setFare(trainDetails.getFare());
        train.setTrainName(trainDetails.getTrainName());
        train.setFromStation(trainDetails.getFromStation());
        train.setToStation(trainDetails.getToStation());
        train.setTrainNo(trainDetails.getTrainNo());

        List<Reservation> myReservation = new ArrayList<>();
        myReservation.add(reservation);
        user.setReservation(myReservation);
        reservation.setUser(user);
        return "redirect:/user_do_payment";
    }

    @RequestMapping("/user_do_payment")
    public String payment() {
        return "payment";
    }

    @RequestMapping("/user_complete_payment")
    public String doPayment(Principal principal, HttpSession session) throws Exception {
        try{
            String username = principal.getName();
            User user = userRepository.getUserByUsername(username);
            String seatNo = this.reservation.getSeatNo();
            Double fare = this.reservation.getTrain().getFare();
            double seatCount = Double.parseDouble(seatNo);
            double totalAmount = Math.round(fare*seatCount)*100.0/100.0;
            this.reservation.setTotalAmount(totalAmount);
            trainService.updateTrain(this.train);
            Reservation reser = reservationService.doReservation(this.reservation);

            if(reser == null){
                System.out.println("Reservation Not Completed");
                session.setAttribute("msg_alert","Reservation failed!! Please Try Again!");
                return "redirect:/user_do_payment";
            }
            else{
                System.out.println("Reservation Completed");
                session.setAttribute("msg_alert","Reservation Successful.");
                emailService.sendEmail("Train Reservation | "+LocalDate.now().toString(),
                        """
                        Your Train Reservation Details \n
                        Passenger Name -"""+user.getFirstName()+ """
                    \nPassenger Email -"""+user.getEmailID()+ """
                    \nPassenger Contact -"""+user.getContact()+ """
                    \n
                    \nTrain Details
                    \nTrain Number -"""+this.reservation.getTrain().getTrainNo()+"""
                    \nTrain Name -"""+this.reservation.getTrain().getTrainName()+"""
                    \nFrom Station Name -"""+this.reservation.getTrain().getFromStation()+"""
                    \nTo Station Name -"""+this.reservation.getTrain().getToStation()+"""
                    \nReservation Date -"""+this.reservation.getReservationDate()+"""
                    \nTrain Class -"""+this.reservation.getTrainClass()+"""
                    \nTrain Berth Preference -"""+this.reservation.getTrainBerth()+"""
                    """,TO);
                return "redirect:/user_home";
            }

        }catch (Exception e){
            return "redirect:/user_home";
        }
    }

    @RequestMapping("/user_trainBStation")
    public String trainBetweenStation(Model model){
        model.addAttribute("train",new Train());
        return "train_between_station";
    }

    @RequestMapping("/user_train_b_station")
    public String trainBStation(@ModelAttribute("train") Train train,
                                Model model, HttpSession session){
        model.addAttribute("train",new Train());
        Train trainDetails = trainService.findByFromStationToStation(train.getFromStation(), train.getToStation());
        if(trainDetails == null){
            System.out.println("Train details not found");
            session.setAttribute("msg_alert","Train Details Are Not Found!! Please Try Again!!");
            return "redirect:/user_trainBStation";
        }
        else{
            System.out.println("Train details found");
            session.setAttribute("train", trainDetails);
            model.addAttribute("trainModel", trainDetails);
            return "train_b_station";
        }
    }

    @RequestMapping("/user_train_booking_details")
    public String userTrainBookingDetails(Model model, Principal principal){
        String username = principal.getName();
        User user = userRepository.getUserByUsername(username);
        List<Reservation> reservationList = reservationService.getReservationByUser(user);
        model.addAttribute("reservation",reservationList);
        System.out.println(reservationList);
        return "booking_details";
    }

    @RequestMapping("/user_fare_enq_page")
    public String FareEnquiryPage(Model model){
        model.addAttribute("train", new Train());
        return "fare_enquiry_page";
    }
    @RequestMapping("/user_do_fare_enq_now")
    public String FareEnquiryDetails(@ModelAttribute("train") Train train,HttpSession session, Model model){
        Train trainDetails = trainService.findByFromStationToStation(train.getFromStation(), train.getToStation());
        session.setAttribute("train", trainDetails);
        model.addAttribute("trainModel", trainDetails);
        return "fare_enquiry_details";
    }

    @RequestMapping("/user_search_available_seat")
    public String searchAvailableSeat(Model model){
        model.addAttribute("train", new Train());
        return "seat_available_enquiry";
    }

    @RequestMapping("/user_search_available_details")
    public String seatAvailableDetails(@ModelAttribute("train") Train train,
                                       HttpSession session, Model model, Principal principal){
        String username = principal.getName();
        User userDetails = userRepository.getUserByUsername(username);
        Train trainDetails = trainService.findByTrainNo(train.getTrainNo());
        session.setAttribute("train", trainDetails);
        session.setAttribute("user", userDetails);
        model.addAttribute("trainModel", trainDetails);
        return "seat_available_details";
    }

    @RequestMapping("/user_search_train")
    public String userSearchTrain(Model model){
        model.addAttribute("train", new Train());
        return "user_search_train_page";
    }

    @RequestMapping("/user_do_search_train_details")
    public String userDoSearchTrain(@ModelAttribute("train") Train train,
                                    Model model, Principal principal, HttpSession session){
        String username = principal.getName();
        User userDetails = userRepository.getUserByUsername(username);
        Train trainDetails = trainService.findByTrainNo(train.getTrainNo());
        model.addAttribute("trainModel", trainDetails);
        session.setAttribute("user", userDetails);
        return "searched_train_details";
    }

    @RequestMapping("/user_profile")
    public String userProfile(Principal principal, Model model){
        String username = principal.getName();
        User user = userRepository.getUserByUsername(username);
        model.addAttribute("user", user);
        return "user_profile";
    }

    @RequestMapping("/user_view_profile")
    public String viewUserProfile(Principal principal, Model model, HttpSession session){
        String username = principal.getName();
        User user = userRepository.getUserByUsername(username);
        model.addAttribute("user", user);
        session.setAttribute("user", user);
        return "view_user_profile";
    }

    @RequestMapping("/user_edit_profile")
    public String editUserProfile(Principal principal, Model model, HttpSession session){
        String username = principal.getName();
        User user = userRepository.getUserByUsername(username);
        model.addAttribute("user", user);
        session.setAttribute("user", user);
        return "edit_user_profile";
    }

    @RequestMapping("/user_do_update_profile")
    public String updateUserProfile(@ModelAttribute("user") User user,
                                    HttpSession session, Principal principal, Model model){
        model.addAttribute("user", new User());
        String username = principal.getName();
        User userDetails = userRepository.getUserByUsername(username);
        user.setProfile(userDetails.getProfile());
        user.setRole(userDetails.getRole());
        User save = userService.createUser(user);
        if(save == null){
            System.out.println("User not updated!!");
            session.setAttribute("msg_alert", "User not updated!! Please try again!!");
            model.addAttribute("user", user);
            return "redirect:/user_edit_profile";
        }
        else{
            System.out.println("User updated successfully");
            session.setAttribute("msg_alert", "User updated successfully.");
            model.addAttribute("user", user);
            return "redirect:/user_profile";
        }
    }

    @RequestMapping("/user_change_password")
    public String changePasswordView(Model model){
        model.addAttribute("user", new User());
        return "change_password";
    }

    @RequestMapping("/user_do_change_password")
    public String changeUserPassword(@ModelAttribute("user") User user,
                                     Model model,
                                     Principal principal,
                                     HttpSession session){
        String username = principal.getName();
        User userDetails = userRepository.getUserByUsername(username);
        userDetails.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getEmailID().equals(userDetails.getEmailID())){
            User savedUser = userService.createUser(userDetails);
            if(savedUser == null){
                System.out.println("password not updated!!");
                model.addAttribute("user", new User());
                session.setAttribute("msg_alert","Password Not Updated!! Please try again!!");
                return "redirect:/user_change_password";
            }
            else {
                System.out.println("Password updated.");
                model.addAttribute("user", new User());
                session.setAttribute("msg_alert","Password Updated Successfully.");
                return "redirect:/user_profile";
            }
        }
        else{
            System.out.println("Email not verified");
            session.setAttribute("msg_alert","Email id not verified!! Please try same email ID!!");
            return "redirect:/user_profile";
        }
    }

}
