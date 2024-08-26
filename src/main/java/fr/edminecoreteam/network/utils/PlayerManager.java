package fr.edminecoreteam.network.utils;

import fr.edminecoreteam.network.account.AccountInfo;

public class PlayerManager {

    public static boolean hasPermission(String p, int permission){
        AccountInfo accountInfo = new AccountInfo(p);
        if(accountInfo.getRankModule() >= permission){
            return true;
        }
        return false;
    }

}
