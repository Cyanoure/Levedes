package ga.cyanoure.levedes.commands;

import ga.cyanoure.levedes.Levedes;
import ga.cyanoure.levedes.protection.ChunkZone;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ClaimCommand extends DefaultCommand {
    private static String command = "lev\u00E9d";
    private static String description = "Lev\u00E9di a chunkot, amelyen \u00E1llsz.";
    private static String usage = "/"+command;
    private static ArrayList<String> aliases = new ArrayList<String>(){{
        add("leved");
    }};

    public ClaimCommand(){
        super(command,description,usage,aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (onlyPlayer(sender)){
            Player p = (Player) sender;
            if (hasPerm(p,"use")){
                ChunkZone claim = new ChunkZone(p.getLocation());
                if (!claim.overlaps()){
                    if (EcoBridge.buyChunk(p)) {
                        claim.setOwner(p);
                        claim.create();
                        cmsg(p, Levedes.prefix + "&2Sikeres lev\u00E9d\u00E9s.");
                    }
                }else{
                    cmsg(p,Levedes.prefix+"&cEzen a chunkon m\u00E1r van lev\u00E9dett ter\u00FClet!");
                }
            }else{
                noPerm(p);
            }
        }
        return true;
    }
}
