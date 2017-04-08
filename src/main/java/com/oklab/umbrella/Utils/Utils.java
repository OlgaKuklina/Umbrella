package com.oklab.umbrella.Utils;

import java.util.Collection;

/**
 * Created by olgakuklina on 2017-04-08.
 */

public class Utils {
    public static double stddev(Collection<Double> points) {
        double avg = avg(points);
        double sum = 0.0D;
        for (double p : points) {
            double d = p - avg;
            sum += d / (points.size() - 1) * d;
        }

        return Math.sqrt(sum);
    }

    private static double avg(Collection<Double> points) {
        double ave = 0.0D;
        for (double p : points) {
            ave += p / points.size();
        }

        return ave;
    }

    public static double celsiusToFahrenheit (double c) {
        return c * 1.8 + 32;
    }
}
