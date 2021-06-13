package ga.cyanoure.levedes.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import ga.cyanoure.levedes.Levedes;
import ga.cyanoure.levedes.protection.ChunkZone;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UnclaimCommand extends DefaultCommand {
    private static String command = "lev\u00E9dt\u00F6r\u00F6l";
    private static String description = "T\u00F6rli a lev\u00E9d\u00E9st arr\u00F3l a chunkr\u00F3l, amelyen \u00E1llsz.";
    private static String usage = "/"+command;
    private static ArrayList<String> aliases = new ArrayList<String>(){{
        add("levedtorol");
    }};
    public UnclaimCommand(){
        super(command,description,usage,aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (onlyPlayer(sender)){
            Player p = (Player) sender;
            if (hasPerm(p,"use")){
                RegionContainer rc = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionManager rm = rc.get(BukkitAdapter.adapt(p.getWorld()));
                int[] chunkPos = ChunkZone.locationToChunk(p.getLocation());
                ApplicableRegionSet set = rc.createQuery().getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
                ProtectedRegion ownedRegion = null;
                for (ProtectedRegion region : set){
                    if (region.isOwner(WorldGuardPlugin.inst().wrapPlayer(p)) || hasPerm(p,"admin")){
                        ownedRegion = region;
                    }
                }
                if (set.size() == 0){
                    cmsg(p, Levedes.prefix+"&cEzen a chunkon nincs lev\u00E9d\u00E9s!");
                }else if(ownedRegion == null){
                    cmsg(p,Levedes.prefix+"&cEz a ter\u00FClet nem a ti\u00E9d!");
                }else{
                    EcoBridge.sellChunk(p);
                    rm.removeRegion(ownedRegion.getId());
                    cmsg(p,Levedes.prefix+"&2Lev\u00E9d\u00E9s t\u00F6r\u00F6lve.");
                }
            }else{
                noPerm(p);
            }
        }
        return true;
    }
}
