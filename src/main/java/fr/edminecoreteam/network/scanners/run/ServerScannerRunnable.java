package fr.edminecoreteam.network.scanners.run;

import fr.edminecoreteam.network.scanners.ServerScanner;

public class ServerScannerRunnable implements Runnable
{
    private ServerScanner scanner;

    @Override
    public void run() {
        if (scanner == null) scanner = new ServerScanner();
        scanner.serverScanner();
    }
}
