package fr.edminecoreteam.network.scanners;

import fr.edminecoreteam.network.Network;
import fr.edminecoreteam.network.database.ServerData;

public class PlayersOnlineScanner
{
    private static final Network network = Network.getNetwork();

    public void checkOnlines()
    {
        int onlines = network.getProxy().getOnlineCount();

        ServerData data = new ServerData(null);
        data.setOnlines(onlines);
    }
}
