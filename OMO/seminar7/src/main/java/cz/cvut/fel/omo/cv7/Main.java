package cz.cvut.fel.omo.cv7;

import cz.cvut.fel.omo.cv7.ab.AbBankFactory;
import cz.cvut.fel.omo.cv7.uni.UniBankFactory;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

/** Kurz A7B36OMO - Objektove modelovani - Cviceni 7 Abstract factory, factory method, singleton, dependency injection
 *
 *  @author mayerto1
 *
 *
 */
public class Main {

    public static void main(String args[]){

        // TODO 1 - doplnit druhy prvek pole o novou instanci AbBankFactory
        // TODO 2 - upravit kod vytvareni instance tak, aby respektoval navrhovy vzor Singleton
        AbstractBankFactory[] factories = {UniBankFactory.getInstance(), AbBankFactory.getInstance()};

        MonetaryAmount loanAmount = Money.of(1000,"EUR");
        int months = 20;
        double interestRate = 0.01;

        for(AbstractBankFactory factory : factories){
            BankOffice office = factory.createBankOffice();
            Loan loan = factory.createLoan(loanAmount,months,interestRate);
            Account account = factory.createAccount();
            account.deposit(Money.of(100,"EUR"));
            account.withdraw(Money.of(50,"EUR"));

            System.out.println(office);
            System.out.println(loan);
            System.out.println(account);

        }
    }
}
