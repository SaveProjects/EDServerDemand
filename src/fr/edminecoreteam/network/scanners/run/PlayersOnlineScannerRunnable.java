package fr.edminecoreteam.network.scanners.run;

import fr.edminecoreteam.network.scanners.PlayersOnlineScanner;

public class PlayersOnlineScannerRunnable implements Runnable
{
    private PlayersOnlineScanner scanner;

    @Override
    public void run() {
        if (scanner == null) scanner = new PlayersOnlineScanner();
        scanner.checkOnlines();
    }
}
