package cz.cvut.fel.omo.cv7.uni;

import cz.cvut.fel.omo.cv7.BankOffice;

/** Kurz A7B36OMO - Objektove modelovani - Cviceni 7 Abstract factory, factory method, singleton, dependency injection
 *
 *  @author mayerto1
 *
 *
 */
public class UniBankOffice implements BankOffice {

    String ADDRESS = "Uni, Namesti 1, Praha 1";
    String PHONE = "420-2-234-234-234";

    @Override
    public String getAddress() {
        return ADDRESS;
    }

    @Override
    public String getPhoneContact() {
        return PHONE;
    }

    public String toString(){
        return String.format("Bank Address: %s, Phone Number: %s",getAddress(),getPhoneContact());
    }

}
