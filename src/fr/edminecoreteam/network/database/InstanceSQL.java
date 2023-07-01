package fr.edminecoreteam.network.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InstanceSQL
{
    private final String urlBase;
    private final String host;
    private final String database;
    private final String userName;
    private final String password;
    private static Connection connection;

    public InstanceSQL(String urlBase, String host, String database, String userName, String password) {
        this.urlBase = urlBase;
        this.host = host;
        this.database = database;
        this.userName = userName;
        this.password = password;
        this.connexion();
    }

    public static Connection getConnection() {
        return InstanceSQL.connection;
    }

    public void connexion() {
        if (!isOnline()) {
            try {
                InstanceSQL.connection = DriverManager.getConnection(String.valueOf(this.urlBase) + this.host + "/" + this.database, this.userName, this.password);
                UtilsSQL utilsSQL = new UtilsSQL();
                utilsSQL.message("connectMSG");
                utilsSQL.creatingTableServers();
            }
            catch (SQLException e) {
                System.out.println("§cErreur de connexion a la base de donnée...");
            }
        }
    }

    public void deconnexion() {
        if (!isOnline()) {
            try {
                InstanceSQL.connection.close();
                UtilsSQL utilsSQL = new UtilsSQL();
                utilsSQL.message("disconnectMSG");
            }
            catch (SQLException e) {
                System.out.println("§cErreur de déconnexion de la base de donnée...");
            }
        }
    }

    public boolean isOnline() {
        try {
            return InstanceSQL.connection != null && !InstanceSQL.connection.isClosed();
        }
        catch (SQLException e) {
            return false;
        }
    }
}
