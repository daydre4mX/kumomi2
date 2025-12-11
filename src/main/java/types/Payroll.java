package main.java.types;

import java.time.LocalDate;

/**
 *
 * @author Zerubbabel Ashenafi
 */
public class Payroll {
    int paymentID;
    LocalDate dateOfPayment;
    double earnings;
    double federalTax;
    double federalSocialSec;
    double stateTax;
    double retirement401k;
    double healthcareWithheld;

    public Payroll() {
        this.paymentID = 0;
        this.dateOfPayment = LocalDate.of(0, 0, 0);
        this.earnings = 0;
        this.federalTax = 0;
        this.federalSocialSec = 0;
        this.stateTax = 0;
        this.retirement401k = 0;
        this.healthcareWithheld = 0;
    }

    public Payroll(LocalDate dateOfPayment, double earnings, double federalSocialSec, double federalTax,
            double healthcareWithheld, int paymentID, double retirement401k, double stateTax) {
        this.dateOfPayment = dateOfPayment;
        this.earnings = earnings;
        this.federalSocialSec = federalSocialSec;
        this.federalTax = federalTax;
        this.healthcareWithheld = healthcareWithheld;
        this.paymentID = paymentID;
        this.retirement401k = retirement401k;
        this.stateTax = stateTax;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public LocalDate getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(LocalDate dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }

    public double getFederalTax() {
        return federalTax;
    }

    public void setFederalTax(double federalTax) {
        this.federalTax = federalTax;
    }

    public double getFederalSocialSec() {
        return federalSocialSec;
    }

    public void setFederalSocialSec(double federalSocialSec) {
        this.federalSocialSec = federalSocialSec;
    }

    public double getStateTax() {
        return stateTax;
    }

    public void setStateTax(double stateTax) {
        this.stateTax = stateTax;
    }

    public double getRetirement401k() {
        return retirement401k;
    }

    public void setRetirement401k(double retirement401k) {
        this.retirement401k = retirement401k;
    }

    public double getHealthcareWithheld() {
        return healthcareWithheld;
    }

    public void setHealthcareWithheld(double healthcareWithheld) {
        this.healthcareWithheld = healthcareWithheld;
    }

}
