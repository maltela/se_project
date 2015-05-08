package de.fh_muenster.xbank;

import java.math.BigDecimal;

/**
 * Created by FalkoHoefte on 29.03.15.
 */
public class Account {
    private int id;
    private BigDecimal amount;

    public Account(int id, BigDecimal amount)
    {
        this.id = id;
        this.amount = amount;
    }

    public int getId()
    {
        return this.id;
    }

    public BigDecimal getAmount()
    {
        return this.amount;
    }

    public void increase(BigDecimal amount)
    {
        this.amount = this.amount.add(amount);
    }

    public void decrease(BigDecimal amount)
    {
        this.amount = this.amount.subtract(amount);
    }

    @Override
    public String toString()
    {
        return "Account: ID " + this.id + " - Amount: " + this.amount.toString();
    }
}
