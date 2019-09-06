package cz.cvut.fel.omo.server;

import javax.money.MonetaryAmount;

public abstract class CryptoCurrency {

    protected String currencyName;
    protected MonetaryAmount price;

    public MonetaryAmount getPrice(){
        return this.price;
    }

    public void setPrice(MonetaryAmount price){
        this.price = price;
    }

    public String getCurrencyName(){
        return currencyName;
    }

    public void printMessage(){
        System.out.println(" ANNOUNCEMENT: " + currencyName + " new price of " + price);
    }
}