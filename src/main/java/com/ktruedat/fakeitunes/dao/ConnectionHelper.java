package com.ktruedat.fakeitunes.dao;

/*
 This class serves the purpose of holding the connection URL
 This means there is only one place in the application where the connection string needs to be written.
 When we use a proper SQL provider like, PostGres,this connection string is stored in the application and
 there is no need for a class like this.
*/

public class ConnectionHelper {
    public static final String CONNECTION_URL = "jdbc:sqlite::resource:Chinook_Sqlite.sqlite";
}
