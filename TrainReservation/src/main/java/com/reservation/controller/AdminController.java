package com.reservation.controller;

import com.reservation.model.Train;
import com.reservation.model.User;
import com.reservation.repository.UserRepository;
import com.reservation.service.TrainService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class AdminController{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainService trainService;

    @RequestMapping("/admin_home")
    public String home(Model model, Principal principal, HttpSession session){
        String username = principal.getName();
        User user = userRepository.getUserByUsername(username);
        model.addAttribute("title", "Home - Admin Portal");
        session.setAttribute("user", user);
        return "admin_dashboard";
    }

    @RequestMapping("/admin_add_train")
    public String registration(Model model) {
        model.addAttribute("title", "Add Train - Train Reservation Portal");
        model.addAttribute("train", new Train());
        return "add_trains";
    }

    @RequestMapping("/add_train_now")
    public String add_train(@ModelAttribute("train") Train train, Model model,
                            HttpSession session){
        boolean b = trainService.checkTrainName(train.getTrainName());
        boolean c = trainService.checkTrainNo(train.getTrainNo());
        System.out.println(b + " " + c);
        if(b || c){
            session.setAttribute("msg_alert", "Train Name OR Number is Already Existed!");
            model.addAttribute("train", train);
            return "add_trains";
        }
        else{
            Train savedTrain = trainService.addTrain(train);
            if (savedTrain == null) {
                session.setAttribute("msg_alert", "Train Details Not Added! Please try again!");
                model.addAttribute("train", train);
                return "add_trains";
            } else {
                session.setAttribute("msg_alert", "Train Details added Successfully.");
            List<Train> allTrains = trainService.getAllTrains();
            model.addAttribute("trains", allTrains);
            return "view_trains";
            }
        }
    }

    @RequestMapping("/admin_view_train")
    public String view_trains(Model model){
        List<Train> allTrains = trainService.getAllTrains();
        model.addAttribute("trains", allTrains);
        return "view_trains";
    }

    @RequestMapping("/admin_train_update/{trainNo}")
    public String admin_update_train(@PathVariable("trainNo") Long trainNo, Model model){
        Train trainByTrainNo = trainService.findByTrainNo(trainNo);
        System.out.println(trainByTrainNo);
        model.addAttribute("train", trainByTrainNo);
        return "update_train";
    }

    @RequestMapping("/update_train_now")
    public String update_train(@ModelAttribute("train") Train train, Model model){
        Train train1 = trainService.updateTrain(train);
        List<Train> allTrains = trainService.getAllTrains();
        model.addAttribute("trains", allTrains);
        return "view_trains";
    }

    @RequestMapping("/admin_search_train_page")
    public String searchTrainPage(Model model){
        model.addAttribute("train", new Train());
        return "admin_search_train";
    }

    @RequestMapping("/search_train")
    public String searchTrain(@ModelAttribute("train") Train train, HttpSession session){
        Train train1 = trainService.findByTrainNo(train.getTrainNo());
        if(train1==null){
            session.setAttribute("msg_alert", "Train No." + train.getTrainNo() + " is Not Available !");
            session.setAttribute("train", null);
        }
        else{
            session.setAttribute("train", train1);
        }
        return "view_searched_train";
    }

    @RequestMapping("/admin_delete_train_page")
    public String deleteTrainPage(Model model){
        model.addAttribute("train", new Train());
        return "delete_train";
    }

    @RequestMapping("/do_delete_train")
    public String deleteTrain(@ModelAttribute("train") Train train, Model model){
        int b = trainService.deleteTrain(train.getTrainNo());
        if(b == 1){
            System.out.println("Train deleted");
        }
        else{
            System.out.println("Not Delete");
        }
        model.addAttribute("train", train);
        List<Train> allTrains = trainService.getAllTrains();
        model.addAttribute("trains", allTrains);
        return "view_trains";
    }


}
