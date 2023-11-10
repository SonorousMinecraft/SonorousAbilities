package com.sereneoasis.archetypes.ocean;


import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.Archetype;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;



public class GlacierBreath extends CoreAbility {

    private final String name = "GlacierBreath";

    private int firehelixes;


    //set variables
    private long chargestarttime;
    private long breathstarttime;
    private Location origin;
    private Location loc;
    private Vector dir;
    private Boolean charged;
    private Boolean started;

    private boolean isBlackIce = false;
    private double arbitraryangleincrement;
    private double angle;
    private double angledifference;
    private BossBar barduration;


    private AbilityStatus abilityStatus = AbilityStatus.CHARGING;


    public GlacierBreath(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name)) {
            return;
        }

        setFields();
        start();
    }

    public void setFields() {

        //Config

        this.firehelixes = 6;

        //Set variables
        this.chargestarttime = System.currentTimeMillis();
        this.charged = false;
        this.started = false;
        if (firehelixes!=0) {
            this.angledifference = 360/(firehelixes+1);
        }
        this.arbitraryangleincrement = 0;

        barduration = Bukkit.getServer().createBossBar(name,BarColor.BLUE, BarStyle.SEGMENTED_10);

    }


    @Override
    public String getName() {
        return name;
    }


    @Override
    public void progress() {
        if (player.isDead() || !player.isOnline()){
            this.remove();
        }

        Long timecharged = System.currentTimeMillis() - chargestarttime;

        if (timecharged > chargeTime) {
            charged = true;
            abilityStatus = AbilityStatus.CHARGED;
        }

        if (isBlackIce && timecharged < chargeTime && !player.isSneaking())
        {
            this.remove();
        }

        if (!isBlackIce) {
            if (!player.isSneaking())
            {
                this.remove();
            }
        }

        if (started) {

            if (!isBlackIce) {
                Long timeelapsed = System.currentTimeMillis() - breathstarttime;
                Double progress = 1 - (double) timeelapsed / (double) duration;
                if (progress < 0) {
                    barduration.setProgress(0);
                } else {
                    barduration.setProgress(progress);
                }

                if (timeelapsed > duration) {
                    this.remove();
                }
                this.origin = player.getEyeLocation();
                this.dir = origin.getDirection().normalize();
            }
            if (isBlackIce){
                this.dir = new Vector(0,-1,0);
                this.origin = player.getLocation();
            }

            arbitraryangleincrement+=5;

            for (double d = 0; d < range; d+=0.5) {

                angle = arbitraryangleincrement + 10*d;
                this.loc = origin.clone().add(dir.clone().multiply(d));

//                if (d < 1) {
//                    Vector smokeorthagonal = GeneralMethods.getOrthogonalVector(dir, 90,1);
//                    for (double smokeangle = 0; smokeangle < 360; smokeangle +=20) {
//                        Location smokeloc = loc.clone().add(dir.clone().multiply(2)).add(smokeorthagonal.clone().multiply(d+0.5).rotateAroundAxis(dir, Math.toRadians(smokeangle)));
//                        //ParticleEffect.SPELL_MOB_AMBIENT.display(smokeloc, 0, 244/255, 243/255, 239/255, 1);
//                        ParticleEffect.SMOKE_NORMAL.display(smokeloc, 1, 0.1, 0.1, 0.1);
//                    }
//                }

                for (int i = 0; i < firehelixes; i++) {
                    Vector orthagonal = Vectors.getOrthogonalVector(dir, 90, Math.log(d+2) * Math.log(d+2));
                    Location helixloc = loc.clone().add(dir.clone().multiply(2)).add(orthagonal.clone().rotateAroundAxis(dir, Math.toRadians(angle + angledifference*i)));
                    //Location helixloc = loc.clone().add(dir.clone()).add(new Vector(0,Math.cos(angle + angledifference*i) + Math.log(distance+2) * Math.log(distance+2) ,0).rotateAroundAxis(dir, Math.toRadians(angle + angledifference*i)));

                    if (!Vectors.isObstructed(loc, helixloc)){
                        //Particles.spawnColoredParticle(loc, 1, 0.1, 1, ArchetypeDataManager.getArchetypeData(Archetype.OCEAN).getColor());
                        new TempDisplayBlock(helixloc, DisplayBlock.ICE, 100, 0.5);
                    }

					else {
                            Block topBlock = helixloc.clone().add(0,1,0).getBlock();
							if (topBlock.getType().isAir()){
								if (!TempBlock.isTempBlock(topBlock) && helixloc.getBlock().getType() == Material.WATER) {
                                    new TempBlock(helixloc.getBlock(), DisplayBlock.ICE, 2000, true);
								}
							}
					}

                }


                Particles.spawnColoredParticle(loc.clone().add(dir.clone().multiply(2)), 1, d/3, 1, ArchetypeDataManager.getArchetypeData(Archetype.OCEAN).getColor());

                if (Entities.getAffected(loc, d, player) != null) {
                    DamageHandler.damageEntity(Entities.getAffected(loc, d, player), player, this, damage);
                }
            }
        }
    }

    public AbilityStatus getAbilityStatus() {
        return abilityStatus;
    }

    public void setBlackIce()
    {
        barduration.removeAll();
        this.isBlackIce = true;
        started = true;
    }

    public void onClick() {
        if (!isBlackIce && charged && !started) {
            started = true;
            breathstarttime = System.currentTimeMillis();
            this.barduration.addPlayer(player);
            abilityStatus = AbilityStatus.SHOT;
        }

    }



    @Override
    public void remove() {
        super.remove();
        barduration.removeAll();
        if (this.started) {
            sPlayer.addCooldown(name, cooldown);
        }

    }

    @Override
    public Player getPlayer() {
        return player;
    }


}














