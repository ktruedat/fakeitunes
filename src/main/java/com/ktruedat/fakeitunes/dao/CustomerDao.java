package com.ktruedat.fakeitunes.dao;

import com.ktruedat.fakeitunes.models.aggregated.customers.CustomerCollationCriteria;
import com.ktruedat.fakeitunes.models.aggregated.customers.CustomersCollated;
import com.ktruedat.fakeitunes.models.product.items.Artist;
import com.ktruedat.fakeitunes.models.product.items.Genre;
import com.ktruedat.fakeitunes.models.product.items.Song;
import com.ktruedat.fakeitunes.models.product.items.Track;
import com.ktruedat.fakeitunes.models.specific.customers.CustomerMostPopularGenre;
import com.ktruedat.fakeitunes.models.specific.customers.CustomerShort;
import com.ktruedat.fakeitunes.models.specific.customers.CustomerSpendingProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerDao {
    private static final int NUMBER_OF_RANDOM_ITEMS_TO_FETCH = 5;
    private Logger log = LoggerFactory.getLogger(CustomerDao.class);
    private String URL = ConnectionHelper.CONNECTION_URL;
    private Connection connection = null;

    public List<CustomerShort> getAllCustomers() {
        List<CustomerShort> customers = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL);
            log.info("Connection to SQLite to retrieve all customer data has been established.");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT CustomerId, FirstName, LastName, Country, PostalCode, Phone, Email " +
                            "FROM Customer"
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customers.add(
                        new CustomerShort(
                                resultSet.getInt("CustomerId"),
                                resultSet.getString("FirstName"),
                                resultSet.getString("LastName"),
                                resultSet.getString("Country"),
                                resultSet.getString("PostalCode"),
                                resultSet.getString("Phone"),
                                resultSet.getString("Email")
                        )
                );
            }
            log.info("All customers have been retrieved.");
        } catch (SQLException exception) {
            log.error(exception.toString());
            log.error(exception.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                log.error(exception.toString());
                log.error(exception.getMessage());
            }
        }
        return customers;
    }

    public boolean addNewCustomer(CustomerShort newCustomer) {
        boolean success = false;
        try {
            connection = DriverManager.getConnection(URL);
            log.info("Connection to SQLite to add a new customer has been established.");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Customer(CustomerId, FirstName, LastName, Country, PostalCode, Phone, Email) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?)"
            );
            preparedStatement.setInt(1, newCustomer.getCustomerId());
            preparedStatement.setString(2, newCustomer.getFirstName());
            preparedStatement.setString(3, newCustomer.getLastName());
            preparedStatement.setString(4, newCustomer.getCountry());
            preparedStatement.setString(5, newCustomer.getPostalCode());
            preparedStatement.setString(6, newCustomer.getPhone());
            preparedStatement.setString(7, newCustomer.getEmail());

            int result = preparedStatement.executeUpdate();
            success = result != 0;
            log.info("A new customer has been added successfully.");
        } catch (SQLException exception) {
            log.error(exception.toString());
            log.error(exception.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                log.error(exception.toString());
                log.error(exception.getMessage());
            }
        }
        return success;
    }

    public boolean updateExistingCustomer(int customerId, CustomerShort customer) {
        boolean success = false;
        try {
            connection = DriverManager.getConnection(URL);
            log.info("Connection to SQLite to update an existing customer has been established.");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Customer " +
                            "SET CustomerId = ?, FirstName = ?, LastName = ?, Country = ?, PostalCode = ?, Phone = ?," +
                            " Email = ?" +
                            " WHERE CustomerId = ?"
            );
            preparedStatement.setInt(1, customer.getCustomerId());
            preparedStatement.setString(2, customer.getFirstName());
            preparedStatement.setString(3, customer.getLastName());
            preparedStatement.setString(4, customer.getCountry());
            preparedStatement.setString(5, customer.getPostalCode());
            preparedStatement.setString(6, customer.getPhone());
            preparedStatement.setString(7, customer.getEmail());
            preparedStatement.setInt(8, customerId);

            int result = preparedStatement.executeUpdate();
            success = result != 0;
            log.info("The customer has been updated successfully.");
        } catch (SQLException exception) {
            log.error(exception.toString());
            log.error(exception.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                log.error(exception.toString());
                log.error(exception.getMessage());
            }
        }
        return success;
    }

    public List<CustomersCollated> getAllCustomersCollated(String criterion, String order) {
        List<CustomersCollated> customersTalliedByCriteria = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL);
            log.info("Connection to SQLite to retrieve all customer data has been established.");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    String.format(
                        "SELECT %s, COUNT(%s) as count " +
                                "FROM Customer " +
                                "GROUP BY %s " +
                                "ORDER BY count %s, %s",
                        criterion, criterion,
                            criterion,
                            order, criterion
                    )
            );

            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            while (resultSet.next()) {
                customersTalliedByCriteria.add(
                        new CustomersCollated(
                                CustomerCollationCriteria.valueOf(resultSetMetaData.getColumnName(1).toUpperCase()),
                                resultSet.getString(1),
                                resultSet.getInt("count")
                        )
                );
            }
            log.info("All customers have been tallied.");
        } catch (SQLException exception) {
            log.error(exception.toString());
            log.error(exception.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                log.error(exception.toString());
                log.error(exception.getMessage());
            }
        }
        return customersTalliedByCriteria;
    }

    public List<CustomerSpendingProfile> getHighestSpenders() {
        List<CustomerSpendingProfile> highestPayingCustomers = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL);
            log.info("Connection to SQLite to retrieve customer spending data has been established.");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT CustomerId, FirstName, LastName, round(SUM(Total), 2) as total " +
                    "FROM Customer NATURAL JOIN Invoice " +
                    "GROUP BY FirstName, LastName " +
                    "ORDER BY total DESC"
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                highestPayingCustomers.add(
                        new CustomerSpendingProfile(
                                resultSet.getInt("CustomerId"),
                                resultSet.getString("FirstName"),
                                resultSet.getString("LastName"),
                                resultSet.getDouble("total")
                        )
                );
            }
            log.info("All customers' spending has been tallied.");
        } catch (SQLException exception) {
            log.error(exception.toString());
            log.error(exception.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                log.error(exception.toString());
                log.error(exception.getMessage());
            }
        }
        return highestPayingCustomers;
    }

    public CustomerMostPopularGenre getMostPopularGenre(int customerId) {
        CustomerMostPopularGenre customerMostPopularGenre = new CustomerMostPopularGenre();
        try {
            connection = DriverManager.getConnection(URL);
            log.info("Connection to SQLite to retrieve customer data has been established.");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "WITH CountQuery AS (SELECT c.CustomerId, c.FirstName, c.LastName, g.Name, count(g.GenreId) as GenreCount " +
                                                "FROM Customer AS c " +
                                                        "JOIN Invoice AS iv " +
                                                            "ON iv.CustomerId = c.CustomerId " +
                                                        "JOIN InvoiceLine AS il " +
                                                            "ON il.InvoiceId = iv.InvoiceId " +
                                                        "JOIN Track AS t " +
                                                            "ON t.TrackId = il.TrackId " +
                                                        "JOIN Genre AS g " +
                                                            "ON g.GenreId = t.GenreId " +
                                                "WHERE c.CustomerId=? " +
                                                "GROUP BY g.GenreId " +
                                                "ORDER BY GenreCount) " +
                            "SELECT CustomerId, FirstName, LastName, Name, GenreCount " +
                            "FROM CountQuery " +
                            "WHERE (SELECT MAX(GenreCount) from CountQuery) = GenreCount"
            );
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customerMostPopularGenre.setCustomerId(resultSet.getInt("CustomerId"));
                customerMostPopularGenre.setFirstName(resultSet.getString("FirstName"));
                customerMostPopularGenre.setLastName(resultSet.getString("LastName"));
                customerMostPopularGenre.setMostPopularGenre(resultSet.getString("Name"), resultSet.getInt("GenreCount"));
            }
            log.info("The customer's most popular genre(s) has(have) been retrieved.");
        } catch (SQLException exception) {
            log.error(exception.toString());
            log.error(exception.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                log.error(exception.toString());
                log.error(exception.getMessage());
            }
        }
        return customerMostPopularGenre;
    }

    public List<Artist> getRandomArtists() {
        List<Artist> artists = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL);
            log.info("Connection to SQLite to retrieve artist data has been established.");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    String.format(
                            "SELECT Name FROM Artist " +
                                    "ORDER BY random() " +
                                    "LIMIT %d",
                            NUMBER_OF_RANDOM_ITEMS_TO_FETCH
                    )
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                artists.add(
                        new Artist(resultSet.getString("Name"))
                );
            }
            log.info(String.format("%d random artists have been retrieved.", NUMBER_OF_RANDOM_ITEMS_TO_FETCH));
        } catch (SQLException exception) {
            log.error(exception.toString());
            log.error(exception.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                log.error(exception.toString());
                log.error(exception.getMessage());
            }
        }
        return artists;
    }

    public List<Song> getRandomSongs() {
        List<Song> songs = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL);
            log.info("Connection to SQLite to retrieve track data has been established.");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    String.format(
                            "SELECT Name FROM Track " +
                                    "ORDER BY random() " +
                                    "LIMIT %d",
                            NUMBER_OF_RANDOM_ITEMS_TO_FETCH
                    )
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                songs.add(
                        new Song(resultSet.getString("Name"))
                );
            }
            log.info(String.format("%d random track names have been retrieved.", NUMBER_OF_RANDOM_ITEMS_TO_FETCH));
        } catch (SQLException exception) {
            log.error(exception.toString());
            log.error(exception.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                log.error(exception.toString());
                log.error(exception.getMessage());
            }
        }
        return songs;
    }

    public List<Genre> getRandomGenres() {
        List<Genre> genres = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL);
            log.info("Connection to SQLite to retrieve genre data has been established.");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    String.format(
                            "SELECT Name FROM Genre " +
                                    "ORDER BY random() " +
                                    "LIMIT %d",
                            NUMBER_OF_RANDOM_ITEMS_TO_FETCH
                    )
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                genres.add(
                        new Genre(resultSet.getString("Name"))
                );
            }
            log.info(String.format("%d random genre names have been retrieved.", NUMBER_OF_RANDOM_ITEMS_TO_FETCH));
        } catch (SQLException exception) {
            log.error(exception.toString());
            log.error(exception.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                log.error(exception.toString());
                log.error(exception.getMessage());
            }
        }
        return genres;
    }

    public List<Track> getAllMatches(String termToSearch) {
        List<Track> allMatches = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL);
            log.info("Connection to SQLite to search for a track data has been established.");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    String.format(
                            "SELECT t.Name, ar.Name, al.Title, g.Name " +
                                "FROM Track AS t " +
                                    "JOIN Album AS al " +
                                        "ON t.AlbumId = al.AlbumId " +
                                    "JOIN  Artist AS ar " +
                                        "ON al.ArtistId = ar.ArtistId " +
                                    "JOIN Genre as g " +
                                        "ON g.GenreId = t.GenreId " +
                            "WHERE t.Name LIKE '%%%s%%'",
                            termToSearch
                    )
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                allMatches.add(
                        new Track(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4)
                        )
                );
            }
            log.info(String.format("The search for \"%s\" has been processed.", termToSearch));
        } catch (SQLException exception) {
            log.error(exception.toString());
            log.error(exception.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                log.error(exception.toString());
                log.error(exception.getMessage());
            }
        }
        return allMatches;
    }

    public boolean deleteExistingCustomer(int customerId) {
        boolean success = false;
        try {
            connection = DriverManager.getConnection(URL);
            log.info("Connection to SQLite to delete an existing customer has been established.");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM Customer WHERE CustomerId = ?"
            );
            preparedStatement.setInt(1, customerId);

            int result = preparedStatement.executeUpdate();
            success = result != 0;
            log.info("The customer has been deleted successfully.");
        } catch (SQLException exception) {
            log.error(exception.toString());
            log.error(exception.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                log.error(exception.toString());
                log.error(exception.getMessage());
            }
        }
        return success;
    }

    public static int getNumberOfRandomItemsToFetch() {
        return NUMBER_OF_RANDOM_ITEMS_TO_FETCH;
    }
}
