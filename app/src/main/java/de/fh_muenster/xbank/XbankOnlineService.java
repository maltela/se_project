package de.fh_muenster.xbank;

/**
 * Created by FalkoHoefte on 29.03.15.
 */


import java.math.BigDecimal;
import java.util.List;

import de.fh_muenster.xbank.exceptions.InvalidLoginException;
import de.fh_muenster.xbank.exceptions.NoSessionException;

public interface XbankOnlineService {

    public Customer login(String username, String password)
            throws InvalidLoginException;
    public void logout()
            throws NoSessionException;
    public List<Account> getMyAccounts()
            throws NoSessionException;
    public BigDecimal getBalance(int accountID)
            throws NoSessionException;
    public BigDecimal transfer(int fromAccount,
                               int toAccount,
                               BigDecimal amount)
            throws NoSessionException;
}