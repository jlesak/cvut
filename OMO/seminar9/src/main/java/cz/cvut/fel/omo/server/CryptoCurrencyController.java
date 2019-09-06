package cz.cvut.fel.omo.server;

import cz.cvut.fel.omo.Observable;
import cz.cvut.fel.omo.Observer;

import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.List;

public abstract class CryptoCurrencyController implements Observable {

    // IMPLEMENT ME
    // This class will implement Observable interface.
    private List<Observer> clients = new ArrayList<Observer>();


    public void attach(Observer observer) {
        clients.add(observer);
    }

    public void detach(Observer observer) {
        clients.remove(observer);
    }

    public void notifyAllObservers() {
        for (Observer observer : clients){
            observer.notify(); //????
        }
    }

    protected CryptoCurrency currency;
    int PERCENTAGE = 100;


    /*
     * Method for calculating the new price of cryptocurrency.
     */
    public void changePrice(int fluctuation) {
        MonetaryAmount currentPrice = currency.getPrice();
        MonetaryAmount change = currentPrice.multiply(fluctuation).divide(PERCENTAGE);
        currency.setPrice(currentPrice.add(change));
        currency.printMessage();
        // State of component has change, you should let observers know about this update.
    }

    public MonetaryAmount getState(){
        return currency.getPrice();
    }


}
