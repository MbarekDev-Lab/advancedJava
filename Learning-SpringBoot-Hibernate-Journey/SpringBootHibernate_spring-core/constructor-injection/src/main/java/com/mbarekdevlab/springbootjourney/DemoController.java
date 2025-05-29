package com.mbarekdevlab.springbootjourney;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    private final Coach coach;

    @Autowired // can be omitted in Spring Boot 2.6+
    public DemoController(Coach coach) {
        this.coach = coach;
    }

    @GetMapping("/dailytraining")
    public String getDailytraining() {
        return coach.getDailyWorkout();
    }

}
