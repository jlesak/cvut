package cz.cvut.fel.omo.cv7.uni;

import cz.cvut.fel.omo.cv7.AbstractBankFactory;
import cz.cvut.fel.omo.cv7.Account;
import cz.cvut.fel.omo.cv7.BankOffice;
import cz.cvut.fel.omo.cv7.Loan;

import javax.inject.Named;
import javax.money.MonetaryAmount;

/** Kurz A7B36OMO - Objektove modelovani - Cviceni 7 Abstract factory, factory method, singleton, dependency injection
 *
 *  @author mayerto1
 *
 *
 */
    @Named("Uni")
public class UniBankFactory extends AbstractBankFactory {

    private static UniBankFactory instance = null;

    @Override
    public BankOffice createBankOffice() {
        return new UniBankOffice();
    }

    @Override
    public Account createAccount() {
        return new UniAccount();
    }

    @Override
    public Loan createLoan(MonetaryAmount amount, int months, double recommendedInterestRate) {
        return new UniLoan(amount, months, recommendedInterestRate);
    }

    public static UniBankFactory getInstance(){
        if (instance == null) instance = new UniBankFactory();
        return instance;
    }

}
