package cz.cvut.fel.omo.server;

import org.javamoney.moneta.Money;

public class Bitcoin extends CryptoCurrency {

    public Bitcoin(){
        currencyName = "Bitcoin";
        price = Money.of(8000, "EUR");
    }
}
