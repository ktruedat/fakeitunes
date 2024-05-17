package com.ktruedat.fakeitunes.models.aggregated.customers;

public enum CustomerCollationCriteria {
    COUNTRY("Country");

    private String criterion;

    CustomerCollationCriteria(String criterion) {
        this.criterion = criterion;
    }

    public String getCriterionString() {
        return this.criterion;
    }
}
