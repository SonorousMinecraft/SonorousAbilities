package com.sereneoasis.listeners;

import com.sereneoasis.*;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.archetypes.ocean.*;
import com.sereneoasis.archetypes.sun.CruelSun;
import com.sereneoasis.displays.SerenityBoard;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Bukkit;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.UUID;

/**
 * @author Sakrajin
 * Main listener for all serenity events
 */
public class SerenityListener implements Listener {


    public static void initialiseAttributePlayer(Player player, SerenityPlayer serenityPlayer)
    {
        removeAttributePlayer(player, serenityPlayer);
        ArchetypeDataManager.getArchetypeData(serenityPlayer.getArchetype()).getArchetypeAttributes().forEach((attribute, value) ->
        {
            AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID(),"Serenity." + attribute.toString(),value,
                    AttributeModifier.Operation.ADD_NUMBER);

            player.getAttribute(attribute).addModifier(attributeModifier);
        });
    }

    public static void removeAttributePlayer(Player player, SerenityPlayer serenityPlayer)
    {
        ArchetypeDataManager.getArchetypeData(serenityPlayer.getArchetype()).getArchetypeAttributes().forEach((attribute, value) ->
        {
            player.getAttribute(attribute).getModifiers().forEach(attributeModifier ->  {player.getAttribute(attribute).removeModifier(attributeModifier);});
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();
        SerenityPlayer.loadPlayer(player.getUniqueId(), player);

        Bukkit.getScheduler().runTaskLater(Serenity.getPlugin(), () -> {
            SerenityBoard board = SerenityBoard.createScore(player);
            SerenityPlayer serenityPlayer = SerenityPlayer.getSerenityPlayer(player);
            board.setSlot(10, serenityPlayer.getArchetype().toString());
            for (int i : serenityPlayer.getAbilities().keySet()) {
                board.setAbilitySlot(i, serenityPlayer.getAbilities().get(i));
            }
            initialiseAttributePlayer(player, serenityPlayer);

        }, 150L);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        SerenityBoard.removeScore(player);

        SerenityPlayer serenityPlayer = SerenityPlayer.getSerenityPlayer(player);

        Serenity.getComboManager().removePlayer(player);

        SerenityPlayer.upsertPlayer(serenityPlayer);

//        Serenity.getRepository().getAsync(player.getUniqueId())
//                .thenAsync((playerData) ->
//                {
//                    playerData.setName(player.getName());
//
//                    playerData.setAbilities(serenityPlayer.getAbilities());
//
//                    playerData.setArchetype(serenityPlayer.getArchetype().toString());
//                    Serenity.getRepository().upsertAsync(playerData);
//                });
//        PlayerData oldPlayerData = Serenity.getRepository().get(uuid);
//        oldPlayerData.setAbilities(serenityPlayer.getAbilities());
//        oldPlayerData.setArchetype(serenityPlayer.getArchetype().toString());
//        Serenity.getRepository().update(oldPlayerData);



        removeAttributePlayer(player, serenityPlayer);



    }

    @EventHandler
    public void onSwing(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        if (player == null)
        {
            return;
        }
        SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayer(player);
        if (sPlayer == null)
        {
            return;
        }
        String ability = sPlayer.getHeldAbility();

        if (ability != null)
        {
            Serenity.getComboManager().addRecentlyUsed(player, ability, ClickType.LEFT);
        }
        switch(ability)
        {
            case "Torrent":
                if (CoreAbility.hasAbility(e.getPlayer(), Torrent.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Torrent.class).setHasClicked();
                }
                else{
                    new Torrent(player);
                }
                break;
            case "Gimbal":
                if (CoreAbility.hasAbility(e.getPlayer(), Gimbal.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Gimbal.class).setHasClicked();
                }
                else{
                    new Gimbal(player);
                }
                break;
            case "Spikes":
                if (CoreAbility.hasAbility(e.getPlayer(), Spikes.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Spikes.class).setHasClicked();
                }
                break;
            case "FrostBite":
                if (CoreAbility.hasAbility(e.getPlayer(), FrostBite.class)) {
                    CoreAbility.getAbility(e.getPlayer(), FrostBite.class).setSourceBlock2();
                    CoreAbility.getAbility(e.getPlayer(), FrostBite.class).setHasClicked();
                }
                else{
                    new FrostBite(player);
                }
                break;
            case "GlacierBreath":
                    if (CoreAbility.hasAbility(player, GlacierBreath.class)){
                        CoreAbility.getAbility(player, GlacierBreath.class).onClick();
                    }
                break;

        }

    }
    @EventHandler
    public void onShift(PlayerToggleSneakEvent e)
    {
        Player player = e.getPlayer();
        if (player == null)
        {
            return;
        }
        SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayer(player);
        if (sPlayer == null)
        {
            return;
        }
        String ability = sPlayer.getHeldAbility();
        if (ability != null)
        {
            Serenity.getComboManager().addRecentlyUsed(player, ability, ClickType.SHIFT_LEFT);
        }
        switch(ability)
        {
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
            case "Spikes":
                new Spikes(player);
                break;
            case "GlacierBreath":
                new GlacierBreath(player);
                break;
            case "CruelSun":
                new CruelSun(player);
                break;

        }
    }

    @EventHandler
    public void stopTempLiquidFlow(BlockFromToEvent event)
    {
        if (event.getBlock().isLiquid() && TempBlock.isTempBlock(event.getBlock()))
        {
            event.setCancelled(true);
        }
    }
}
