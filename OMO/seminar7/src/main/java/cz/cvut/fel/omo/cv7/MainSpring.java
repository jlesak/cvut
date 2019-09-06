package cz.cvut.fel.omo.cv7;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import javax.inject.Inject;
import javax.inject.Named;
import javax.money.MonetaryAmount;

/** Kurz A7B36OMO - Objektove modelovani - Cviceni 7 Abstract factory, factory method, singleton, dependency injection
 *
 *  @author mayerto1
 *
 *  Demonstrates the Spring dependency injection. It injects abstract factory to the main class through javax.inject.Inject annotation
 */
@SpringBootApplication
public class MainSpring {

    // TODO pouzijte anotace @Inject @Qualifier("Uni") pro pouziti Spring dependency injection
    @Inject @Qualifier("Uni")
    AbstractBankFactory factory;

    public static void main(String[] args){SpringApplication.run(MainSpring.class,args);}

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            MonetaryAmount loanAmount = Money.of(1000,"EUR");
            int months = 20;
            double interestRate = 0.01;

            // TODO - bez pouziti anotace @Inject viz vyse bude kod na nasledujicim radku padat na NullPointerException - neni nastavena instance factory
            BankOffice office = factory.createBankOffice();
            Loan loan = factory.createLoan(loanAmount,months,interestRate);
            Account account = factory.createAccount();
            account.deposit(Money.of(120,"EUR"));
            account.withdraw(Money.of(50,"EUR"));

            System.out.println(office);
            System.out.println(loan);
            System.out.println(account);

        };

    }

}



