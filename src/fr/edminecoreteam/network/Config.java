package fr.edminecoreteam.network;

import java.io.File;

import net.md_5.bungee.config.Configuration;

public class Config
{
    private static Network network = Network.getNetwork();
    private static Configuration config = Network.getConfiguration();

    public void setDefaultConfig()
    {
        config = network.getConfig("config");
        config.set("config.serverPerGroup", "0");
        config.set("config.modelPath", "/test/test/");
        config.set("config.randomServer", "false");

        config.set("groups.TESTSERVER", "DYNAMIC");

        config.set("servers.TestServer.startPort", "11000");
        config.set("servers.TestServer.address", "localhost");
        config.set("servers.TestServer.group", "TESTSERVER");
        config.set("servers.TestServer.templatePath", "/test/test");

        network.saveConfig(config, "config");
    }

    public void setDefaultMySQLConfig()
    {
        config = network.getConfig("mysql");
        config.set("mysql.host", "none");
        config.set("mysql.database", "none");
        config.set("mysql.user", "none");
        config.set("mysql.password", "none");

        network.saveConfig(config, "mysql");
    }

    public void importGroups()
    {
        for(String group : network.getConfig("config").getSection("groups").getKeys())
        {
            network.getTypes().add(group);
        }
    }

    public void checkOrCreateDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            boolean created = directory.mkdir();

            if (!created) {
                // Handle the case where the directory could not be created
                throw new RuntimeException("Could not create directory: " + directoryPath);
            }
        }
    }
}
