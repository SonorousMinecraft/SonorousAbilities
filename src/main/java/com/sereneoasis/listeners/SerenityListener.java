package com.sereneoasis.listeners;

import com.sereneoasis.Serenity;
import com.sereneoasis.SerenityPlayer;
import com.sereneoasis.ability.BendingManager;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.Archetype;

import com.sereneoasis.archetypes.earth.RockKick;
import com.sereneoasis.displays.SerenityBoard;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.spigotmc.event.entity.EntityDismountEvent;

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


        if (ability != null) {
            Serenity.getComboManager().addRecentlyUsed(player, ability, ClickType.LEFT);
        }

        BendingManager.getInstance().handleRedirections(player, ClickType.LEFT);
        switch (ability) {
            case "RockKick":
                if (CoreAbility.hasAbility(e.getPlayer(), RockKick.class)) {
                    CoreAbility.getAbility(e.getPlayer(), RockKick.class).setHasClicked();
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
        SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayer(player);
        if (sPlayer == null) {
            return;
        }
        String ability = sPlayer.getHeldAbility();

        if (ability != null) {
            Serenity.getComboManager().addRecentlyUsed(player, ability, ClickType.SHIFT_LEFT);
        }


        switch (ability) {
            case "RockKick":
                new RockKick(player);
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
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL){
                event.setCancelled(true);
            }
        }
    }




    @EventHandler
    public void onPlayerTryToRide(PlayerInteractEntityEvent event){
        if (event.getRightClicked() instanceof LivingEntity rideable && event.getPlayer().getVehicle() ==null ){
            Player player = event.getPlayer();
            rideable.addPassenger(player);
            //rideable.setAI(false);
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event){
        if (event.getEntity() instanceof Player player && player.isSneaking()) {
            event.setCancelled(true);
        } else{
            if (event.getEntity() instanceof LivingEntity rideable){
                //rideable.setAI(true);
            }
        }
    }
}
























