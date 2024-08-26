package fr.edminecoreteam.network.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.edminecoreteam.network.Network;
import fr.edminecoreteam.network.database.ServerData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Messages
{
    private static Network network = Network.getNetwork();

    public static void messageHelp(CommandSender sender)
    {
        sender.sendMessage(TextComponent.fromLegacyText(""));
        sender.sendMessage(TextComponent.fromLegacyText(" §7» §6§lCentre d'aide §6(EDMINE SERVER DEMAND):"));
        sender.sendMessage(TextComponent.fromLegacyText(""));
        sender.sendMessage(TextComponent.fromLegacyText(" §7• §d/§fedserver help §8§l» §7Ouvrir le menu d'aide."));
        sender.sendMessage(TextComponent.fromLegacyText(" §7• §d/§fedserver list §b[groupe] §8§l» §7Afficher la liste des groupes."));
        sender.sendMessage(TextComponent.fromLegacyText(" §7• §d/§fedserver switch §a[serveur] §8§l» §7Changer de serveur."));
        sender.sendMessage(TextComponent.fromLegacyText(" §7• §d/§fedserver start §a[serveur] §8§l» §7Démmarer un serveur."));
        sender.sendMessage(TextComponent.fromLegacyText(" §7• §d/§fedserver stop §a[serveur] §8§l» §7Arrêter un serveur."));
        sender.sendMessage(TextComponent.fromLegacyText(" §7• §d/§fedserver stopAll §a[groupe] §8§l» §7Arrêter un groupe de serveur."));
        sender.sendMessage(TextComponent.fromLegacyText(" §7• §d/§fedserver check §a[serveur] §8§l» §7Analyser un serveur dans la bdd."));
        sender.sendMessage(TextComponent.fromLegacyText(""));
    }


    public static void checkDebugServer(CommandSender sender, String server)
    {

        sender.sendMessage(TextComponent.fromLegacyText("§7Analyse du serveur §e" + server + "§7 en cours..."));
        ServerInfo srvInfo = ProxyServer.getInstance().getServerInfo(server);

        if (srvInfo != null)
        {
            if (PingServers.ping(server) == true)
            {
                sender.sendMessage(TextComponent.fromLegacyText("§aTout est bon dans le cochon ! §7Le serveur §e" + server + "§7 semble démmaré."));
            }
            else if (PingServers.ping(server) == false)
            {
                ServerData data = new ServerData(server);
                if (data.getStatus() > 0)
                {
                    data.forceUpdateStatut(0, null, 0, 0);
                    sender.sendMessage(TextComponent.fromLegacyText("§6§l⚠WARN⚠ §7(Il semble que le serveur été bugger...)"));
                    sender.sendMessage(TextComponent.fromLegacyText("§7Statut du serveur §e" + server + "§7 update."));
                }
                else if (data.getStatus() == 0)
                {
                    sender.sendMessage(TextComponent.fromLegacyText("§aTout est bon ! §7Le serveur §e" + server + "§7 semble arrêté."));
                }
            }
        }
        else
        {
            sender.sendMessage(TextComponent.fromLegacyText("§cErreur d'analyse sur §c" + server + "§c... (serveur introuvable)"));
        }
    }

    public static void switchServer(CommandSender sender, String server)
    {
        ProxiedPlayer p = (ProxiedPlayer) sender;
        sender.sendMessage(TextComponent.fromLegacyText("§7Connexion vers §e" + server + "§7..."));
        try {
            p.connect(ProxyServer.getInstance().getServerInfo(server));
            if (p.getServer().getInfo().getName() == server)
            {
                //sender.sendMessage(TextComponent.fromLegacyText("§7Connexion vers §a" + server + "§7 réussite !"));
            }
            else
            {
                ServerInfo info = ProxyServer.getInstance().getServerInfo(server);
                if (!PingServers.ping(info.getName()))
                {
                    sender.sendMessage(TextComponent.fromLegacyText("§cErreur de connexion vers §c" + server + "§c... (serveur hors-ligne)"));
                }
            }
        }
        catch (Exception e) {
            sender.sendMessage(TextComponent.fromLegacyText("§cErreur de connexion vers §c" + server + "§c... (serveur introuvable)"));
            return;
        }
    }

    public static void switchGroupServer(CommandSender sender, String group)
    {
        List<String> OnlinesServers = new ArrayList<String>();
        Random r = new Random();
        for (String groups : network.getTypes())
        {
            for (String srv : network.getServersList())
            {
                String srvGroup = network.getConfig("config").getString("servers." + srv.replaceAll("\\d", "") + ".group");
                if (srvGroup.equalsIgnoreCase(groups))
                {
                    if (PingServers.ping(srv) == true)
                    {
                        OnlinesServers.add(srv);
                    }
                }
            }
        }
        String finalServer = "";
        int randomIndex = r.nextInt(OnlinesServers.size());
        finalServer = OnlinesServers.get(randomIndex);
        ProxiedPlayer p = (ProxiedPlayer) sender;
        sender.sendMessage(TextComponent.fromLegacyText("§7Connexion vers un serveur §e" + group + "§7..."));
        try {
            p.connect(ProxyServer.getInstance().getServerInfo(finalServer));
            if (p.getServer().getInfo().getName() == finalServer)
            {
                //sender.sendMessage(TextComponent.fromLegacyText("§7Connexion vers §a" + group + "§7 réussite !"));
            }
            else
            {
                ServerInfo info = ProxyServer.getInstance().getServerInfo(finalServer);
                if (!PingServers.ping(info.getName()))
                {
                    sender.sendMessage(TextComponent.fromLegacyText("§cErreur de connexion vers §c" + finalServer + "§c... (aucun serveurs disponible)"));
                }
            }
        }
        catch (Exception e) {
            sender.sendMessage(TextComponent.fromLegacyText("§cErreur de connexion vers §c" + finalServer + "§c... (groupe introuvable)"));
            return;
        }
    }

    @SuppressWarnings("deprecation")
    public static void getGeneralList(CommandSender sender)
    {
        List<String> ServersResponse = PingServers.getList();
        List<String> finalList = new ArrayList<String>();
        for(String srv : ServersResponse)
        {
            finalList.add(srv);
        }

        sender.sendMessage(TextComponent.fromLegacyText(""));
        sender.sendMessage(TextComponent.fromLegacyText(" §7» §6§lListe des serveurs §6(Général):"));
        sender.sendMessage(TextComponent.fromLegacyText(""));
        sender.sendMessage(TextComponent.fromLegacyText("  §d§lInformations:"));
        sender.sendMessage(TextComponent.fromLegacyText(" §7• §fNombre de groupes total: §e" + finalList.size()));
        sender.sendMessage(TextComponent.fromLegacyText(""));
        sender.sendMessage(TextComponent.fromLegacyText("  §6§lListe des groupes:"));

        TextComponent message = new TextComponent(" §f➡ §e");
        int counter = 0;
        int finalCount = finalList.size();
        for (String srv : finalList)
        {
            TextComponent tokenComponent = new TextComponent("§e" + srv);
            tokenComponent.setBold(true);
            tokenComponent.setUnderlined(true);
            tokenComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Cliquez ici pour voir.")));
            tokenComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/edserver list " + srv.toString()));

            message.addExtra(tokenComponent);
            counter++;
            if (counter == finalCount)
            {
                message.addExtra("§7.");
            }
            else
            {
                message.addExtra("§7, ");
            }
        }
        sender.sendMessage(message);

        sender.sendMessage(TextComponent.fromLegacyText(""));
    }

    @SuppressWarnings("deprecation")
    public static void getGroupsList(CommandSender sender, String groupName)
    {
        List<String> OnlinesServers = new ArrayList<String>();
        List<String> OfflinesServers = new ArrayList<String>();
        for (String group : network.getTypes())
        {
            for (String srv : network.getServersList())
            {
                String srvGroup = network.getConfig("config").getString("servers." + srv.replaceAll("\\d", "") + ".group");
                if (srvGroup.equalsIgnoreCase(group))
                {
                    if (PingServers.ping(srv) == true)
                    {
                        OnlinesServers.add(srv);
                    }
                    else
                    {
                        OfflinesServers.add(srv);
                    }
                }
            }
        }
        sender.sendMessage(TextComponent.fromLegacyText(""));
        sender.sendMessage(TextComponent.fromLegacyText(" §7» §6§lListe des serveurs §6(" + groupName + "):"));
        sender.sendMessage(TextComponent.fromLegacyText(""));
        sender.sendMessage(TextComponent.fromLegacyText("  §d§lInformations:"));
        sender.sendMessage(TextComponent.fromLegacyText(" §7• §fNombre de serveurs total: §e" + network.getConfig("config").getInt("config.serverPerGroup")));
        sender.sendMessage(TextComponent.fromLegacyText(" §7• §fNombre de serveurs en-lignes§f: §a" + OnlinesServers.size()));
        sender.sendMessage(TextComponent.fromLegacyText(" §7• §fNombre de serveurs hors-lignes§f: §c" + OfflinesServers.size()));
        sender.sendMessage(TextComponent.fromLegacyText(" "));
        sender.sendMessage(TextComponent.fromLegacyText("  §6§lListe des serveurs:"));
        TextComponent messageOnline = new TextComponent(" §f➡ §7Serveurs en-lignes§7: ");
        int counterOnline = 0;
        int finalCountOnline = OnlinesServers.size();
        for (String srv : OnlinesServers)
        {
            TextComponent tokenComponent = new TextComponent("§a" + srv);
            tokenComponent.setBold(true);
            tokenComponent.setUnderlined(true);
            tokenComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Joueur(s): §f" + ProxyServer.getInstance().getServerInfo(srv).getPlayers().size() + "\n" + "§7Cliquez ici pour vous connecter.")));
            tokenComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/edserver switch " + srv.toString()));

            messageOnline.addExtra(tokenComponent);
            counterOnline++;
            if (counterOnline == finalCountOnline)
            {
                messageOnline.addExtra("§7.");
            }
            else
            {
                messageOnline.addExtra("§7, ");
            }
        }
        sender.sendMessage(messageOnline);

        TextComponent messageOffline = new TextComponent(" §f➡ §7Serveurs hors-lignes§7: ");
        int counterOffline = 0;
        int finalCountOffline = OfflinesServers.size();
        for (String srv : OfflinesServers)
        {
            TextComponent tokenComponent = new TextComponent("§8" + srv);
            tokenComponent.setBold(true);
            tokenComponent.setUnderlined(true);
            tokenComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Cliquez ici pour démarrer ce serveur.")));
            tokenComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/edserver start " + srv.toString()));

            messageOffline.addExtra(tokenComponent);
            counterOffline++;
            if (counterOffline == finalCountOffline)
            {
                messageOffline.addExtra("§7.");
            }
            else
            {
                messageOffline.addExtra("§7, ");
            }
        }
        sender.sendMessage(messageOffline);
        sender.sendMessage(TextComponent.fromLegacyText(" "));
    }





    public static void errorSynthax(CommandSender sender)
    {
        ServerList srvList = new ServerList();
        sender.sendMessage(TextComponent.fromLegacyText("§cErreur de synthaxe..."));
        sender.sendMessage(TextComponent.fromLegacyText(srvList.getAllServers().toString()));
    }
}
