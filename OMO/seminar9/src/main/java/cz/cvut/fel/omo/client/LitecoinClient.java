package cz.cvut.fel.omo.client;

import cz.cvut.fel.omo.Observer;

public class LitecoinClient implements Observer{

    StockExchangeClient context;

    public LitecoinClient(StockExchangeClient context){
        this.context = context;
    }

    public void update() {
        System.out.println(context.getName() + "; litecoin price: " + context.getServer().getLitecoinState());
    }

    // IMPLEMENT ME
        // This class should implement observer interface.
        // On update the class should print name of context class and current price of litecoin.
}
