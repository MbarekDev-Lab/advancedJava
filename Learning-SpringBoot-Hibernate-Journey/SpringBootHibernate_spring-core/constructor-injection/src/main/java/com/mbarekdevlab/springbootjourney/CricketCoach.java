package com.mbarekdevlab.springbootjourney;

import org.springframework.stereotype.Component;

@Component
public class CricketCoach implements Coach{
    @Override
    public String getDailyWorkout() {
        return "Practice Java every day!!!!!!";
    }
}
