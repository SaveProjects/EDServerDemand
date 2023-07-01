package fr.edminecoreteam.network.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import fr.edminecoreteam.network.Network;

public class ServerData
{
    public String table;
    private String proxyName;
    private String displayName;
    private static Network network = Network.getNetwork();

    public ServerData(String displayName)
    {
        this.proxyName = "ProxyNetwork";
        this.table = "ed_servers";
        this.displayName = displayName;
    }

    public void createSpigotServer(String address, int port)
    {
        if (!hasServer())
        {
            try
            {
                PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("INSERT INTO " + table + " (server_name, server_status, server_port, server_ip, server_onlines, server_id) VALUES (?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, displayName); /*server_name*/
                preparedStatement.setInt(2, 0); /*server_status*/
                preparedStatement.setInt(3, port); /*server_port*/
                preparedStatement.setString(4, address); /*server_ip*/
                preparedStatement.setInt(5, 0); /*server_onlines*/
                preparedStatement.setInt(6, 0); /*server_id*/
                preparedStatement.execute();
                preparedStatement.close();
            }
            catch (SQLException e)
            {
                e.toString();
            }
        }
    }

    public void createNetworkServer(String address, int port)
    {
        removeNetworkServer();
        try
        {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("INSERT INTO " + table + " (server_name, server_status, server_port, server_ip, server_onlines, server_id) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, proxyName); /*server_name*/
            preparedStatement.setInt(2, 1); /*server_status*/
            preparedStatement.setInt(3, port); /*server_port*/
            preparedStatement.setString(4, address); /*server_ip*/
            preparedStatement.setInt(5, 0); /*server_onlines*/
            preparedStatement.setInt(6, network.getConfig("config").getInt("config.serverPerGroup")); /*server_id*/
            preparedStatement.execute();
            preparedStatement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void removeNetworkServer()
    {
        try
        {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("DELETE FROM " + table + " WHERE server_name = ?");
            preparedStatement.setString(1, proxyName);
            preparedStatement.execute();
            preparedStatement.close();
        }
        catch (SQLException e)
        {
            e.toString();
        }
    }

    public void removeServer()
    {
        try
        {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("DELETE FROM " + table + " WHERE server_name = ?");
            preparedStatement.setString(1, displayName);
            preparedStatement.execute();
            preparedStatement.close();
        }
        catch (SQLException e)
        {
            e.toString();
        }
    }

    public boolean hasServer()
    {
        try
        {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("SELECT server_port FROM " + table + " WHERE server_name = ?");
            preparedStatement.setString(1, displayName);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        }
        catch (SQLException e)
        {
            e.toString();
            return false;
        }
    }

    public void defineStatus(int status)
    {
        try
        {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("UPDATE " + table + " SET server_status = ? WHERE server_name = ?");
            preparedStatement.setInt(1, status);
            preparedStatement.setString(2, displayName);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getServerID()
    {
        try
        {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("SELECT server_id FROM ed_servers WHERE server_name = ?");
            preparedStatement.setString(1, displayName);
            ResultSet rs = preparedStatement.executeQuery();
            int address = 0;
            while (rs.next())
            {
                address = rs.getInt("server_id");
            }
            preparedStatement.close();
            return address;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getPort()
    {
        try
        {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("SELECT server_port FROM " + table + " WHERE server_name = ?");
            preparedStatement.setString(1, displayName);
            ResultSet rs = preparedStatement.executeQuery();
            int port = 0;
            while (rs.next())
            {
                port = rs.getInt("server_port");
            }
            preparedStatement.close();
            return port;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getIP()
    {
        try
        {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("SELECT server_ip FROM " + table + " WHERE server_name = ?");
            preparedStatement.setString(1, displayName);
            ResultSet rs = preparedStatement.executeQuery();
            String port = "";
            while (rs.next())
            {
                port = rs.getString("server_ip");
            }
            preparedStatement.close();
            return port;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getStatus()
    {
        try
        {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("SELECT server_status FROM " + table + " WHERE server_name = ?");
            preparedStatement.setString(1, displayName);
            ResultSet rs = preparedStatement.executeQuery();
            int status = 0;
            while (rs.next())
            {
                status = rs.getInt("server_status");
            }
            preparedStatement.close();
            return status;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setOnlines(int onlines) {
        try {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("UPDATE ed_servers SET server_onlines = ? WHERE server_name = ?");
            preparedStatement.setInt(1, onlines);
            preparedStatement.setString(2, proxyName);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSpigotServerIfNotExists(String serverName, String serverAddress, String serverMotd) throws IOException
    {
        File configFile = new File("config.yml");

        InputStream input = new FileInputStream(configFile);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (!yamlData.containsKey("servers") || !(yamlData.get("servers") instanceof Map))
        {
            yamlData.put("servers", new LinkedHashMap<String, Object>());
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> serversData = (Map<String, Object>)yamlData.get("servers");
        if (serversData.containsKey(serverName) && serversData.get(serverName) instanceof Map)
        {
            System.out.println("Le serveur " + serverName + " existe déjà dans la configuration.");
            return;
        }

        Map<String, Object> serverData = new LinkedHashMap<String, Object>();
        serverData.put("address", serverAddress);
        serverData.put("motd", serverMotd);
        serverData.put("restricted", false);
        serversData.put(serverName, serverData);

        OutputStream output = new FileOutputStream(configFile);
        yaml.dump(yamlData, new OutputStreamWriter(output));
    }

    public void removeSpigotServerIfExists(String serverName) throws IOException
    {
        File configFile = new File("config.yml");

        InputStream input = new FileInputStream(configFile);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (!yamlData.containsKey("servers") || !(yamlData.get("servers") instanceof Map))
        {
            System.out.println("Le serveur " + serverName + " n'existe pas dans la configuration.");
            return;
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> serversData = (Map<String, Object>)yamlData.get("servers");
        if (!serversData.containsKey(serverName) || !(serversData.get(serverName) instanceof Map))
        {
            System.out.println("Le serveur " + serverName + " n'existe pas dans la configuration.");
            return;
        }

        serversData.remove(serverName);

        OutputStream output = new FileOutputStream(configFile);
        yaml.dump(yamlData, new OutputStreamWriter(output));
    }

    public void forceUpdateStatut(int status, String motd, int onlines, int serverID) {
        try {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("UPDATE ed_servers SET server_status = ? WHERE server_name = ?");
            preparedStatement.setInt(1, status);
            preparedStatement.setString(2, displayName);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("UPDATE ed_servers SET server_motd = ? WHERE server_name = ?");
            preparedStatement.setString(1, motd);
            preparedStatement.setString(2, displayName);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("UPDATE ed_servers SET server_onlines = ? WHERE server_name = ?");
            preparedStatement.setInt(1, onlines);
            preparedStatement.setString(2, displayName);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("UPDATE ed_servers SET server_id = ? WHERE server_name = ?");
            preparedStatement.setInt(1, serverID);
            preparedStatement.setString(2, displayName);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getName() { return this.displayName; }
    public String getProxy() { return this.proxyName; }
}
