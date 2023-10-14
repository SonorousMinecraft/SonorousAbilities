package com.sereneoasis.listeners;

import com.sereneoasis.*;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.archetypes.sun.CruelSun;
import com.sereneoasis.displays.SerenityBoard;
import com.sereneoasis.archetypes.ocean.Gimbal;
import com.sereneoasis.archetypes.ocean.Spikes;
import com.sereneoasis.archetypes.ocean.Torrent;
import com.sereneoasis.storage.PlayerData;
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
        SerenityPlayer.loadAsync(player.getUniqueId(), player);

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
        UUID uuid = player.getUniqueId();

        SerenityBoard.removeScore(player);

        SerenityPlayer serenityPlayer = SerenityPlayer.getSerenityPlayer(player);

        Serenity.getComboManager().removePlayer(player);


//        PlayerData oldPlayerData = Serenity.getRepository().get(uuid);
//        oldPlayerData.setAbilities(serenityPlayer.getAbilities());
//        oldPlayerData.setArchetype(serenityPlayer.getArchetype().toString());
//        Serenity.getRepository().update(oldPlayerData);

        PlayerData playerData = new PlayerData();
        playerData.setKey(uuid);
        playerData.setName(player.getName());

        playerData.setAbilities(serenityPlayer.getAbilities());

        playerData.setArchetype(serenityPlayer.getArchetype().toString());

        Serenity.getRepository().updateAsync(playerData);

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
                break;
            case "Gimbal":
                if (CoreAbility.hasAbility(e.getPlayer(), Gimbal.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Gimbal.class).setHasClicked();
                }
                break;
            case "Spikes":
                if (CoreAbility.hasAbility(e.getPlayer(), Gimbal.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Gimbal.class).setHasClicked();
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
                new Torrent(player);
                break;
            case "Gimbal":
                new Gimbal(player);
                break;
            case "Spikes":
                new Spikes(player);
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
