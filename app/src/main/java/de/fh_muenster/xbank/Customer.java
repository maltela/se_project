package de.fh_muenster.xbank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FalkoHoefte on 29.03.15.
 */
public class Customer {
    private String userName;
    private String password;
    private List<Account> accounts;

    public Customer(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    public List<Account> getAccounts()
    {
        return this.accounts;
    }

    public void addNewAccount(Account newAccount){
        if(newAccount != null) {
            this.accounts.add(newAccount);
        }
    }

    public Account getAccountById(int id){
        for(Account acc : this.accounts)
        {
            if(acc.getId() == id)
            {
                return acc;
            }
        }
        return null;
    }

    public String getUserName()
    {
        return this.userName;
    }

    @Override
    public String toString()
    {
        String retString = "Customer \"" + this.userName + "\" - Accounts: " + this.accounts.size() + " - ";
        for(Account acc : this.accounts)
        {
            retString += acc.toString();
        }
        return retString;
    }
}
