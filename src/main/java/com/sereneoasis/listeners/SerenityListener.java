package com.sereneoasis.listeners;

import com.sereneoasis.Serenity;
import com.sereneoasis.SerenityPlayer;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.Archetype;
import com.sereneoasis.archetypes.earth.*;
import com.sereneoasis.archetypes.ocean.*;
import com.sereneoasis.archetypes.sky.*;
import com.sereneoasis.archetypes.sun.*;
import com.sereneoasis.archetypes.war.*;
import com.sereneoasis.displays.SerenityBoard;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

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


    }

    @EventHandler
    public void onSwing(PlayerInteractEvent e) throws ReflectiveOperationException {
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
            if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                Serenity.getComboManager().addRecentlyUsed(player, ability, ClickType.LEFT);
            }
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Serenity.getComboManager().addRecentlyUsed(player, ability, ClickType.RIGHT);
            }
        }
        switch (ability) {
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
            case "Iceberg":
                if (CoreAbility.hasAbility(e.getPlayer(), Iceberg.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Iceberg.class).setHasClicked();
                }
                break;
            case "FrostBite":
                if (CoreAbility.hasAbility(e.getPlayer(), FrostBite.class)) {
                    CoreAbility.getAbility(e.getPlayer(), FrostBite.class).setSourceBlock2();
                    CoreAbility.getAbility(e.getPlayer(), FrostBite.class).setHasClicked();
                } else {
                    new FrostBite(player);
                }
                break;
            case "GlacierBreath":
                if (CoreAbility.hasAbility(player, GlacierBreath.class)) {
                    CoreAbility.getAbility(player, GlacierBreath.class).onClick();
                }
                break;
            case "Blizzard":
                if (CoreAbility.hasAbility(e.getPlayer(), Blizzard.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Blizzard.class).setHasClicked();
                }
                break;
            case "Tsunami":
                new Tsunami(player);
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
                new Sunrise(player);
                break;
            case "SkyBlast":
                if (CoreAbility.hasAbility(e.getPlayer(), SkyBlast.class)) {
                    CoreAbility.getAbility(e.getPlayer(), SkyBlast.class).setHasClicked();
                }
                break;
            case "Nimbus":
                new Nimbus(player);
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
                new CloudStep(player);
                break;
            case "HeavenSlash":
                new HeavenSlash(player);
                break;
            case "ThunderStrike":
                if (CoreAbility.hasAbility(e.getPlayer(), ThunderStrike.class)) {
                    CoreAbility.getAbility(e.getPlayer(), ThunderStrike.class).setHasClicked();
                }
                break;
            case "Tether":
                if (CoreAbility.hasAbility(e.getPlayer(), Tether.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Tether.class).setHasClicked();
                } else {
                    new Tether(player);
                }
                break;
            case "Jab":
                new Jab(player);
                break;

            case "Rocket":
                if (CoreAbility.hasAbility(e.getPlayer(), Rocket.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Rocket.class).setHasClicked();
                } else {
                    new Rocket(player);
                }
                break;
            case "Formless":
                if (CoreAbility.hasAbility(e.getPlayer(), Formless.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Formless.class).setHasClicked(e.getAction());
                }
                break;
            case "Katana":
                if (CoreAbility.hasAbility(e.getPlayer(), Katana.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Katana.class).setHasClicked();
                }
                break;
            case "Spear":
                new Spear(player);
                break;
            case "Grenades":
                if (CoreAbility.hasAbility(e.getPlayer(), Grenades.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Grenades.class).setHasClicked();
                } else {
                    new Grenades(player);
                }
                break;
            case "RockKick":
                if (CoreAbility.hasAbility(e.getPlayer(), RockKick.class)) {
                    CoreAbility.getAbility(e.getPlayer(), RockKick.class).setHasClicked();
                }
                break;
            case "Accretion":
                if (CoreAbility.hasAbility(e.getPlayer(), Accretion.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Accretion.class).setHasClicked();
                }
                break;
            case "TerraLine":
                if (CoreAbility.hasAbility(e.getPlayer(), TerraLine.class)) {
                    CoreAbility.getAbility(e.getPlayer(), TerraLine.class).setHasClicked();
                }
                break;
            case "Wall":
                new Wall(player);
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

        if (sPlayer.getArchetype().equals(Archetype.OCEAN) && player.getLocation().getBlock().getType() == Material.WATER) {
            player.setVelocity(player.getEyeLocation().getDirection().normalize());
        }
        if (ability != null) {
            Serenity.getComboManager().addRecentlyUsed(player, ability, ClickType.SHIFT_LEFT);
        }

        switch (ability) {
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
            case "Iceberg":
                new Iceberg(player);
                break;
            case "Blizzard":
                new Blizzard(player);
                break;
            case "GlacierBreath":
                new GlacierBreath(player);
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
            case "Formless":
                new Formless(player);
                break;
            case "Katana":
                new Katana(player);
                break;
            case "RockKick":
                new RockKick(player);
                break;
            case "Accretion":
                new Accretion(player);
                break;
            case "EarthQuake":
                new EarthQuake(player);
                break;
            case "Catapult":
                new Catapult(player);
                break;
            case "TerraLine":
                new TerraLine(player);
                break;
        }
    }

    @EventHandler
    public void stopTempLiquidFlow(BlockFromToEvent event) {
        if (event.getBlock().isLiquid() && TempBlock.isTempBlock(event.getBlock())) {
            event.setCancelled(true);
        }
    }
}
