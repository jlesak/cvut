package cz.cvut.fel.omo;

import cz.cvut.fel.omo.client.StockExchangeClient;
import cz.cvut.fel.omo.server.StockExchangeServer;

public class Main {

    public static void main(String [ ] args) {

        int TIME_SPEED = 2000;
        int MARKET_MONITORED_TIME = 100;

        // CREATE NEW CRYPTOCURRENCY STOCK EXCHANGE AND CLIENTS
        StockExchangeServer stockExchange = StockExchangeServer.getInstance();
        StockExchangeClient client1 = new StockExchangeClient(stockExchange, "Vergilius");
        StockExchangeClient client2 = new StockExchangeClient(stockExchange, "Ovidius");
        StockExchangeClient client3 = new StockExchangeClient(stockExchange, "Horatius");
        StockExchangeClient client4 = new StockExchangeClient(stockExchange, "Tullius");

        // SUBSCRIBE CLIENTS TO CRYPTOCURRENCIES
        client1.subscribeToBitcoinChannel();
        client2.subscribeToLitecoinChannel();
        client3.subscribeToAllChannels();

        // SIMULATE STOCK EXCHANGE
        for (int time=0; time<MARKET_MONITORED_TIME; time++) {
            System.out.print("\n===============================================================\n");
            stockExchange.computeMarketFluctuation();
            System.out.print("===============================================================\n");
            // wait for a while
            try {
                Thread.sleep(TIME_SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
