package cz.cvut.fel.omo.cv7.ab;

import cz.cvut.fel.omo.cv7.Loan;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

public class ABLoan implements Loan {

    MonetaryAmount balance;
    double interestRate;
    int repaymentPeriod;

    public ABLoan(MonetaryAmount amount, int months, double recommendedInterestRate) {

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
        return balance=balance.divide(repaymentPeriod).add(balance.multiply(interestRate/12).subtract(Money.of(10, "EUR")));
    }
}
