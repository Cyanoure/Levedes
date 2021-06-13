package ga.cyanoure.levedes.protection;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import ga.cyanoure.levedes.Levedes;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ProtectionManager extends ProtectedCuboidRegion {
    public ProtectedCuboidRegion region;
    public ProtectionManager(String name, BlockVector3 min, BlockVector3 max){
        super(name,min,max);
        region = this;
    }
    public ProtectionManager(ProtectedCuboidRegion reg){
        super(reg.getId(),reg.getMinimumPoint(),reg.getMaximumPoint());
        region = reg;
        updateSelf();
    }
    private void updateSelf(){
        setFlags(region.getFlags());
        setOwners(region.getOwners());
        setMembers(region.getMembers());
    }
    public void setOwner(Player p){
        DefaultDomain owners = new DefaultDomain();
        owners.addPlayer(WorldGuardPlugin.inst().wrapPlayer(p));
        region.setOwners(owners);
        updateSelf();
    }
    public void setOwner(String p){
        DefaultDomain owners = new DefaultDomain();
        owners.addPlayer(p);
        region.setOwners(owners);
    }
    public void setOwner(UUID p){
        DefaultDomain owners = new DefaultDomain();
        owners.addPlayer(p);
        region.setOwners(owners);
        updateSelf();
    }

    public void addMember(Player p){
        DefaultDomain domain = getMembers();
        domain.addPlayer(WorldGuardPlugin.inst().wrapPlayer(p));
        region.setMembers(domain);
        updateSelf();
    }
    public void addMember(String p){
        DefaultDomain domain = getMembers();
        domain.addPlayer(p);
        region.setMembers(domain);
        updateSelf();
    }
    public void addMember(UUID p){
        DefaultDomain domain = getMembers();
        domain.addPlayer(p);
        region.setMembers(domain);
        updateSelf();
    }

    public void removeMember(Player p){
        DefaultDomain domain = getMembers();
        domain.removePlayer((LocalPlayer) p);
        region.setMembers(domain);
        updateSelf();
    }
    public void removeMember(String p){
        DefaultDomain domain = getMembers();
        domain.removePlayer(p);
        region.setMembers(domain);
        updateSelf();
    }
    public void removeMember(UUID p){
        DefaultDomain domain = getMembers();
        domain.removePlayer(p);
        region.setMembers(domain);
        updateSelf();
    }
    public void setFlagToGroup(StateFlag flag,String group){
        RegionGroup groupValue = null;
        RegionGroupFlag groupFlag = flag.getRegionGroupFlag();
        try {
            groupValue = groupFlag.parseInput(FlagContext.create().setSender(null).setInput(group).setObject("region", this).build());
        }catch (Exception e){
            e.printStackTrace();
        }
        setFlag(groupFlag,groupValue);
        updateSelf();
    }
    public void reset(){
        region.setMembers(new DefaultDomain());
        resetFlags();
    }
    public void softReset(){
        resetFlags();
    }
    public void resetFlags(){
        region.setFlags(new HashMap<Flag<?>, Object>());
        setFlag(Flags.BUILD, StateFlag.State.DENY);
        setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
        setFlag(Flags.INTERACT, StateFlag.State.DENY);
        setFlag(Flags.DENY_MESSAGE,Levedes.prefix+"&cEz egy v\u00E9dett ter\u00FClet!");

        setFlagToGroup(Flags.BUILD,"non_members");
        setFlagToGroup(Flags.BLOCK_BREAK,"non_members");
        setFlagToGroup(Flags.INTERACT,"non_members");
        updateSelf();
    }
    public ProtectedCuboidRegion get(){
        updateSelf();
        return region;
    }
}
