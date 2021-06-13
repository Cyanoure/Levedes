package ga.cyanoure.levedes.commands;

import ga.cyanoure.levedes.Levedes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DefaultCommand extends Command {
    private Levedes plugin;

    public DefaultCommand(String command, String description, String usage, ArrayList<String> aliases){
        super(command,description,usage,aliases);
        plugin = Levedes.getPlugin(Levedes.class);
        Bukkit.getServer().getCommandMap().register(command,this);
        Levedes.commands.add(this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return false;
    }

    protected void cmsg(CommandSender sender, String msg){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',msg));
    }
    protected void cmsg(Player sender, String msg){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',msg));
    }

    protected boolean onlyPlayer(CommandSender sender){
        if (sender instanceof Player){
            return true;
        }else{
            cmsg(sender,Levedes.prefix+"&4Ezt a parancsot csak j\u00E1t\u00E9kos futtathatja!");
            return false;
        }
    }

    protected boolean hasPerm(CommandSender sender,String perm){
        return sender.hasPermission(Levedes.permPrefix+"."+perm) || sender.hasPermission(Levedes.permPrefix+".admin") || sender.hasPermission(Levedes.globalPermPrefix+".admin");
    }
    protected boolean hasPerm(Player sender,String perm){
        return sender.hasPermission(Levedes.permPrefix+"."+perm) || sender.hasPermission(Levedes.permPrefix+".admin") || sender.hasPermission(Levedes.globalPermPrefix+".admin");
    }

    private String noPermMSG = Levedes.prefix+"&cItt ehhez nincs jogod!";
    protected void noPerm(CommandSender sender){
        cmsg(sender,noPermMSG);
    }
    protected void noPerm(Player sender){
        cmsg(sender,noPermMSG);
    }
}
