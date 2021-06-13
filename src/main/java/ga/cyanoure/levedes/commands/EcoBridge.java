package ga.cyanoure.levedes.commands;

import ga.cyanoure.levedes.Levedes;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EcoBridge {
    private static Economy econ = null;
    private static boolean economyReady = false;
    private static String colored(String txt){
        return ChatColor.translateAlternateColorCodes('&',txt);
    }
    public static boolean setupEconomy(){
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null){
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null){
            return false;
        }
        econ = rsp.getProvider();
        economyReady = econ != null;
        return econ != null;
    }
    public static double getBalance(Player p){
        if (!economyReady) System.out.println(colored("&4Nincs Vault plugin!"));
        return economyReady ? econ.getBalance(p) : 0;
    }
    public static boolean giveMoney(Player p, double amount){
        if (amount == 0) return true;
        if (economyReady) {
            EconomyResponse r = econ.depositPlayer(p, amount);
            boolean success = r.transactionSuccess();
            if (success){
                p.sendMessage(colored("&cEnnyivel gazdagabb lett\u00E9l: "+econ.format(amount)));
            }
            return success;
        }else{
            System.out.println(colored("4Nincs Vault plugin!"));
            return false;
        }
    }
    public static boolean takeMoney(Player p, double amount){
        if (amount == 0) return true;
        if (economyReady) {
            if (canPayout(p, amount)) {
                boolean success = econ.withdrawPlayer(p, amount).transactionSuccess();
                if (success){
                    p.sendMessage(colored("&cEnnyivel szeg\u00E9nyebb lett\u00E9l: "+econ.format(amount)));
                }
                return success;
            } else {
                return false;
            }
        }else{
            System.out.println(colored("&4Nincs Vault plugin!"));
            return false;
        }
    }
    public static boolean canPayout(Player p, double amount){
        return getBalance(p) >= amount;
    }

    public static boolean buyChunk(Player p){
        double price = Levedes.config.getDouble("claimPrice");
        if (takeMoney(p, price)){
            return true;
        }else{
            if (economyReady) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Levedes.prefix+"&4Nincs n\u00E1lad "+econ.format(price)+", hogy lev\u00E9dd ezt a ter\u00FCletet!"));
            }else{
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Levedes.prefix+"&4HIBA!"));
            }
            return false;
        }
    }

    public static boolean sellChunk(Player p){
        return giveMoney(p, Levedes.config.getDouble("unclaimMoneyBack"));
    }

    public static String buyPriceFormat(){
        String price = "?";
        if (economyReady){
            price = econ.format(Levedes.config.getDouble("claimPrice"));
        }
        return price;
    }

    public static String sellPriceFormat(){
        String price = "?";
        if (economyReady){
            price = econ.format(Levedes.config.getDouble("unclaimMoneyBack"));
        }
        return price;
    }
}
