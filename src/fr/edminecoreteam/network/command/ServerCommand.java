package fr.edminecoreteam.network.command;


import fr.edminecoreteam.network.Network;
import fr.edminecoreteam.network.utils.EDUtils;
import fr.edminecoreteam.network.utils.Messages;
import fr.edminecoreteam.network.utils.PlayerManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ServerCommand extends Command
{
    public ServerCommand(Network network)
    {
        super("edserver", "", "srv", "edsrv");
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) { return; }
        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(sender.getName());
        if(PlayerManager.hasPermission(p.getName(), 14)){
            if (args.length == 0)
            {
                Messages.messageHelp(p);
            }
            if (args.length == 1)
            {
                if (args[0].equalsIgnoreCase("help"))
                {
                    Messages.messageHelp(p);
                }
                if (args[0].equalsIgnoreCase("list"))
                {
                    Messages.getGeneralList(p);
                }
                if (args[0].equalsIgnoreCase("switch"))
                {
                    Messages.errorSynthax(p);
                }
                if (args[0].equalsIgnoreCase("start"))
                {
                    Messages.errorSynthax(p);
                }
                if (args[0].equalsIgnoreCase("stop"))
                {
                    Messages.errorSynthax(p);
                }
                if (args[0].equalsIgnoreCase("check"))
                {
                    Messages.errorSynthax(p);
                }
                if (args[0].equalsIgnoreCase("update"))
                {
                    Messages.errorSynthax(p);
                }
                if(args[0].equalsIgnoreCase("stopall")){
                    Messages.getGeneralList(p);
                }
            }
            if (args.length == 2)
            {
                if (args[0].equalsIgnoreCase("list"))
                {
                    Messages.getGroupsList(sender, args[1]);
                }
                if (args[0].equalsIgnoreCase("switch"))
                {
                    Messages.switchServer(sender, args[1]);
                }
                if (args[0].equalsIgnoreCase("check"))
                {
                    Messages.checkDebugServer(sender, args[1]);
                }
                if (args[0].equalsIgnoreCase("start"))
                {
                    EDUtils.startServer(sender, args[1]);
                }
                if (args[0].equalsIgnoreCase("stop"))
                {
                    EDUtils.stopServer(sender, args[1]);
                }
                if(args[0].equalsIgnoreCase("stopall")){
                    EDUtils.stopAllServerGroup(sender, args[1]);
                }

            }
        }else{
            p.sendMessage(TextComponent.fromLegacyText("§cCommande inconnue..."));
        }

    }

}
