package ga.cyanoure.levedes.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import ga.cyanoure.levedes.Levedes;
import ga.cyanoure.levedes.protection.ChunkZone;
import ga.cyanoure.levedes.protection.ProtectionManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DisallowCommand extends DefaultCommand{
    private static String command = "lev\u00E9dtilt";
    private static String description = "T\u00F6rli a j\u00E1t\u00E9kost a lev\u00E9d\u00E9sb\u0151l.";
    private static String usage = "/"+command+" <j\u00E1t\u00E9kos>";
    private static ArrayList<String> aliases = new ArrayList<String>(){{
        add("levedtilt");
    }};
    public DisallowCommand(){
        super(command,description,usage,aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (onlyPlayer(sender)){
            Player p = (Player) sender;
            if (args.length > 0) {
                if (hasPerm(p, "use")) {
                    RegionContainer rc = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionManager rm = rc.get(BukkitAdapter.adapt(p.getWorld()));
                    int[] chunkPos = ChunkZone.locationToChunk(p.getLocation());
                    ApplicableRegionSet set = rc.createQuery().getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
                    ProtectedRegion ownedRegion = null;
                    for (ProtectedRegion region : set) {
                        if (region.isOwner(WorldGuardPlugin.inst().wrapPlayer(p)) || hasPerm(p, "admin")) {
                            ownedRegion = region;
                        }
                    }
                    if (set.size() == 0) {
                        cmsg(p, Levedes.prefix + "&cEzen a chunkon nincs lev\u00E9d\u00E9s!");
                    } else if (ownedRegion == null) {
                        cmsg(p, Levedes.prefix + "&cEz a ter\u00FClet nem a ti\u00E9d!");
                    } else {
                        if (ownedRegion instanceof ProtectedCuboidRegion) {
                            if (ownedRegion.isMember(args[0])) {
                                ProtectionManager protMan = new ProtectionManager((ProtectedCuboidRegion) ownedRegion);
                                protMan.removeMember(args[0]);
                                cmsg(p, Levedes.prefix + "&2J\u00E1t\u00E9kos sikeresen t\u00F6r\u00F6lve.");
                            }else{
                                cmsg(p,Levedes.prefix+"&cA j\u00E1t\u00E9kos nincs hozz\u00E1adva a lev\u00E9d\u00E9shez!");
                            }
                        }
                    }
                } else {
                    noPerm(p);
                }
            }else{
                cmsg(p,Levedes.prefix+"&cHelyes haszn\u00E1lat: &6/lev\u00E9dtilt &c<&8j\u00E1t\u00E9kosn\u00E9v&c>");
            }
        }
        return true;
    }
}
