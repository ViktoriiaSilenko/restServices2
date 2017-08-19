package it.discovery.services;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@ResponseBody
@RequestMapping("/api")
public class DateTimeController {

    @RequestMapping("/date")
    public String getCurrentLocalDate() {
        return LocalDate.now().toString();
    }

    @RequestMapping("/time")
    public String  getCurrentLocalTime() {
        return LocalTime.now().toString();
    }

}
