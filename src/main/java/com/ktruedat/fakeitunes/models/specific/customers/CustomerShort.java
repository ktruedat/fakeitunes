package com.ktruedat.fakeitunes.models.specific.customers;

public class CustomerShort extends CustomerBasic {
    private String country;
    private String postalCode;
    private String phone;
    private String email;

    public CustomerShort() {
        super();
    }

    public CustomerShort(int customerId, String firstName, String lastName, String country, String postalCode,
                         String phone, String email) {
        super(customerId, firstName, lastName);
        this.country = country;
        this.postalCode = postalCode;
        this.phone = phone;
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public CustomerShort setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public CustomerShort setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public CustomerShort setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CustomerShort setEmail(String email) {
        this.email = email;
        return this;
    }
}
