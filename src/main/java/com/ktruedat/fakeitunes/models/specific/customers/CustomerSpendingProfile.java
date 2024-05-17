package com.ktruedat.fakeitunes.models.specific.customers;

public class CustomerSpendingProfile extends CustomerBasic {
    private double totalSpending;

    public CustomerSpendingProfile() {
        super();
    }

    public CustomerSpendingProfile(int customerId, String firstName, String lastName, double totalSpending) {
        super(customerId, firstName, lastName);
        this.totalSpending = totalSpending;
    }

    public double getTotalSpending() {
        return totalSpending;
    }

    public CustomerSpendingProfile setTotalSpending(double totalSpending) {
        this.totalSpending = totalSpending;
        return this;
    }
}
