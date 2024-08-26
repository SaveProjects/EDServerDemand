package fr.edminecoreteam.network.account;

import java.util.HashMap;
import java.util.Map;

public class AccountInfo
{
    private Map<String, AccountInfo> rankInfo;
    private String p;
    private AccountData accountData;;

    public AccountInfo(String p) {
        this.p = p;
        this.rankInfo = new HashMap<String, AccountInfo>();
        this.accountData = new AccountData(p);
        this.rankInfo.put(p, this);
    }

    public AccountInfo getAccountInfos(String player) { return rankInfo.get(player); }

    public boolean hasRank() { return accountData.hasRank(); }


    public int getRankID() { return accountData.getRankID(); }

    public int getRankModule() { return accountData.getRankModule(); }

    public String getPurchaseDate() { return accountData.getPurchaseDate(); }

    public String getDeadLineDate() { return accountData.getDeadLineDate(); }

    public String getRankType() { return accountData.getRankType(); }
    public String getRankName() { return accountData.getRankName(); }
}
