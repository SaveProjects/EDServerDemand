package fr.edminecoreteam.network.utils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import fr.edminecoreteam.network.Network;
import net.md_5.bungee.api.ProxyServer;

public class PingServers
{

    private static Network network = Network.getNetwork();

    @SuppressWarnings("deprecation")
    public static boolean ping(String serverName)
    {
        int getPort = ProxyServer.getInstance().getServerInfo(serverName).getAddress().getPort();
        String address = network.getConfig("config").getString("servers." + serverName + ".address");
        boolean online = PingServers.getServerStatut(address, getPort);
        if (online == true)
        {
            return true;
        }
        else if (online == false)
        {
            return false;
        }
        return false;
    }

    public static boolean getServerStatut(String IPAdress, int port)
    {
        boolean online = false;
        try
        {
            Socket s = new Socket(IPAdress, port);
            // ONLINE
            s.close();
            online = true;
            return online;
        }
        catch (UnknownHostException ex)
        {
            ex.toString();
        }
        catch (IOException et)
        {
            et.toString();
        }
        return online;
    }


    public static List<String> getList()
    {
        return getGroupList();
    }

    public static List<String> getGroupList()
    {
        List<String> list = new ArrayList<String>();

        //Ajouter les groupes ici
        for (String group : network.getTypes())
        {
            list.add(group);
        }

        return list;

    }
}
