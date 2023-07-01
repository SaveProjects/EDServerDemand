package fr.edminecoreteam.network.servers;

import java.io.File;
import java.io.IOException;

import fr.edminecoreteam.network.Network;
import fr.edminecoreteam.network.database.ServerData;
import fr.edminecoreteam.network.scanners.DeleteFoldersScanner;
import fr.edminecoreteam.network.utils.Manager;

public class Server
{

    private static Network network = Network.getNetwork();
    public ServerData data;
    public String group;
    public String groupType;
    public String serverName;
    public String address;
    public int port;
    private String templatePath;
    private String modelPath;

    public Server(String group, String serverName, String address, int port)
    {
        this.group = group;
        this.serverName = serverName;
        this.address = address;
        this.port = port;
        this.templatePath = network.getConfig("config").getString("servers." + serverName.replaceAll("\\d", "") + ".templatePath");
        this.modelPath = network.getConfig("config").getString("config.modelPath") + group + "/" + serverName;
        this.data = new ServerData(serverName);
        this.groupType = network.getConfig("config").getString("groups." + group);
    }

    public void start()
    {
        ServerData data = new ServerData(serverName);
        Manager manager = new Manager();
        String groupFile = network.getConfig("config").getString("servers." + data.getName().replaceAll("\\d", "") + ".group");
        File file = new File(network.getConfig("config").getString("config.modelPath") + groupFile + "/" + serverName);
        DeleteFoldersScanner folder = new DeleteFoldersScanner();
        if (file.exists())
        {
            try {
                folder.deleteDirectory(file);
            } catch (IOException e) {
                System.out.println("Erreur de suppresion du serveur: " + network.getConfig("config").getString("config.modelPath" + groupFile.replaceAll("\\d", "").toUpperCase() + "/" + serverName));
            }
        }
        try {
            manager.createServer(group, serverName, port, templatePath, modelPath);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String startpath = modelPath;
        ProcessBuilder builder = new ProcessBuilder(new String[] { "sh", "start.sh" });
        try
        {
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("");
            System.out.println("  EDMINE SERVER-DEMAND:");
            System.out.println("    - Demarrage d'un serveur...");
            System.out.println("    - Groupe: " + group);
            System.out.println("    - Serveur: " + serverName);
            System.out.println("    - Adresse: " + address);
            System.out.println("    - Port: " + port);
            System.out.println("    - Chemin d'acces: " + startpath);
            System.out.println("");
            System.out.println("----------------------------------------------------------------------------");
            builder.directory(new File(startpath));
            Process process = builder.start();
            process.waitFor();
            data.defineStatus(-1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop()
    {
        String startpath = modelPath;
        ProcessBuilder builder = new ProcessBuilder(new String[] { "sh", "stop.sh" });
        try
        {
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("");
            System.out.println("  EDMINE SERVER-DEMAND:");
            System.out.println("    - Arret d'un serveur...");
            System.out.println("    - Groupe: " + group);
            System.out.println("    - Serveur: " + serverName);
            System.out.println("    - Adresse: " + address);
            System.out.println("    - Port: " + port);
            System.out.println("    - Chemin d'acces: " + startpath);
            System.out.println("");
            System.out.println("----------------------------------------------------------------------------");
            builder.directory(new File(startpath));
            Process process = builder.start();
            process.waitFor();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
