package com.sereneoasis.listeners;

import com.sereneoasis.Serenity;
import com.sereneoasis.SerenityPlayer;
import com.sereneoasis.ability.BendingManager;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.Archetype;

import com.sereneoasis.archetypes.chaos.*;
import com.sereneoasis.archetypes.earth.*;

import com.sereneoasis.archetypes.ocean.FrostTsunami;
import com.sereneoasis.archetypes.ocean.Gimbal;
import com.sereneoasis.archetypes.ocean.Torrent;
import com.sereneoasis.archetypes.ocean.WaterSpout;
import com.sereneoasis.archetypes.sky.*;
import com.sereneoasis.archetypes.sun.*;
import com.sereneoasis.displays.SerenityBoard;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.*;

import static com.sereneoasis.SerenityPlayer.getSerenityPlayer;
import static com.sereneoasis.SerenityPlayer.removeAttributePlayer;

/**
 * @author Sakrajin
 * Main listener for all serenity events
 */
public class SerenityListener implements Listener {



    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();
        SerenityPlayer.loadPlayer(player.getUniqueId(), player);

        Bukkit.getScheduler().runTaskLater(Serenity.getPlugin(), () -> {
            SerenityPlayer.initialisePlayer(player);

        }, 150L);

    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        SerenityBoard.removeScore(player);

        SerenityPlayer serenityPlayer = SerenityPlayer.getSerenityPlayer(player);

        Serenity.getComboManager().removePlayer(player);

        SerenityPlayer.upsertPlayer(serenityPlayer);

        removeAttributePlayer(player, serenityPlayer);


