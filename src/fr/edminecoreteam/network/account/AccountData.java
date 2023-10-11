package fr.edminecoreteam.network.account;

import fr.edminecoreteam.network.database.InstanceSQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountData
{
    private String p;

    public AccountData(String p)
    {
        this.p = p;
    }

    public boolean hasRank()
    {
        try
        {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("SELECT player_rank_id FROM ed_ranks WHERE player_name = ?");
            preparedStatement.setString(1, p);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        }
        catch (SQLException e)
        {
            e.toString();
            return false;
        }
    }

    public int getRankID()
    {
        try
        {
            PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("SELECT player_rank_id FROM ed_ranks WHERE player_name = ?");
            preparedStatement.setString(1, p);
            int power = 0;
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
            {
                power = rs.getInt("player_rank_id");
            }
            preparedStatement.close();
            return power;
        }
        catch (SQLException e)
        {
            e.toString();
            return 0;
        }
    }

    public String getRankType()
    {
        if (hasRank())
        {
            try
            {
                PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("SELECT player_rank_type FROM ed_ranks WHERE player_name = ?");
                preparedStatement.setString(1, p);
                String response = "";
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next())
                {
                    response = rs.getString("player_rank_type");
                }
                preparedStatement.close();
                return response;
            }
            catch (SQLException e)
            {
                e.toString();
            }
        }
        return "";
    }

    public String getPurchaseDate()
    {
        if (hasRank())
        {
            try
            {
                PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("SELECT player_rank_purchase_date FROM ed_ranks WHERE player_name = ?");
                preparedStatement.setString(1, p);
                String response = "";
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next())
                {
                    response = rs.getString("player_rank_purchase_date");
                }
                preparedStatement.close();
                return response;
            }
            catch (SQLException e)
            {
                e.toString();
            }
        }
        return "";
    }

    public String getDeadLineDate()
    {
        if (hasRank())
        {
            try
            {
                PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("SELECT player_rank_deadline_date FROM ed_ranks WHERE player_name = ?");
                preparedStatement.setString(1, p);
                String response = "";
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next())
                {
                    response = rs.getString("player_rank_deadline_date");
                }
                preparedStatement.close();
                return response;
            }
            catch (SQLException e)
            {
                e.toString();
            }
        }
        return "";
    }

    public int getRankModule()
    {
        if (hasRank())
        {
            try
            {
                PreparedStatement preparedStatement = InstanceSQL.getConnection().prepareStatement("SELECT player_modulable_rank FROM ed_ranks WHERE player_name = ?");
                preparedStatement.setString(1, p);
                int response = 0;
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next())
                {
                    response = rs.getInt("player_modulable_rank");
                }
                preparedStatement.close();
                return response;
            }
            catch (SQLException e)
            {
                e.toString();
            }
        }
        return 0;
    }

    public String getRankName(){
        try{
            PreparedStatement ps = InstanceSQL.getConnection().prepareStatement("SELECT player_rank_name FROM ed_ranks WHERE player_name = ?");
            ps.setString(1, p);
            String response = "§7Le §astaff";
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                response = rs.getString("player_rank_name");
            }
            if(response.equalsIgnoreCase("MOD")){
                response = "§7Le §9modérateur";
            }else if(response.equalsIgnoreCase("DEV")){
                response = "§7Le §5développeur";
            }else if (response.equalsIgnoreCase("RESP")){
                response = "§7Le §4responsable";
            }else if(response.equalsIgnoreCase("ADMIN")){
                response = "§7L'§cadministrateur";
            }else {
                response = "§7Le §astaff";
            }
            ps.close();
            return response;
        }
        catch (SQLException e){
            e.toString();
        }
        return "§7Le §astaff";
    }

}
