package fr.edminecoreteam.network.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.edminecoreteam.network.Network;
import fr.edminecoreteam.network.database.InstanceSQL;

public class ServerList
{
    private Network network;

    public ServerList()
    {
        this.network = Network.getNetwork();
    }

    public List<String> getServer(String serverType)
    {
        try
        {
            String response;
            List<String> serverList = new ArrayList<String>();
            for(int i = 1; i < network.getConfig("config").getInt("config.serverPerGroup") + 1; i++)
            {
                int ServerNumber = i;
                response = serverType + ServerNumber;
                PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("SELECT server_name FROM ed_servers WHERE server_name = ?");
                preparedStatement.setString(1, response);
                ResultSet rs = preparedStatement.executeQuery();
                String result = "";
                while (rs.next())
                {
                    result = rs.getString("server_name");
                    serverList.add(result);
                }
                preparedStatement.close();
            }
            return serverList;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getAllServers()
    {
        try
        {
            List<String> serverList = new ArrayList<String>();
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("SELECT server_name FROM ed_servers");
            ResultSet rs = preparedStatement.executeQuery();
            String result = "";
            while (rs.next())
            {
                result = rs.getString("server_name");
                serverList.add(result);
            }
            preparedStatement.close();

            return serverList;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
