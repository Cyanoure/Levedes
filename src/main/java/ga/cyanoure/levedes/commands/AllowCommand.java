package ga.cyanoure.levedes.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
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

public class AllowCommand extends DefaultCommand {
    private static String command = "lev\u00E9denged";
    private static String description = "Hozz\u00E1adja a j\u00E1t\u00E9kost a lev\u00E9d\u00E9shez.";
    private static String usage = "/"+command+" <j\u00E1t\u00E9kos>";
    private static ArrayList<String> aliases = new ArrayList<String>(){{
        add("levedenged");
    }};
    public AllowCommand(){
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
                    boolean isOwner = false;
                    for (ProtectedRegion region : set) {
                        if (region.isOwner(WorldGuardPlugin.inst().wrapPlayer(p)) || hasPerm(p, "admin")) {
                            ownedRegion = region;
                        }
                        if (region.isOwner(WorldGuardPlugin.inst().wrapPlayer(p))){
                            isOwner = true;
                        }
                    }
                    if (set.size() == 0) {
                        cmsg(p, Levedes.prefix + "&cEzen a chunkon nincs lev\u00E9d\u00E9s!");
                    } else if (ownedRegion == null) {
                        cmsg(p, Levedes.prefix + "&cEz a ter\u00FClet nem a ti\u00E9d!");
                    } else {
                        if (!ownedRegion.isOwner(args[0]) || (isOwner && p.getName().equalsIgnoreCase(args[0]))) {
                            if (ownedRegion instanceof ProtectedCuboidRegion) {
                                ProtectionManager protMan = new ProtectionManager((ProtectedCuboidRegion) ownedRegion);
                                protMan.addMember(args[0]);
                                cmsg(p, Levedes.prefix + "&2J\u00E1t\u00E9kos sikeresen hozz\u00E1adva.");
                            }
                        }else{
                            cmsg(p,Levedes.prefix+"&cA ter\u00FClet tulajdonosa nem lehet tag!");
                        }
                    }
                } else {
                    noPerm(p);
                }
            }else{
                cmsg(p,Levedes.prefix+"&cHelyes haszn\u00E1lat: &6/lev\u00E9denged &c<&8j\u00E1t\u00E9kosn\u00E9v&c>");
            }
        }
        return true;
    }
}
