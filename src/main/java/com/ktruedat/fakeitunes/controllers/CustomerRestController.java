package com.ktruedat.fakeitunes.controllers;

import com.ktruedat.fakeitunes.dao.CustomerDao;
import com.ktruedat.fakeitunes.models.aggregated.customers.CustomerCollationCriteria;
import com.ktruedat.fakeitunes.models.specific.customers.CustomerMostPopularGenre;
import com.ktruedat.fakeitunes.models.specific.customers.CustomerShort;
import com.ktruedat.fakeitunes.models.specific.customers.CustomerSpendingProfile;
import com.ktruedat.fakeitunes.models.aggregated.customers.CustomersCollated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
public class CustomerRestController {
    private Logger log = LoggerFactory.getLogger(CustomerDao.class);

    @Autowired
    CustomerDao customerDao;

    @GetMapping("/v1/api/customers")
    public List<CustomerShort> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    @PostMapping("/v1/api/customers")
    public boolean addNewCustomer(@RequestBody CustomerShort newCustomer) {
        return customerDao.addNewCustomer(newCustomer);
    }

    @PutMapping("/v1/api/customers/{customerId}")
    public boolean updateExistingCustomer(@PathVariable("customerId") int customerId, @RequestBody CustomerShort customer) {
        return customerDao.updateExistingCustomer(customerId, customer);
    }

    @GetMapping("/v1/api/customers/collated")
    public List<CustomersCollated> getAllCustomersCollated(
            @RequestParam(value = "sort", defaultValue = "Country:DESC") String sort) {
        try {
            String[] sortCriteria = sanitizeInputForSorting(sort);
            String criterion = sortCriteria[0];
            String order = sortCriteria[1];
            return customerDao.getAllCustomersCollated(criterion, order);
        } catch (IllegalStateException exception) {
            log.error(exception.toString());
            log.error(exception.getMessage());
        }
        return null;
    }

    @GetMapping("/v1/api/customers/highest-spenders")
    public List<CustomerSpendingProfile> getHighestSpenders() {
        return customerDao.getHighestSpenders();
    }

    @GetMapping("/v1/api/customers/{customerId}/most-popular/genre")
    public CustomerMostPopularGenre getMostPopularGenre(@PathVariable("customerId") int customerId) {
        return customerDao.getMostPopularGenre(customerId);
    }

    // A delete endpoint is used to restore the database to its original state after plug and play with the other endpoints
    @DeleteMapping ("/v1/api/customers/{customerId}")
    public boolean deleteExistingCustomer(@PathVariable("customerId") int customerId) {
        return customerDao.deleteExistingCustomer(customerId);
    }

    private String[] sanitizeInputForSorting(String sort) {
        String[] sortCriteria = new String[2];

        String[] sortCriteriaRaw = sort.split(":");

        String criterion;
        if (sortCriteriaRaw[0].toLowerCase().equals("country")) {
            criterion = CustomerCollationCriteria.COUNTRY.getCriterionString();
        } else {
            throw new IllegalStateException("Unexpected value: " + sortCriteriaRaw[0] + ". Possible sort criteria are: " + Arrays.toString(CustomerCollationCriteria.values()));
        }
        String order = sortCriteriaRaw.length == 2 ? sortCriteriaRaw[1] : "ASC";

        sortCriteria[0] = criterion;
        sortCriteria[1] = order;

        return sortCriteria;
    }
}
