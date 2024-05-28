//package com.sereneoasis.archetypes.earth;
//
//import com.sereneoasis.ability.superclasses.CoreAbility;
//import com.sereneoasis.util.methods.Scheduler;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.util.Vector;
//
//import java.lang.ref.WeakReference;
//import java.util.WeakHashMap;
//import java.util.concurrent.TimeUnit;
//
//public class EarthFist extends CoreAbility {
//
//    private Location loc, origin;
//
//    private Vector dir;
//
//    public EarthFist(Player player) {
//        super(player, "EarthFist");
//
//        String val = "shit";
//        WeakHashMap<String, Player> test = new WeakHashMap<>();
//        test.put(val, player);
//        System.out.println(test);
//        val = null;
//        System.out.println(test);
//        System.gc();
//        Scheduler.performTaskLater(20, () ->         System.out.println(test)
//        );
//
//
//        if (shouldStart()){
////            this.loc = player.getEyeLocation();
////            this.origin = loc.clone();
////            this.dir = loc.getDirection();
////            start();
//        }
//    }
//
//    @Override
//    public void progress() throws ReflectiveOperationException {
//        if (loc.distanceSquared(origin) > range*range){
//            this.remove();
//            sPlayer.addCooldown("EarthFist", cooldown);
//        }
//        loc.add(dir.clone().multiply(speed));
//    }
//
//
//
//    @Override
//    public String getName() {
//        return "EarthFist";
//    }
//}
