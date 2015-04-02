package com.Andre;

import java.util.Date;

/**
 * Created by Andre on 3/30/2015.
 */
public class WaterHeater extends ServiceCall {
    //adds the aage of the water heater and
    //a static city fee of $20
    private int waterHeaterAge;
    private static Double cityFee = 20.00;

    public int getWaterHeaterAge() {
        return waterHeaterAge;
    }

    //Constructor that takes the Service call variables and adds the age
    public WaterHeater(String serviceAddress, String problemDescription, Date date, int age) {
        super(serviceAddress, problemDescription, date);

        this.waterHeaterAge = age;
    }

    //Overrides the toString method to fit a water heater
    //adds String for age, city fee and total of fee and city fee
    @Override
    public String toString() {

        String waterHeaterAgeString = Integer.toString(this.waterHeaterAge);
        String resolvedDateString = (resolvedDate == null) ? "Unresolved" : this.resolvedDate.toString();
        String resolutionString = (this.resolution == null) ? "Unresolved" : this.resolution;
        String feeString = (fee == UNRESOLVED) ? "Unresolved" : "$" + Double.toString(fee);
        String mandatoryString = "$" + Double.toString(cityFee);
        String totalString = (fee == UNRESOLVED) ? "Unresolved" : "$" + Double.toString(fee + cityFee);

        return "Water Heater Service Call " + "\n" +
                "Service Address= " + serviceAddress + "\n" +
                "Problem Description = " + problemDescription + "\n" +
                "Water Heater Age = " + waterHeaterAgeString + " years" + "\n" +
                "Reported Date = " + reportedDate + "\n" +
                "Resolved Date = " + resolvedDateString + "\n" +
                "Resolution = " + resolutionString + "\n" +
                "Service Charge = " + feeString + "\n" +
                "Mandatory City Fee = " + mandatoryString + "\n" +
                "Total Charged = " + totalString;

    }
}
