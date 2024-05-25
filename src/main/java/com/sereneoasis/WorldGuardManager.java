package com.sereneoasis;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldGuardManager {

    private final WorldGuard worldGuard;

    private final RegionContainer regionContainer;

    public static StateFlag SERENITY_FLAG;

    public WorldGuardManager(){
        worldGuard = WorldGuard.getInstance();
        regionContainer = worldGuard.getPlatform().getRegionContainer();
    }

    public static void registerFlag(){
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            StateFlag flag = new StateFlag("serenity", true);
            registry.register(flag);
            SERENITY_FLAG = flag; // only set our field if there was no error
        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("serenity");
            if (existing instanceof StateFlag) {
                SERENITY_FLAG = (StateFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
            }
        }
    }

    public boolean canBend(Player player){
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        RegionQuery query = regionContainer.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
//        Bukkit.broadcastMessage(String.valueOf(set.testState( localPlayer, SERENITY_FLAG)));

        return set.testState( localPlayer, SERENITY_FLAG);
    }

    private ProtectedRegion getProtectedRegion(String protectedRegion, World world){
        RegionManager regions = regionContainer.get(BukkitAdapter.adapt(world));
        if (regions != null) {
            return regions.getRegion(protectedRegion);
        } else {
            return null;
        }
    }
}
