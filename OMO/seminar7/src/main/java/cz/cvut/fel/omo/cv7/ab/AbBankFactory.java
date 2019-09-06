package cz.cvut.fel.omo.cv7.ab;

import cz.cvut.fel.omo.cv7.AbstractBankFactory;
import cz.cvut.fel.omo.cv7.Account;
import cz.cvut.fel.omo.cv7.BankOffice;
import cz.cvut.fel.omo.cv7.Loan;

import javax.inject.Named;
import javax.money.MonetaryAmount;

@Named("Ab")
public class AbBankFactory extends AbstractBankFactory {
    private static AbBankFactory instance = null;

    @Override
    public BankOffice createBankOffice() {
        return new AbBankOffice();
    }

    @Override
    public Account createAccount() {
        return new AbAccount();
    }

    @Override
    public Loan createLoan(MonetaryAmount amount, int months, double recommendedInterestRate) {
        return new ABLoan(amount, months, recommendedInterestRate);
    }

    public static AbBankFactory getInstance(){
        if (instance == null) instance = new AbBankFactory();
        return instance;
    }
}
