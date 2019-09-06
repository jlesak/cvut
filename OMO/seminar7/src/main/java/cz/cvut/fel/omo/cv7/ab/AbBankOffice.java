package cz.cvut.fel.omo.cv7.ab;

import cz.cvut.fel.omo.cv7.BankOffice;

public class AbBankOffice implements BankOffice {
    String ADDRESS = "Ab Banka, Náměstí 2, Praha 1";
    String PHONE = "420-2-777-777-777";

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
