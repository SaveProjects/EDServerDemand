package fr.edminecoreteam.network;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fr.edminecoreteam.network.command.ServerCommand;
import fr.edminecoreteam.network.command.ServerInfoCommand;
import fr.edminecoreteam.network.command.ServerReplaceCommand;
import fr.edminecoreteam.network.database.InstanceSQL;
import fr.edminecoreteam.network.database.ServerData;
import fr.edminecoreteam.network.database.UtilsSQL;
import fr.edminecoreteam.network.scanners.DeleteFoldersScanner;
import fr.edminecoreteam.network.scanners.run.DeleteFoldersRunnable;
import fr.edminecoreteam.network.scanners.run.OfflinesServersScannerRunnable;
import fr.edminecoreteam.network.scanners.run.PlayersOnlineScannerRunnable;
import fr.edminecoreteam.network.scanners.run.ServerScannerRunnable;
import fr.edminecoreteam.network.servers.Server;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;


public class Network extends Plugin
{
    private static Network instance;
    private static Plugin plugin;
    private static InstanceSQL database;
    private static Configuration configuration;
    private List<String> serversGroup;
    private List<String> serversList;
    public Network()
    {
        serversGroup = new ArrayList<String>();
        serversList = new ArrayList<String>();
    }

    @Override
    public void onEnable() {
        instance = this;

        createFile("config");
        createFile("mysql");

        databaseConnect();
        Config config = new Config();
        config.importGroups();

        loadServers();
        executeCommand("greload");
        getProxy().getScheduler().schedule((Plugin)this, (Runnable)new ServerScannerRunnable(), 2L, 25L, TimeUnit.SECONDS);
        getProxy().getScheduler().schedule((Plugin)this, (Runnable)new DeleteFoldersRunnable(), 1L, 50L, TimeUnit.SECONDS);
        getProxy().getScheduler().schedule((Plugin)this, (Runnable)new OfflinesServersScannerRunnable(), 1L, 25L, TimeUnit.SECONDS);
        getProxy().getScheduler().schedule((Plugin)this, (Runnable)new PlayersOnlineScannerRunnable(), 1L, 15L, TimeUnit.SECONDS);
        getProxy().getPluginManager().registerCommand(this, (Command)new ServerInfoCommand(this));
        getProxy().getPluginManager().registerCommand(this, (Command)new ServerCommand(this));
        getProxy().getPluginManager().registerCommand(this, (Command)new ServerReplaceCommand(this));
        getLogger().info("loaded");
    }

    @Override
    public void onDisable() {
        stopAllServersAndRemove();
    }

    public void loadServers()
    {
        ServerData networkData = new ServerData(null);
        Config config = new Config();
        networkData.createNetworkServer("localhost", 25565);
        int getServerLimit = (int) getConfig("config").getInt("config.serverPerGroup");
        for(String group : getConfig("config").getSection("groups").getKeys())
        {
            String path = getConfig("config").getString("config.modelPath") + group;
            config.checkOrCreateDirectory(path);
            for(String srvTypeList : getConfig("config").getSection("servers").getKeys())
            {
                String getGroup = getConfig("config").getString("servers." + srvTypeList + ".group");
                if (getGroup.equalsIgnoreCase(group))
                {
                    System.out.println(group + ":");
                    String groupAddress = getConfig("config").getString("servers." + srvTypeList + ".address");
                    int port = getConfig("config").getInt("servers." + srvTypeList + ".startPort");
                    for(int i = 1; i < getServerLimit + 1; i++)
                    {
                        port++;
                        System.out.println(" - " + srvTypeList + i);
                        ServerData srvData = new ServerData(srvTypeList + i);
                        srvData.createSpigotServer(groupAddress, port);
                        try {
                            srvData.addSpigotServerIfNotExists(srvTypeList + i, groupAddress + ":" + port, group);
                        } catch (IOException e) {
                            System.out.println("Erreur de load...");
                        }
                        getServersList().add(srvTypeList + i);

                    }
                }
            }
        }
    }

    public void stopAllServersAndRemove()
    {
        for (String srvList : getServersList())
        {
            ServerData srvData = new ServerData(srvList);
            String groupName = getConfig("config").getString("servers." + srvData.getName().replaceAll("\\d", "") + ".group");
            String srvName = srvData.getName();
            String addressName = srvData.getIP();
            int portName = srvData.getPort();
            Server srv = new Server(groupName, srvName, addressName, portName);
            if (srvData.getStatus() >= 1)
            {
                srv.stop();
            }
            srvData.removeServer();
            try {
                srvData.removeSpigotServerIfExists(srvList);
            } catch (IOException e) {
                System.out.println("Erreur de unload...");
            }
        }
        DeleteFoldersScanner delFolder = new DeleteFoldersScanner();
        delFolder.checkFolders();
    }

    private void databaseConnect()
    {
        (Network.database = new InstanceSQL("jdbc:mysql://",
                getConfig("mysql").getString("mysql.host"),
                getConfig("mysql").getString("mysql.database"),
                getConfig("mysql").getString("mysql.user"),
                getConfig("mysql").getString("mysql.password")
        )).connexion();
        if (!database.isOnline()) { return; }
        refreshConnexion();
        UtilsSQL utilsSQL = new UtilsSQL();
        utilsSQL.getDefaultMySQLConfigAndConnect();
    }

    public void refreshConnexion()
    {
        ProxyServer.getInstance().getScheduler().schedule((Plugin)this, (Runnable)new Runnable()
        {
            @Override
            public void run()
            {
                if (!Network.database.isOnline())
                {
                    Network.database.connexion();
                    run();
                }
            }
        }, 0L, 20L, TimeUnit.SECONDS);
    }

    private void createFile(String fileName)
    {
        if (!getDataFolder().exists())
        {
            getDataFolder().mkdir();
        }

        File file = new File(getDataFolder(), fileName + ".yml");

        if (!file.exists())
        {
            try {
                file.createNewFile();

                if (fileName.equals("mysql"))
                {
                    Config config = new Config();
                    config.setDefaultMySQLConfig();
                }
                if (fileName.equals("config"))
                {
                    Config config = new Config();
                    config.setDefaultConfig();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Configuration getConfig(String fileName)
    {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), fileName + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveConfig(Configuration config, String fileName)
    {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), fileName + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeCommand(String command)
    {
        CommandSender console = ProxyServer.getInstance().getConsole();
        ProxyServer.getInstance().getPluginManager().dispatchCommand(console, command);
    }

    public List<String> getServersList() { return serversList; }
    public List<String> getTypes() { return serversGroup; }
    public static Network getNetwork() { return instance; }
    public static Plugin getPlugin() { return plugin; }
    public static Connection getDatabase() { return InstanceSQL.getConnection(); }
    public static Configuration getConfiguration() { return configuration; }
}
