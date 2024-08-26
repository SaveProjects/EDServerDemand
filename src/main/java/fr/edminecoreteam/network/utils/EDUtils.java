package fr.edminecoreteam.network.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.edminecoreteam.network.Network;
import fr.edminecoreteam.network.database.ServerData;
import fr.edminecoreteam.network.servers.Server;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class EDUtils
{
    private static final Network network = Network.getNetwork();

    public static void startServer(CommandSender sender, String serverName)
    {
        sender.sendMessage(TextComponent.fromLegacyText("§7Recherche du serveur §e" + serverName +  "§7 en cours..."));
        ServerData data = new ServerData(serverName);
        String getGroup = network.getConfig("config").getString("servers." + data.getName().replaceAll("\\d", "") + ".group");
        String groupName = getGroup;
        String srvName = data.getName();
        String addressName = data.getIP();
        int portName = data.getPort();
        Server srv = new Server(groupName, srvName, addressName, portName);

        if (data.getStatus() > 0)
        {
            sender.sendMessage(TextComponent.fromLegacyText("§cErreur, le serveur " + serverName + " §csemble déjà démarrer..."));
            return;
        }
        else if (data.getStatus() == 0)
        {
            srv.start();
            sender.sendMessage(TextComponent.fromLegacyText("§7Tentative de démarrage du serveur §a" + serverName +  "§7 en cours..."));
        }
    }

    public static void stopServer(CommandSender sender, String serverName)
    {
        sender.sendMessage(TextComponent.fromLegacyText("§7Recherche du serveur §e" + serverName +  "§7 en cours..."));
        ServerData data = new ServerData(serverName);
        String getGroup = network.getConfig("config").getString("servers." + data.getName().replaceAll("\\d", "") + ".group");
        String groupName = getGroup;
        String srvName = data.getName();
        String addressName = data.getIP();
        int portName = data.getPort();
        Server srv = new Server(groupName, srvName, addressName, portName);

        if (data.getStatus() == 0)
        {
            sender.sendMessage(TextComponent.fromLegacyText("§cErreur, le serveur " + serverName + " §csemble déjà arrêter..."));
            return;
        }
        else
        {
            srv.stop();
            sender.sendMessage(TextComponent.fromLegacyText("§7Tentative d'arrêt du serveur §a" + serverName +  "§7 en cours..."));
        }
    }

    public static void stopAllServerGroup(CommandSender sender, String groupName) {
        String startGroupName = groupName.substring(0, 1).toUpperCase();
        String endGroupName = groupName.substring(1).toLowerCase();
        for (String srv : network.getServersList()) {
            if (srv.startsWith(startGroupName + endGroupName)) {
                if (PingServers.ping(srv)) {
                    stopServer(sender, srv);
                }
            }
        }

    }

    public static void copyContent(File source, File target)
    {
        try
        {
            if(source.isDirectory())
            {
                if(!target.exists())
                    target.mkdirs();
                String files[] = source.list();
                for (String file : files)
                {
                    File srcFile = new File(source, file);
                    File destFile = new File(target, file);
                    copyContent(srcFile, destFile);
                }
            }
            else
            {
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(target);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0)
                    out.write(buffer, 0, length);
                in.close();
                out.close();
            }
        } catch (IOException e)
        {
            //Rien
        }
    }
}
