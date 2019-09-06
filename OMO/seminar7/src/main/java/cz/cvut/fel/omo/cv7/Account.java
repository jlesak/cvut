package cz.cvut.fel.omo.cv7;

import javax.money.MonetaryAmount;
import javax.money.MonetaryException;

/** Kurz A7B36OMO - Objektove modelovani - Cviceni 7 Abstract factory, factory method, singleton, dependency injection
 *
 *  @author mayerto1
 *
 *
 */
public interface Account {
    public MonetaryAmount getBalance();
    public MonetaryAmount getWithdrawLimit();
    public MonetaryAmount getMonthlyFee();
    public void withdraw(MonetaryAmount amount);
    public void deposit(MonetaryAmount amount);
}
