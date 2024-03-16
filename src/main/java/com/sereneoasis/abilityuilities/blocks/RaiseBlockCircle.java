package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RaiseBlockCircle extends CoreAbility {

    private final String name;

    private double currentHeight = 0;
    private double height;
    private List<TempDisplayBlock> block;

    private boolean shouldFall;


    public RaiseBlockCircle(Player player, String name, double height, double currentRadius, double size, boolean shouldFall) {
        super(player, name);

        this.name = name;
        this.height = height;
        this.shouldFall = shouldFall;
        block = new ArrayList<>();
        Locations.getOutsideSphereLocs(player.getLocation(), currentRadius, size / 2 + 0.001).stream()
                .filter(l -> Blocks.getArchetypeBlocks(sPlayer).contains(l.getBlock().getType()))
                .filter(l -> Blocks.isTopBlock(l.getBlock()))
                .forEach(l -> {
                    //Bukkit.broadcastMessage(String.valueOf(Vectors.getDirectionBetweenLocations(b.getLocation(), player.getLocation()).setY(0).length()));
                    TempDisplayBlock tb = new TempDisplayBlock(l.setDirection(l.getBlock().getLocation().getDirection()).clone(), l.getBlock().getType(), 60000, size);
                    block.add(tb);
                });


//        Blocks.getBlocksAroundPoint(player.getLocation(), currentRadius).stream()
//                .filter(b -> b.getLocation().distance(player.getLocation()) > currentRadius/2)
//                .filter((b -> Blocks.getArchetypeBlocks(sPlayer).contains(b.getType())))
//                .filter(Blocks::isTopBlock)
//                .forEach(b -> {
//                    //Bukkit.broadcastMessage(String.valueOf(Vectors.getDirectionBetweenLocations(b.getLocation(), player.getLocation()).setY(0).length()));
//                    TempDisplayBlock tb = new TempDisplayBlock(b.getLocation(), b.getType(), 60000, size-0.001);
//                    block.add(tb);
//                });
        if (!block.isEmpty()) {
            abilityStatus = AbilityStatus.SOURCING;
            start();
        } else {
            abilityStatus = AbilityStatus.NO_SOURCE;
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (abilityStatus == AbilityStatus.SOURCING) {
            if (currentHeight < height) {
                for (TempDisplayBlock tb : block) {
                    tb.moveTo(tb.getBlockDisplay().getLocation().clone().add(0, 0.1, 0));
                }
                currentHeight += 0.1;
            } else {
                abilityStatus = AbilityStatus.SOURCED;
            }
        } else if (abilityStatus == AbilityStatus.SOURCED && shouldFall) {
            if (currentHeight > 0.1) {
                for (TempDisplayBlock tb : block) {
                    tb.moveTo(tb.getBlockDisplay().getLocation().clone().subtract(0, 0.1, 0));
                }
                currentHeight -= 0.1;
            } else {
                shouldFall = false;
                abilityStatus = AbilityStatus.COMPLETE;
            }

        }


    }

    @Override
    public void remove() {
        super.remove();
        for (TempDisplayBlock tb : block) {
            tb.revert();
        }
    }


    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return name;
    }
}
