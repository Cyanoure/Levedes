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

public class AllowAllCommand extends DefaultCommand{
    private static String command = "levedengedmind";
    private static String description = "Hozz\u00E1adja a j\u00E1t\u00E9kost a lev\u00E9d\u00E9sekhez a jelenlegi vil\u00E1gban.";
    private static String usage = "/"+command+" <j\u00E1t\u00E9kos>";
    private static ArrayList<String> aliases = new ArrayList<String>(){{
        add("lev\u00E9dengedmind");
    }};
    public AllowAllCommand(){
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
                    int n = 0;
                    for (ProtectedRegion reg : set){
                        if(reg instanceof ProtectedCuboidRegion && reg.isOwner(WorldGuardPlugin.inst().wrapPlayer(p))){
                            if(!reg.isMember(args[0])){
                                ProtectionManager protMan = new ProtectionManager((ProtectedCuboidRegion) reg);
                                protMan.addMember(args[0]);
                                n++;
                            }
                        }
                    }
                    cmsg(p, Levedes.prefix + "&2J\u00E1t\u00E9kos sikeresen hozz\u00E1adva "+n+" lev\u00E9d\u00E9shez.");
                } else {
                    noPerm(p);
                }
            }else{
                cmsg(p,Levedes.prefix+"&cHelyes haszn\u00E1lat: &6/lev\u00E9dengedmind &c<&8j\u00E1t\u00E9kosn\u00E9v&c>");
            }
        }
        return true;
    }
}
