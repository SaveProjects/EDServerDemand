package fr.edminecoreteam.network.scanners.run;

import fr.edminecoreteam.network.scanners.DeleteFoldersScanner;

public class DeleteFoldersRunnable implements Runnable
{
    private DeleteFoldersScanner scanner;

    @Override
    public void run() {
        if (scanner == null) scanner = new DeleteFoldersScanner();
        scanner.checkFolders();
    }
}
