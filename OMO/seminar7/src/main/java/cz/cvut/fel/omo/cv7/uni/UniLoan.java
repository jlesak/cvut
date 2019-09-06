package cz.cvut.fel.omo.cv7.uni;

import cz.cvut.fel.omo.cv7.Loan;

import javax.money.MonetaryAmount;

/** Kurz A7B36OMO - Objektove modelovani - Cviceni 7 Abstract factory, factory method, singleton, dependency injection
 *
 *  @author mayerto1
 *
 *
 */
public class UniLoan implements Loan{

    MonetaryAmount balance;
    double interestRate;
    int repaymentPeriod;

    public UniLoan(MonetaryAmount amount, int months, double recommendedInterestRate){
        balance = amount;
        interestRate = recommendedInterestRate;
        repaymentPeriod = months;
    }

    @Override
    public MonetaryAmount getBalance() {
        return balance;
    }

    @Override
    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public MonetaryAmount getMonthlyPayment() {
        return balance=balance.divide(repaymentPeriod).add(balance.multiply(interestRate/12));
    }

    public String toString(){
        return String.format("Loan Overview - Balance: %s, InterestRate: %f, MonthlyPayment: %s", getBalance(), getInterestRate(), getMonthlyPayment());
    }

}
