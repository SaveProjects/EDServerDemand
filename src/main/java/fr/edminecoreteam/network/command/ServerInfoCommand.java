package fr.edminecoreteam.network.command;

import fr.edminecoreteam.network.Network;
import fr.edminecoreteam.network.database.ServerData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ServerInfoCommand extends Command
{

    public ServerInfoCommand(Network network)
    {
        super("info");
    }

    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(sender.getName());
        String ServerName = p.getServer().getInfo().getName();
        int getOnlinesPlayers = p.getServer().getInfo().getPlayers().size();
        ServerData data = new ServerData(ServerName);
        String getServer = data.getName();
        String getGroup = getServer.replaceAll("\\d", "").toUpperCase();
        int getServerID = data.getServerID();
        if (args.length == 0)
        {
            messageInfo(sender, getServer, getGroup, getOnlinesPlayers, getServerID);
        }
        else
        {
            return;
        }
    }

    public void messageInfo(CommandSender sender, String serverName, String group, int onlines, int serverid) {
        sender.sendMessage(TextComponent.fromLegacyText(""));
        sender.sendMessage(TextComponent.fromLegacyText(" §7» §d§lInformations §d(Serveur):"));
        sender.sendMessage(TextComponent.fromLegacyText(""));
        sender.sendMessage(TextComponent.fromLegacyText(" §f• §7Type de serveur: §b" + group));
        sender.sendMessage(TextComponent.fromLegacyText(" §f• §7Serveur: §a" + serverName));
        sender.sendMessage(TextComponent.fromLegacyText(" §f• §7Joueurs: §e" + onlines));
        sender.sendMessage(TextComponent.fromLegacyText(" §f• §7ID du serveur: §d#" + serverid));
        sender.sendMessage(TextComponent.fromLegacyText(""));
    }
}