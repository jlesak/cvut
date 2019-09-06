package cz.cvut.fel.omo.cv7.ab;

import cz.cvut.fel.omo.cv7.Account;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

public class AbAccount implements Account {

    MonetaryAmount balance = Money.of(0,"EUR");
    private int withdraws = 0;
    private int deposits = 0;


    @Override
    public MonetaryAmount getBalance() {
        return balance;
    }

    @Override
    public MonetaryAmount getWithdrawLimit() {
        return Money.of(2000,"EUR");
    }

    @Override
    public MonetaryAmount getMonthlyFee() {
        return Money.of(withdraws * 0.5 - 0.25 * deposits, "EUR");
    }

    @Override
    public void withdraw(MonetaryAmount amount) {
        balance.subtract(amount);
        withdraws++;
    }

    @Override
    public void deposit(MonetaryAmount amount) {
        balance.add(amount);
        deposits++;
    }

    public String toString(){
        return String.format("Ab Account - balance: %s, fee: %s", getBalance(), getMonthlyFee());
    }
}
