package fr.edminecoreteam.network.scanners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.edminecoreteam.network.Network;
import fr.edminecoreteam.network.database.ServerData;
import fr.edminecoreteam.network.servers.Server;

public class ServerScanner
{
    private static Network network = Network.getNetwork();

    private Random random;

    public ServerScanner() {
        this.random = new Random();
    }

    public void serverScanner()
    {
        List<String> groups = new ArrayList<String>();
        List<String> onlinesServers = new ArrayList<String>();
        List<String> offlinesServers = new ArrayList<String>();
        List<String> needStartServers = new ArrayList<String>();
        for(String getGroups : network.getConfig("config").getSection("groups").getKeys())
        {
            groups.add(getGroups);
        }
        for(String getOnlinesServers : network.getServersList())
        {
            ServerData data = new ServerData(getOnlinesServers);
            if (data.getStatus() == 1)
            {
                onlinesServers.add(getOnlinesServers);
            }
        }
        for(String getOfflinesServers : network.getServersList())
        {
            ServerData data = new ServerData(getOfflinesServers);
            if (data.getStatus() == 0)
            {
                offlinesServers.add(getOfflinesServers);
            }
        }
        for(String getNeedStartServers : network.getServersList())
        {
            ServerData data = new ServerData(getNeedStartServers);
            if (data.getStatus() == 2)
            {
                needStartServers.add(getNeedStartServers);
            }
        }

        for (String GROUP : groups)
        {
            List<String> GROUPOnlines = new ArrayList<String>();
            List<String> GROUPOfflines = new ArrayList<String>();
            List<String> GROUPNeedStart = new ArrayList<String>();
            for(String SRV : onlinesServers)
            {
                String getGroup = network.getConfig("config").getString("servers." + SRV.replaceAll("\\d", "") + ".group");
                if (getGroup.equalsIgnoreCase(GROUP))
                {
                    GROUPOnlines.add(SRV);
                }
            }
            for(String SRV : offlinesServers)
            {
                String getGroup = network.getConfig("config").getString("servers." + SRV.replaceAll("\\d", "") + ".group");
                if (getGroup.equalsIgnoreCase(GROUP))
                {
                    GROUPOfflines.add(SRV);
                }
            }
            for(String SRV : needStartServers)
            {
                String getGroup = network.getConfig("config").getString("servers." + SRV.replaceAll("\\d", "") + ".group");
                if (getGroup.equalsIgnoreCase(GROUP))
                {
                    GROUPNeedStart.add(SRV);
                }
            }
            if (GROUPNeedStart.size() != 0)
            {
                for (String SRV : GROUPNeedStart)
                {
                    ServerData dataSRV = new ServerData(SRV);
                    dataSRV.defineStatus(1);
                    if (network.getConfig("config").getBoolean("config.randomServer") == false)
                    {
                        ServerData data = new ServerData(GROUPOfflines.get(0));
                        String groupName = GROUP;
                        String srvName = data.getName();
                        String addressName = data.getIP();
                        int portName = data.getPort();
                        Server srv = new Server(groupName, srvName, addressName, portName);
                        srv.start();
                    }
                    else if (network.getConfig("config").getBoolean("config.randomServer") == true)
                    {
                        ServerData data = new ServerData(GROUPOfflines.get(random.nextInt(GROUPOfflines.size())));
                        String groupName = GROUP;
                        String srvName = data.getName();
                        String addressName = data.getIP();
                        int portName = data.getPort();
                        Server srv = new Server(groupName, srvName, addressName, portName);
                        srv.start();
                    }
                }
            }
            if (GROUPOnlines.size() == 0)
            {
                if (network.getConfig("config").getBoolean("config.randomServer") == false)
                {
                    ServerData data = new ServerData(GROUPOfflines.get(0));
                    String groupName = GROUP;
                    String srvName = data.getName();
                    String addressName = data.getIP();
                    int portName = data.getPort();
                    Server srv = new Server(groupName, srvName, addressName, portName);
                    srv.start();
                }
                else if (network.getConfig("config").getBoolean("config.randomServer") == true)
                {
                    ServerData data = new ServerData(GROUPOfflines.get(random.nextInt(GROUPOfflines.size())));
                    String groupName = GROUP;
                    String srvName = data.getName();
                    String addressName = data.getIP();
                    int portName = data.getPort();
                    Server srv = new Server(groupName, srvName, addressName, portName);
                    srv.start();
                }
            }
        }
    }
}
