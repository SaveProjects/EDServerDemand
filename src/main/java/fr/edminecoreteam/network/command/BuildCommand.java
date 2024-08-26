package fr.edminecoreteam.network.command;

import fr.edminecoreteam.network.Network;
import fr.edminecoreteam.network.utils.PlayerManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BuildCommand extends Command {
    public BuildCommand(Network network) {
        super("build");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) { return; }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(PlayerManager.hasPermission(p.getName(), 14)){
            try{
                ((ProxiedPlayer) sender).connect(ProxyServer.getInstance().getServerInfo("build1"));
            }catch (Exception e){
                p.sendMessage(TextComponent.fromLegacyText("§cUne erreur est survenue... Veuillez contacter le responsable de développement !"));
                System.out.println("§c Une erreur est survenue avec la commande /build : " + e.getMessage());
            }
        }else{
            p.sendMessage(TextComponent.fromLegacyText("§cCommande inconnue..."));
        }
    }
}
