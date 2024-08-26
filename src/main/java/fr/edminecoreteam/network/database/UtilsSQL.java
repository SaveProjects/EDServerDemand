package fr.edminecoreteam.network.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.edminecoreteam.network.Network;

public class UtilsSQL
{
    private static Network network = Network.getNetwork();

    public void creatingTableServers() {
        try {
            System.out.println("Installation des tables SQL...");
            PreparedStatement stm = InstanceSQL.getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS ed_servers (`server_name` varchar(255) NOT NULL, "
                            + "`server_status` int(11), `server_port` int(11), "
                            + "`server_ip` varchar(255), `server_motd` varchar(255), "
                            + "`server_onlines` int(11), `server_id` int(11), "
                            + "PRIMARY KEY (`server_name`), UNIQUE(`server_name`), INDEX(`server_name`)) CHARACTER SET utf8");
            stm.execute();
            stm.close();
            System.out.println("Tache execute avec succes !");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void message(String condition) {
        if (condition == "connectMSG")
        {
            System.out.println("+--------------------+");
            System.out.println("ED-SERVER-DEMAND");
            System.out.println("ORM: Enable");
            System.out.println("ORM-DATABASE: Connected");
            System.out.println("+--------------------+");
        }
        if (condition == "disconnectMSG")
        {
            System.out.println("+--------------------+");
            System.out.println("ED-SERVER-DEMAND");
            System.out.println("ORM: Disable");
            System.out.println("ORM-DATABASE: Disconnected");
            System.out.println("+--------------------+");
        }
    }

    public void getDefaultMySQLConfigAndConnect()
    {
        System.out.println("+--------------------+");
        System.out.println("ED-SERVER-DEMAND");
        System.out.println("IP: " + network.getConfig("mysql").getString("mysql.host"));
        System.out.println("Base de données: " + network.getConfig("mysql").getString("mysql.database"));
        System.out.println("Utilisateur: " + network.getConfig("mysql").getString("mysql.user"));
        System.out.println("");
        System.out.println("Statut: Connecté");
        System.out.println("+--------------------+");
    }
}
