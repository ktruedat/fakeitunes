package com.ktruedat.fakeitunes.models.specific.customers;

public class CustomerBasic {
    private int customerId;
    private String firstName;
    private String lastName;

    public CustomerBasic() {
    }

    public CustomerBasic(int customerId, String firstName, String lastName) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public CustomerBasic setCustomerId(int customerId) {
        this.customerId = customerId;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public CustomerBasic setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public CustomerBasic setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
}
