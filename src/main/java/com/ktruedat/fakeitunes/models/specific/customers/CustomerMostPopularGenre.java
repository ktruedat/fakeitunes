package com.ktruedat.fakeitunes.models.specific.customers;

import java.util.HashMap;
import java.util.Map;

public class CustomerMostPopularGenre extends CustomerBasic {
    private Map<String, Integer> mostPopularGenre = new HashMap<>();

    public CustomerMostPopularGenre() {
        super();
    }

    public CustomerMostPopularGenre(int customerId, String firstName, String lastName) {
        super(customerId, firstName, lastName);
    }

    public Map<String, Integer> getMostPopularGenre() {
        return mostPopularGenre;
    }

    public void setMostPopularGenre(String mostPopularGenre, int genreCount) {
        this.mostPopularGenre.put(mostPopularGenre, genreCount);
    }
}
