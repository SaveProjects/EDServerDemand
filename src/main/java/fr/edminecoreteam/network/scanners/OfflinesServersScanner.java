package fr.edminecoreteam.network.scanners;

import java.util.ArrayList;
import java.util.List;

import fr.edminecoreteam.network.database.ServerData;
import fr.edminecoreteam.network.utils.PingServers;
import fr.edminecoreteam.network.utils.ServerList;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class OfflinesServersScanner
{
    public void checkServer()
    {
        List<String> OnlinesServers = new ArrayList<String>();
        List<String> StartServers = new ArrayList<String>();
        ServerList srvList = new ServerList();
        List<String> ServersResponse = srvList.getAllServers();
        String permission = "edmine.admin";
        for(String srv : ServersResponse)
        {
            if(!srv.equalsIgnoreCase("ProxyNetwork"))
            {
                ServerData data = new ServerData(srv);
                if (data.getStatus() > 0)
                {
                    OnlinesServers.add(srv);
                }
            }
        }
        for(String srv : StartServers)
        {
            if(!srv.equalsIgnoreCase("ProxyNetwork"))
            {
                ServerData data = new ServerData(srv);
                if (data.getStatus() == -1)
                {
                    OnlinesServers.add(srv);
                }
            }
        }

        for(String srv : OnlinesServers)
        {
            if (PingServers.ping(srv) == false)
            {
                System.out.println("[WARN] Il semble que le serveur " + srv + " est bugger...");
                ServerData data = new ServerData(srv);
                data.defineStatus(0);
                System.out.println("Statut du serveur " + srv + " update.");
                for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers())
                {
                    if (p.hasPermission(permission))
                    {
                        p.sendMessage(TextComponent.fromLegacyText("§6§l⚠WARN⚠ §7(Il semble que le serveur " + srv + " est bugger...)"));
                        p.sendMessage(TextComponent.fromLegacyText("§7Statut du serveur §e" + srv + "§7 update."));
                    }
                }
            }
            else { /*Rien*/ }
        }
        for (String srv : StartServers)
        {
            if (PingServers.ping(srv) == false)
            {
                System.out.println("[WARN] Il semble que le serveur " + srv + " est bugger...");
                ServerData data = new ServerData(srv);
                data.defineStatus(0);
                System.out.println("Statut du serveur " + srv + " update.");
                for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers())
                {
                    if (p.hasPermission(permission))
                    {
                        p.sendMessage(TextComponent.fromLegacyText("§6§l⚠WARN⚠ §7(Il semble que le serveur " + srv + " est bugger...)"));
                        p.sendMessage(TextComponent.fromLegacyText("§7Statut du serveur §e" + srv + "§7 update."));
                    }
                }
            }
            else { /*Rien*/ }
        }
    }
}
