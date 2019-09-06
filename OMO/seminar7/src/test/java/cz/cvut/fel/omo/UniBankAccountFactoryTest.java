package cz.cvut.fel.omo;

import cz.cvut.fel.omo.cv7.Account;
import cz.cvut.fel.omo.cv7.BankOffice;
import cz.cvut.fel.omo.cv7.Loan;
import cz.cvut.fel.omo.cv7.uni.UniBankFactory;
import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.money.MonetaryAmount;
import javax.money.MonetaryOperator;
import javax.money.MonetaryRounding;
import javax.money.format.MonetaryFormats;
import java.math.MathContext;

public class UniBankAccountFactoryTest {

    UniBankFactory factory;

    @Before
    public void setUp(){
        factory = new UniBankFactory();
    }

    @Test
    public void createBankOffice_createNewBankOfficeInstance_bankOfficeInstanceIsReady(){
        // arrange
        String expectedPhoneNubmer = "420-2-234-234-234";
        String expectedAddress = "Uni, Namesti 1, Praha 1";

        // act
        BankOffice office = factory.createBankOffice();

        // assert
        Assert.assertEquals(expectedAddress,office.getAddress());
        Assert.assertEquals(expectedPhoneNubmer,office.getPhoneContact());
    }

    @Test
    public void createAccount_createNewAccountAndDepositAndWithdraw_AccountBalanceShouldBeDepositMinusWithdrawAmount(){
        // arrange
        MonetaryAmount expectedBalance = Money.of(70,"EUR");

        // act
        Account account = factory.createAccount();
        account.deposit(Money.of(120,"EUR"));
        account.withdraw(Money.of(50,"EUR"));

        // assert
        Assert.assertEquals(expectedBalance,account.getBalance());
    }

    @Test
    public void createLoan_createNewLoan_monthlyPaymentIsCaclulated(){
        // arrange
        MonetaryAmount amount=Money.of(1000,"EUR");
        int months = 20;
        double interestRate = 0.01;
        MonetaryAmount expectedMonthlyPayment=Money.of(50.8333333333333334,"EUR");

        // act
        Loan loan = factory.createLoan(amount,months,interestRate);

        // assert
        Assert.assertEquals(expectedMonthlyPayment.toString().substring(0,15),loan.getMonthlyPayment().toString().substring(0,15));
    }
}
