package cz.cvut.fel.omo.client;

import cz.cvut.fel.omo.Observer;
import cz.cvut.fel.omo.server.StockExchangeServer;

public class StockExchangeClient {

    private StockExchangeServer stockExchangeServer;
    private String name;

    private LitecoinClient litecoinClient = new LitecoinClient(this);
    private BitcoinClient bitcoinClient = new BitcoinClient(this);

    public StockExchangeClient(StockExchangeServer stockExchange, String name){
        this.stockExchangeServer = stockExchange;
        this.name = name;
    }

    /*
     * Method makes sure, that this client application is subscribed to the bitcoin channel.
     */
    public void subscribeToBitcoinChannel(){
        stockExchangeServer.subscribeToBitcoinUpdates(bitcoinClient);
    }

    /*
     * Method makes sure, that this client application is subscribed to the litecoin channel.
     */
    public void subscribeToLitecoinChannel(){
        stockExchangeServer.subscribeToLitecoinUpdates(litecoinClient);
    }

    /*
     * Method makes sure, that this client application is subscribed to both litecoin and bitcoin channel.
     */
    public void subscribeToAllChannels(){
        subscribeToBitcoinChannel();
        subscribeToLitecoinChannel();
    }

    /*
     * Method unsubscribes application from bitcoin channel, hereafter no notifications about bitcoin price will be delivered.
     */
    public void unsubscribeFromBitcoinChannel(){
        stockExchangeServer.unsubscribeFromBitcoinChannel(bitcoinClient);
    }

    /*
     * Method unsubscribes application from litecoin channel, hereafter no notifications about litecoin price will be delivered.
     */
    public void unsubscribeFromLitecoinChannel(){
        stockExchangeServer.unsubscribeFromLitecoinChannel(litecoinClient);
    }

    /*
     * Method unsubscribes application from both bitcoin and litecoin channels, hereafter no notifications about bitcoin nor litecoin price will be delivered.
     */
    public void unsubscribeFromAllChannels(){
        unsubscribeFromBitcoinChannel();
        unsubscribeFromLitecoinChannel();
    }

    public String getName(){
        return name;
    }

    public StockExchangeServer getServer(){
        return stockExchangeServer;
    }
}
