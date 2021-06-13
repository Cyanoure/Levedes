package ga.cyanoure.levedes;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import ga.cyanoure.levedes.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Levedes extends JavaPlugin {
    public WorldGuard wg;
    public RegionContainer rc;
    public static String prefix = "&8[&3Lev\u00E9d\u00E9s&8] ";
    public static String permPrefix = "levedes";
    public static String globalPermPrefix = "cyanoure";
    public static FileConfiguration config = null;
    public static List<DefaultCommand> commands = new ArrayList<DefaultCommand>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        loadConfig();

        wg = WorldGuard.getInstance();
        rc = wg.getPlatform().getRegionContainer();

        new ClaimCommand();
        new MainCommand();
        new UnclaimCommand();
        new AllowCommand();
        new DisallowCommand();
        new AllowAllCommand();
        new DisallowAllCommand();

        if (!EcoBridge.setupEconomy()){
            System.out.println(ChatColor.translateAlternateColorCodes('&',"&4Nincs Vault plugin!"));
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }

    public void loadConfig(){
        saveDefaultConfig();
        if (config != null){
            reloadConfig();
        }
        config = getConfig();
    }
}
