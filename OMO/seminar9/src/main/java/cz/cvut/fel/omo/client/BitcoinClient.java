package cz.cvut.fel.omo.client;

import cz.cvut.fel.omo.Observer;

public class BitcoinClient implements Observer {

    StockExchangeClient context;

    public BitcoinClient(StockExchangeClient context) {
        this.context = context;
    }

    public void update() {
        System.out.println(context.getName() + "; bitcoin price: " + context.getServer().getBitcoinState());
    }

    // IMPLEMENT ME
        // This class should implement observer interface.
        // On update the class should print name of context class and current price of bitcoin.
}
