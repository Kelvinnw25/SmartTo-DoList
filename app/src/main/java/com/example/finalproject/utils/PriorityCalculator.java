package com.example.finalproject.utils;

import com.example.finalproject.model.Task;
import java.util.concurrent.TimeUnit;

public class PriorityCalculator     {
    //constant
    private static final double WEIGHT_IMPORTANCE = 5.0;
    private static final double WEIGHT_TIME = 100.0;

    //static method for calculate priority score
    public static double calculateScore(Task task) {

        //for completed task
        if (task.isCompleted()) {
            return 0.0;
        }

        //time difference in milliseconds
        long currentTimeMillis = System.currentTimeMillis();
        long deadlineMillis = task.getDeadlineTimestamp();
        long diffMillis = deadlineMillis - currentTimeMillis;

        //handle task that overdue
        if (diffMillis <= 0) {
            //return the highest score
            return Double.MAX_VALUE;
        }

        //convert to day
        double daysRemaining = (double) diffMillis / TimeUnit.DAYS.toMillis(1);

        //calculate priority components
        //importance component
        double importanceComponent = task.getImportanceLevel() * WEIGHT_IMPORTANCE;

        //time component
        //+1 for avoiding n/0 and give the highest score to assignments with less than 1 day left
        double timeComponent = WEIGHT_TIME / (daysRemaining + 1);

        //total score
        double finalScore = importanceComponent + timeComponent;

        return finalScore;
    }

}
