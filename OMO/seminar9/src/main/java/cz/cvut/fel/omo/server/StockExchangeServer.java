
package cz.cvut.fel.omo.server;

        import cz.cvut.fel.omo.Observer;

        import javax.money.MonetaryAmount;
        import java.util.Random;

public class StockExchangeServer {

    int LITECOIN_RANGE = 15;
    int LITECOIN_COEFFICIENT = 5;
    int BITCOIN_RANGE = 20;
    int BITCOIN_COEFFICIENT = 8;

    private static StockExchangeServer instance = null;

    private BitcoinController bitcoinController;
    private LitecoinController litecoinController;

    public static StockExchangeServer getInstance() {
        if(instance == null) {
            instance = new StockExchangeServer();
        }
        return instance;
    }

    private StockExchangeServer() {
        this.bitcoinController = new BitcoinController();
        this.litecoinController = new LitecoinController();
    }

    /*
         * Method for subscribing Observer to Bitcoin channel.
         */
    public void subscribeToBitcoinUpdates(Observer observer) {
        bitcoinController.attach(observer);
    }

    /*
    * Method for subscribing Observer to Litecoin channel.
    */
    public void subscribeToLitecoinUpdates(Observer observer) {
        litecoinController.attach(observer);
    }

    /*
    * Method for unsubscribing Observer from Bitcoin channel.
    */
    public void unsubscribeFromBitcoinChannel(Observer observer){
        bitcoinController.detach(observer);
    }

    /*
    * Method for unsubscribing Observer from Litecoin channel.
    */
    public void unsubscribeFromLitecoinChannel(Observer observer){
        litecoinController.detach(observer);
    }

    /*
    * Method for computing new price for cryptocurrency.
    */
    public void computeMarketFluctuation() {
        computeBitcoinFluctuation();
        computeLitecoinFluctuation();
    }

    /*
    * Highly sophisticated method for computing new price for litecoin.
    */
    public void computeLitecoinFluctuation() {
        Random rand = new Random();
        int fluctuation = rand.nextInt(LITECOIN_RANGE) - LITECOIN_COEFFICIENT;
        litecoinController.changePrice(fluctuation);
    }

    /*
    * Highly sophisticated method for computing new price for bitcoin.
    */
    public void computeBitcoinFluctuation() {
        Random rand = new Random();
        int fluctuation = rand.nextInt(BITCOIN_RANGE) - BITCOIN_COEFFICIENT;
        bitcoinController.changePrice(fluctuation);
    }

    /*
     * Method for retrieving current price for litecoin.
     */
    public MonetaryAmount getLitecoinState(){
        return litecoinController.currency.getPrice();
    }

    /*
    * Method for retrieving current price for bitcoin.
    */
    public MonetaryAmount getBitcoinState(){
        return bitcoinController.currency.getPrice();

    }
}
