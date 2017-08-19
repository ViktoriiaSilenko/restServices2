package it.discovery.services;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Controller
@ResponseBody
@RequestMapping("/api")
public class DateTimeController {

    @RequestMapping("/date")
    public String getCurrentLocalDate() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        df.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));

        return df.format(date);

    }

    @RequestMapping("/time")
    public String  getCurrentLocalTime() {

        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

}
