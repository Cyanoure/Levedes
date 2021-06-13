package ga.cyanoure.levedes.protection;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import ga.cyanoure.levedes.Levedes;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChunkZone {
    public ProtectionManager region;
    public Levedes plugin;
    public World regionWorld;
    public static int[] locationToChunk(Location loc){
        /*int cX = (int)Math.floor(loc.getBlockX()/16);
        int cZ = (int)Math.floor(loc.getBlockZ()/16);*/
        int cX = loc.getChunk().getX();
        int cZ = loc.getChunk().getZ();
        return new int[]{cX,cZ};
    }
    public ChunkZone(int chunk_x, int chunk_z, World world){
        onCreate(chunk_x,chunk_z,world);
    }
    public ChunkZone(Location loc){
        int[] chunkLoc = locationToChunk(loc);
        onCreate(chunkLoc[0],chunkLoc[1],loc.getWorld());
    }
    public void onCreate(int chunk_x,int chunk_z, World world){
        plugin = Levedes.getPlugin(Levedes.class);
        int x1 = chunk_x*16;
        int y1 = 0;
        int z1 = chunk_z*16;
        int x2 = x1+15;
        int y2 = 255;
        int z2 = z1+15;
        BlockVector3 min = BlockVector3.at(x1,y1,z1);
        BlockVector3 max = BlockVector3.at(x2,y2,z2);
        region = new ProtectionManager("plevedes_x"+String.valueOf(chunk_x)+"_z"+String.valueOf(chunk_z),min,max);
        region.reset();

        regionWorld = world;
    }
    public boolean overlaps(){
        ArrayList<ProtectedRegion> candidates = new ArrayList<ProtectedRegion>();
        candidates.add(region);

        RegionManager rm = plugin.rc.get(BukkitAdapter.adapt(regionWorld));

        List<ProtectedRegion> regions = new ArrayList<ProtectedRegion>(rm.getRegions().values());
        List<ProtectedRegion> overlappingRegions = region.getIntersectingRegions(regions);

        if (overlappingRegions.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    public void setOwner(Player p){
        region.setOwner(p);
    }
    public void setOwner(String p){
        region.setOwner(p);
    }
    public void setOwner(UUID p){
        region.setOwner(p);
    }
    public void create(){
        RegionManager rm = plugin.rc.get(BukkitAdapter.adapt(regionWorld));
        rm.addRegion(region);
    }
}
