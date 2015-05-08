package de.fh_muenster.xbankandroid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import de.fh_muenster.xbank.Account;
import de.fh_muenster.xbank.Customer;
import de.fh_muenster.xbank.XbankOnlineService;
import de.fh_muenster.xbank.exceptions.InvalidLoginException;
import de.fh_muenster.xbank.exceptions.NoSessionException;

/**
 * Diese Klasse ersetzt die Verbindung zum Server als Mock-Objekt.
 * Die Methoden sind aufrufbar, liefern aber nur starre Testdaten.
 * Created by FalkoHoefte on 29.03.15.
 */
public class XbankOnlineServiceImplMock implements XbankOnlineService {

    private Customer cust;
    private List<Account> accountList;

    public XbankOnlineServiceImplMock() {
        accountList = new ArrayList<>();
        accountList.add(new Account(0, new BigDecimal(23)));
        accountList.add(new Account(1, new BigDecimal(30)));
        accountList.add(new Account(2, new BigDecimal(90)));
        accountList.add(new Account(3, new BigDecimal(550)));
        accountList.add(new Account(4, new BigDecimal(550)));
        accountList.add(new Account(5, new BigDecimal(550)));
        accountList.add(new Account(6, new BigDecimal(550)));
        accountList.add(new Account(7, new BigDecimal(550)));
        accountList.add(new Account(8, new BigDecimal(550)));
        accountList.add(new Account(9, new BigDecimal(550)));
        accountList.add(new Account(10, new BigDecimal(550)));
        accountList.add(new Account(11, new BigDecimal(550)));
    }

    @Override
    public Customer login(String username, String password) throws InvalidLoginException {
        cust = new Customer("TestBenutzer", "test123");
        return this.cust;
    }

    @Override
    public void logout() throws NoSessionException {
        this.cust = null;
    }

    @Override
    public List<Account> getMyAccounts() throws NoSessionException {
        //returns a copy of the private account list.
        return new ArrayList<>(this.accountList);
    }

    @Override
    public BigDecimal getBalance(int accountID) throws NoSessionException {
        Account acc = findAccountById(accountID);
        if(acc != null)
        {
            return acc.getAmount();
        }
        return null;
    }

    @Override
    public BigDecimal transfer(int fromAccount, int toAccount, BigDecimal amount) throws NoSessionException {
        Account from = findAccountById(fromAccount);
        Account to = findAccountById(toAccount);

        if(from != null && to != null)
        {
            from.decrease(amount);
            to.increase(amount);

            return from.getAmount();
        }
        return null;
    }

    private Account findAccountById(int accountId)
    {
        for(Account acc : this.accountList)
        {
            if(acc.getId() == accountId)
            {
                return acc;
            }
        }
        return null;
    }
}
