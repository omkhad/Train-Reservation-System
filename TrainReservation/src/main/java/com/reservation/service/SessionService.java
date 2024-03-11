package com.reservation.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
public class SessionService {

    public void removeMessageFromSession(){
        try{
            HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
            session.removeAttribute("msg_alert");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
