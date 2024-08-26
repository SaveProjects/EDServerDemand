package fr.edminecoreteam.network.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class Manager
{

    public void createServer(String groupName, String serverName, int port, String templatePath, String modelPath) throws IOException {
        // Copie le dossier LobbyTemplate
        File templateDir = new File(templatePath);
        File newDir = new File(modelPath);
        copyFolder(templateDir, newDir);

        // Modifie le fichier server.properties
        File propertiesFile = new File(newDir, "server.properties");
        BufferedReader reader = new BufferedReader(new FileReader(propertiesFile));
        String line = reader.readLine();
        StringBuilder builder = new StringBuilder();

        while (line != null) {
            if (line.startsWith("server-name=")) {
                line = "server-name=" + serverName;
            } else if (line.startsWith("server-port=")) {
                line = "server-port=" + port;
            }
            builder.append(line).append("\n");
            line = reader.readLine();
        }

        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(propertiesFile));
        writer.write(builder.toString());
        writer.close();

        // Modifie le fichier start.sh
        File startFile = new File(newDir, "start.sh");
        reader = new BufferedReader(new FileReader(startFile));
        line = reader.readLine();
        builder = new StringBuilder();

        while (line != null) {
            if (line.startsWith("screen -S ")) {
                line = "screen -S " + serverName + " " + line.substring("screen -S Template ".length());
            } else if (line.startsWith("cd ")) {
                line = "cd " + newDir.getAbsolutePath();
            }
            builder.append(line).append("\n");
            line = reader.readLine();
        }

        reader.close();
        writer = new BufferedWriter(new FileWriter(startFile));
        writer.write(builder.toString());
        writer.close();

        // Modifie le fichier stop.sh
        File stopFile = new File(newDir, "stop.sh");
        reader = new BufferedReader(new FileReader(stopFile));
        line = reader.readLine();
        builder = new StringBuilder();

        while (line != null) {
            if (line.startsWith("screen -p 0 -S Template -X eval")) {
                line = "screen -p 0 -S " + serverName + " -X eval \"stuff \\\"stop\\\"\\015\"";
            }
            builder.append(line).append("\n");
            line = reader.readLine();
        }

        reader.close();
        writer = new BufferedWriter(new FileWriter(stopFile));
        writer.write(builder.toString());
        writer.close();
    }

    public void copyFolder(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }

            String[] files = source.list();

            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);

                copyFolder(srcFile, destFile);
            }
        } else {
            Files.copy(source.toPath(), destination.toPath());
        }
    }
}
