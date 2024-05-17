package com.ktruedat.fakeitunes.models.aggregated.customers;

public class CustomersCollated {
    private CustomerCollationCriteria customersCollationCriterion;
    private String criterionValue;
    private int nrOfCustomers;

    public CustomersCollated() {
    }

    public CustomersCollated(CustomerCollationCriteria customersCollationCriterion, String criterionValue, int nrOfCustomers) {
        this.customersCollationCriterion = customersCollationCriterion;
        this.criterionValue = criterionValue;
        this.nrOfCustomers = nrOfCustomers;
    }

    public CustomerCollationCriteria getCriterion() {
        return customersCollationCriterion;
    }

    public CustomersCollated setCriterion(CustomerCollationCriteria customerCollationCriteria) {
        this.customersCollationCriterion = customerCollationCriteria;
        return this;
    }

    public String getCriterionValue() {
        return criterionValue;
    }

    public CustomersCollated setCriterionValue(String criterionValue) {
        this.criterionValue = criterionValue;
        return this;
    }

    public int getNrOfCustomers() {
        return nrOfCustomers;
    }

    public CustomersCollated setNrOfCustomers(int nrOfCustomers) {
        this.nrOfCustomers = nrOfCustomers;
        return this;
    }
}
