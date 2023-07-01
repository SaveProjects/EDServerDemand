package fr.edminecoreteam.network.scanners.run;

import fr.edminecoreteam.network.scanners.OfflinesServersScanner;

public class OfflinesServersScannerRunnable implements Runnable
{
    private OfflinesServersScanner scanner;

    @Override
    public void run() {
        if (scanner == null) scanner = new OfflinesServersScanner();
        scanner.checkServer();
    }
}