        SerenityPlayer.removePlayerFromMap(player);

    }

    @EventHandler
    public void onHitEntity(EntityDamageByEntityEvent e){
        if (! (e.getDamager() instanceof Player)){
            return;
        }

        Player player = (Player) e.getDamager();

        SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayer(player);
        if (sPlayer == null) {
            return;
        }
        String ability = sPlayer.getHeldAbility();

        switch (ability){
            case "RockKick":
                if (CoreAbility.hasAbility(player, RockKick.class)) {
                    CoreAbility.getAbility(player, RockKick.class).setHasClicked();
                }
                break;
        }

//        boolean isValidAttack = true;
//
//        switch (ability){
//            case "Jab":
//                new Jab(player);
//                break;
//            case "Hook":
//                new Hook(player);
//                break;
//            case "Cross":
//                new Cross(player);
//                break;
//            default:
//                isValidAttack = false;
//        }
//
//        if (isValidAttack) {
//            Serenity.getComboManager().addRecentlyUsed(player, ability, ClickType.LEFT);
//            e.setCancelled(true);
//        }
       // Bukkit.getPluginManager().callEvent(new PlayerInteractEvent(player, Action.LEFT_CLICK_AIR, player.getInventory().getItemInMainHand(), null, null, EquipmentSlot.HAND));
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e ) throws ReflectiveOperationException{


        Player player = e.getPlayer();
        if (player == null) {
            return;
        }



        SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayer(player);
        if (sPlayer == null) {
            return;
        }
        String ability = sPlayer.getHeldAbility();

        if (ability != null) {

            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Serenity.getComboManager().addRecentlyUsed(player, ability, ClickType.RIGHT);
                switch(ability) {
                }
            }
        }


    }

    @EventHandler
    public void onSwing(PlayerAnimationEvent e) throws ReflectiveOperationException {
        Player player = e.getPlayer();
        if (player == null) {
            return;
        }

        SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayer(player);
        if (sPlayer == null) {
            return;
        }
        String ability = sPlayer.getHeldAbility();


        if (ability == null) {
            return;
        }

        Serenity.getComboManager().addRecentlyUsed(player, ability, ClickType.LEFT);

        BendingManager.getInstance().handleRedirections(player, ClickType.LEFT);

        switch (ability) {
            case "RockKick":
                CoreAbility.getAbilities(player, RockKick.class).forEach(rockKick -> rockKick.setHasClicked());
                break;

            case "TerraLine":
                if (CoreAbility.hasAbility(e.getPlayer(), TerraLine.class)) {
                    CoreAbility.getAbility(e.getPlayer(), TerraLine.class).setHasClicked();
                }
                break;
            case "EarthWall":
                new EarthWall(player);
                break;
            case "StoneShred":
                if (CoreAbility.hasAbility(e.getPlayer(), StoneShred.class)) {
                    CoreAbility.getAbility(e.getPlayer(), StoneShred.class).setHasClicked();
                }
                break;
            case "TerraSurf":
                new TerraSurf(player);
                break;
            case "TectonicWave":
                if (CoreAbility.hasAbility(e.getPlayer(), TectonicWave.class)) {
                    CoreAbility.getAbility(e.getPlayer(), TectonicWave.class).setHasClicked();
                }
                break;
            case "RockRing":
                if (CoreAbility.hasAbility(e.getPlayer(), RockRing.class)) {
                    CoreAbility.getAbility(e.getPlayer(), RockRing.class).setHasClicked();
                } else {
                    new RockRing(player);
                }
                break;
            case "Singularity":
                if (CoreAbility.hasAbility(e.getPlayer(), Singularity.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Singularity.class).setHasClicked();
                }
                break;
            case "ShadowStep":
                if (CoreAbility.hasAbility(e.getPlayer(), ShadowStep.class)) {
                    CoreAbility.getAbility(e.getPlayer(), ShadowStep.class).setHasClicked();
                } else {
                    new ShadowStep(player);
                }
                break;
            case "AbyssalFall":
                if (CoreAbility.hasAbility(e.getPlayer(), AbyssalFall.class)) {
                    CoreAbility.getAbility(e.getPlayer(), AbyssalFall.class).setHasClicked();
                }
                break;
            case "Limbo":
                if (CoreAbility.hasAbility(e.getPlayer(), Limbo.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Limbo.class).setHasClicked();
                } else {
                    new Limbo(player);
                }
                break;
            case "VoidChasm":
                if (CoreAbility.hasAbility(e.getPlayer(), VoidChasm.class)) {
                    CoreAbility.getAbility(e.getPlayer(), VoidChasm.class).setHasClicked();
                } else {
                    new VoidChasm(player);
                }
                break;
            case "SoulSlash":
                new SoulSlash(player);
                break;
            case "ChaoticVoid":
                if (CoreAbility.hasAbility(e.getPlayer(), ChaoticVoid.class)) {
                    CoreAbility.getAbility(e.getPlayer(), ChaoticVoid.class).setHasClicked();
                } else {
                    new ChaoticVoid(player);
                }
                break;
            case "Supernova":
                if (CoreAbility.hasAbility(e.getPlayer(), Supernova.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Supernova.class).setHasClicked();
                } else {
                    new Supernova(player);
                }
                break;
            case "PhantomBreath":
                if (CoreAbility.hasAbility(e.getPlayer(), PhantomBreath.class)) {
                    CoreAbility.getAbility(e.getPlayer(), PhantomBreath.class).setHasClicked();
                }
                break;
            case "CruelSun":
                if (CoreAbility.hasAbility(e.getPlayer(), CruelSun.class)) {
                    CoreAbility.getAbility(e.getPlayer(), CruelSun.class).setHasClicked();
                }
                break;
            case "FlamingRays":
                if (CoreAbility.hasAbility(e.getPlayer(), FlamingRays.class)) {
                    CoreAbility.getAbility(e.getPlayer(), FlamingRays.class).setHasClicked();
                }
                break;
            case "SolarBeam":
                if (CoreAbility.hasAbility(e.getPlayer(), SolarBeam.class)) {
                    CoreAbility.getAbility(e.getPlayer(), SolarBeam.class).setHasClicked();
                }
                break;
            case "SolarBarrage":
                if (CoreAbility.hasAbility(e.getPlayer(), SolarBarrage.class)) {
                    CoreAbility.getAbility(e.getPlayer(), SolarBarrage.class).setHasClicked();
                }
                break;
            case "Daybreak":
                new Daybreak(player);
                break;
            case "Sunrise":
                if (CoreAbility.hasAbility(e.getPlayer(), Sunrise.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Sunrise.class).setHasClicked();
                } else {
                    new Sunrise(player);
                }
                break;

            case "SkyBlast":
                if (CoreAbility.hasAbility(e.getPlayer(), SkyBlast.class)) {
                    CoreAbility.getAbility(e.getPlayer(), SkyBlast.class).setHasClicked();
                }
                break;
            case "Nimbus":
                if (CoreAbility.hasAbility(e.getPlayer(), Nimbus.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Nimbus.class).setHasClicked();
                } else {
                    new Nimbus(player);
                }
                break;
            case "SkyRipper":
                if (CoreAbility.hasAbility(e.getPlayer(), SkyRipper.class)) {
                    CoreAbility.getAbility(e.getPlayer(), SkyRipper.class).setHasClicked();
                }
                break;
            case "Cyclone":
                if (CoreAbility.hasAbility(e.getPlayer(), Cyclone.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Cyclone.class).setHasClicked();
                } else {
                    new Cyclone(player);
                }
                break;
            case "CloudStep":
                if (CoreAbility.hasAbility(e.getPlayer(), CloudStep.class)) {
                    CoreAbility.getAbility(e.getPlayer(), CloudStep.class).setHasClicked();
                } else {
                    new CloudStep(player);
                }
                break;
            case "HeavenSlash":
                new HeavenSlash(player);
                break;
            case "ThunderStrike":
                if (CoreAbility.hasAbility(e.getPlayer(), ThunderStrike.class)) {
                    CoreAbility.getAbility(e.getPlayer(), ThunderStrike.class).setHasClicked();
                }
                break;
            case "LightningBolts":
                if (CoreAbility.hasAbility(e.getPlayer(), LightningBolts.class)) {
                    CoreAbility.getAbility(e.getPlayer(), LightningBolts.class).setHasClicked();
                }
                break;

            case "Torrent":
                if (CoreAbility.hasAbility(e.getPlayer(), Torrent.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Torrent.class).setHasClicked();
                } else {
                    new Torrent(player);
                }
                break;
            case "Gimbal":
                if (CoreAbility.hasAbility(e.getPlayer(), Gimbal.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Gimbal.class).setHasClicked();
                } else {
                    new Gimbal(player);
                }
                break;
            case "WaterSpout":
                if (CoreAbility.hasAbility(e.getPlayer(), WaterSpout.class)) {
                    CoreAbility.getAbility(e.getPlayer(), WaterSpout.class).setHasClicked();
                } else {
                    new WaterSpout(player);
                }
                break;
            case "FrostTsunami":
                if (CoreAbility.hasAbility(e.getPlayer(), FrostTsunami.class)) {
                    CoreAbility.getAbility(e.getPlayer(), FrostTsunami.class).setHasClicked();
                }
                break;
        }

    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if (player == null) {
            return;
        }

        if (player.isSneaking()){
            return;
        }
        SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayer(player);
        if (sPlayer == null) {
            return;
        }
        String ability = sPlayer.getHeldAbility();

        if (ability == null) {
            return;
        }

        Serenity.getComboManager().addRecentlyUsed(player, ability, ClickType.SHIFT_LEFT);

        switch (ability) {
            case "RockKick":
                new RockKick(player);
                break;
            case "TerraLine":
                new TerraLine(player);
                break;
            case "StoneShred":
                new StoneShred(player);
                break;
            case "Catapult":
                new Catapult(player);
                break;
            case "EarthQuake":
                new EarthQuake(player);
                break;
            case "TectonicWave":
                new TectonicWave(player);
                break;
            case "RockRing":
                if (CoreAbility.hasAbility(e.getPlayer(), RockRing.class)) {
                    CoreAbility.getAbility(e.getPlayer(), RockRing.class).setHasSourced();
                }
                break;
            case "Singularity":
                new Singularity(player);
                break;
            case "AbyssalFall":
                new AbyssalFall(player);
                break;
            case "PhantomBreath":
                new PhantomBreath(player);
                break;
            case "CruelSun":
                new CruelSun(player);
                break;
            case "SolarFlare":
                new SolarFlare(player);
                break;
            case "FlamingRays":
                new FlamingRays(player);
                break;
            case "SolarBeam":
                new SolarBeam(player);
                break;
            case "SunBurst":
                new SunBurst(player);
                break;
            case "MeltingGlare":
                new MeltingGlare(player);
                break;
            case "SolarBarrage":
                new SolarBarrage(player);
                break;
            case "SkyBlast":
                new SkyBlast(player);
                break;
            case "SkyRipper":
                new SkyRipper(player);
                break;
            case "CloudStep":
                if (CoreAbility.hasAbility(e.getPlayer(), CloudStep.class)) {
                    CoreAbility.getAbility(e.getPlayer(), CloudStep.class).setHasShifted();
                }
                break;
            case "Shocker":
                new Shocker(player);
                break;
            case "ThunderStrike":
                new ThunderStrike(player);
                break;
            case "LightningBolts":
                if (CoreAbility.hasAbility(e.getPlayer(), LightningBolts.class)) {
                    CoreAbility.getAbility(e.getPlayer(), LightningBolts.class).setHasSneaked();
                } else {
                    new LightningBolts(player);
                }
                break;
            case "Torrent":
                if (CoreAbility.hasAbility(e.getPlayer(), Torrent.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Torrent.class).setHasSourced();
                }
                break;
            case "Gimbal":
                if (CoreAbility.hasAbility(e.getPlayer(), Gimbal.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Gimbal.class).setHasSourced();
                }
                break;
            case "FrostTsunami":
                new FrostTsunami(player);
                break;
        }
    }

    @EventHandler
    public void stopTempLiquidFlow(BlockFromToEvent event) {
        if (event.getBlock().isLiquid() && TempBlock.isTempBlock(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void noFallDamage(EntityDamageEvent event){
        if (event.getEntity() instanceof Player player){
            SerenityPlayer sPlayer = getSerenityPlayer(player);
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL){

                if (sPlayer.getArchetype() == Archetype.EARTH && sPlayer.getHeldAbility().equals("EarthQuake")) {
                    if (!CoreAbility.hasAbility(player, EarthQuake.class))
                    {
                        EarthQuake earthQuake = new EarthQuake(player);
                        earthQuake.setCharged();
                    }
                }
                event.setCancelled(true);
            } else if (event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
                if (sPlayer.getArchetype() == Archetype.SKY) {
                    event.setCancelled(true);
                }

            }
        }
    }




//    @EventHandler
//    public void onPlayerTryToRide(PlayerInteractEntityEvent event){
//        if (event.getRightClicked() instanceof LivingEntity rideable && event.getPlayer().getVehicle() ==null ){
//            Player player = event.getPlayer();
//            rideable.addPassenger(player);
//            //rideable.setAI(false);
//        }
//    }
//
//    @EventHandler
//    public void onEntityDismount(EntityDismountEvent event){
//        if (event.getEntity() instanceof Player player && player.isSneaking()) {
//            event.setCancelled(true);
//        } else{
//            if (event.getEntity() instanceof LivingEntity rideable){
//                //rideable.setAI(true);
//            }
//        }
//    }

//    @EventHandler
//    public void onPlayerPortal(PlayerPortalEvent event){
//        Player player = event.getPlayer();
//
//        SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayer(player);
//        if (sPlayer == null) {
//            return;
//        }
//        String ability = sPlayer.getHeldAbility();
//
//        if (ability == null) {
//            return;
//        }
//
//        if (CoreAbility.hasAbility(player, ChaoticVoid.class)) {
//            CoreAbility.getAbility(player, ChaoticVoid.class).handleTeleport();
//            event.setCancelled(true);
//        }
//    }

//    @EventHandler
//    public void onPlayerTeleport(PlayerTeleportEndGatewayEvent event){
//        Player player = event.getPlayer();
//
//        if (player.getWorld().equals(World.Environment.NORMAL)) {
//            event.setCancelled(true);
//        }
}
//        SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayer(player);
//        if (sPlayer == null) {
//            return;
//        }
//        String ability = sPlayer.getHeldAbility();
//
//        if (ability == null) {
//            return;
//        }

//        if (CoreAbility.hasAbility(player, ChaoticVoid.class)) {
//            CoreAbility.getAbility(player, ChaoticVoid.class).handleTeleport();
//            event.setCancelled(true);
//        }
//    }


























