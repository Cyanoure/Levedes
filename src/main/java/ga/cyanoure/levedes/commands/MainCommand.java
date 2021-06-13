package ga.cyanoure.levedes.commands;

import ga.cyanoure.levedes.Levedes;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainCommand extends DefaultCommand {
    private static String command = "lev\u00E9d\u00E9s";
    private static String description = "Lev\u00E9d\u00E9s plugin parancsai.";
    private static String usage = "/"+command;
    private static ArrayList<String> aliases = new ArrayList<String>(){{
        add("levedes");
    }};

    public MainCommand(){
        super(command,description,usage,aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length > 0 && Bukkit.getServer().getCommandMap().getCommand(args[0]) != null){
            if (args[0].equalsIgnoreCase("reload") && (!(sender instanceof Player) || hasPerm((Player) sender,"admin"))){
                Levedes.getPlugin(Levedes.class).loadConfig();
                cmsg(sender,"Konfigur\u00E1ci\u00F3 \u00FAjrat\u00F6ltve.");
                return true;
            }
            boolean commandExists = false;
            for (DefaultCommand cmd : Levedes.commands){
                if (commandLabel.equalsIgnoreCase(cmd.getLabel()) || cmd.getAliases().contains(commandLabel)){
                    commandExists = true;
                    break;
                }
            }
            if(commandExists) {
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                Bukkit.getServer().getCommandMap().getCommand(args[0]).execute(sender, args[0], subArgs);
                return true;
            }
        }
        cmsg(sender,"&3--- &8[ &bLEV\u00C9D\u00C9S &8] &3---\n" +
                "&cA ter\u00FClet lev\u00E9d\u00E9s\u00E9\u00E9rt "+EcoBridge.buyPriceFormat()+"-t kell fizetned, a lev\u00E9d\u00E9s t\u00F6rl\u00E9s\u00E9\u00E9rt "+EcoBridge.sellPriceFormat()+"-t kapsz vissza.\n" +
                "&8A chunkhat\u00E1rokat az F3+G lenyom\u00E1s\u00E1val tudod megn\u00E9zni.");
        for (DefaultCommand cmd : Levedes.commands){
            cmsg(sender,"&6"+cmd.getUsage()+"&6 - &e"+cmd.getDescription());
        }
        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1){
            List<String> commands = new ArrayList<String>();
            /*commands.add("lev\u00E9d");
            commands.add("lev\u00E9dt\u00F6r\u00F6l");
            commands.add("lev\u00E9denged");
            commands.add("lev\u00E9dtilt");*/
            for (DefaultCommand cmd : Levedes.commands){
                if(cmd.getLabel() != getLabel()) commands.add(cmd.getLabel());
            }
            if(!(sender instanceof Player) || hasPerm((Player) sender,"admin")){
                commands.add("reload");
            }
            return commands;
        }else if(Bukkit.getServer().getCommandMap().getCommand(args[0]) != null){
            String[] subArgs = Arrays.copyOfRange(args,1,args.length);
            return Bukkit.getServer().getCommandMap().getCommand(args[0]).tabComplete(sender,args[0],subArgs);
        }else{
            return new ArrayList<String>();
        }
    }
}
