package fr.edminecoreteam.network.scanners;

import java.io.File;
import java.io.IOException;

import fr.edminecoreteam.network.Network;
import fr.edminecoreteam.network.database.ServerData;


public class DeleteFoldersScanner
{
    private static Network network = Network.getNetwork();

    public void checkFolders() {
        processSubFolders();
    }

    private void processSubFolders() {
        for (String srv : network.getServersList())
        {
            ServerData data = new ServerData(srv);
            if (data.getStatus() == 0)
            {
                String group = network.getConfig("config").getString("servers." + data.getName().replaceAll("\\d", "") + ".group");
                File file = new File(network.getConfig("config").getString("config.modelPath") + group + "/" + srv);
                if (file.exists())
                {
                    try {
                        deleteDirectory(file);
                    } catch (IOException e) {
                        System.out.println("Erreur de suppresion du serveur: " + network.getConfig("config").getString("config.modelPath" + srv.replaceAll("\\d", "").toUpperCase() + "/" + srv));
                    }
                }
            }
        }
    }

    public void deleteDirectory(File dir) throws IOException {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                deleteDirectory(file);
            }
        }

        if (!dir.delete()) {
            throw new IOException("Failed to delete directory " + dir.getAbsolutePath());
        }
    }
}
