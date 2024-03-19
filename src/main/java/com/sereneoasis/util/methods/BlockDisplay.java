package com.sereneoasis.util.methods;

import org.bukkit.util.Transformation;
import org.joml.Vector3f;

public class BlockDisplay {

    public static void rotateBlockDisplayProperlyDegs(org.bukkit.entity.BlockDisplay blockDisplay, double size,
                                                      float xDegs, float yDegs, float zDegs ) {
        Transformation oldDisplayTransformation = blockDisplay.getTransformation();
        Vector3f oldDisplayTranslation = new Vector3f(oldDisplayTransformation.getTranslation());
        oldDisplayTransformation.getTranslation().set(size/2, 0 ,size/2 );
        oldDisplayTransformation.getLeftRotation().rotateXYZ((float) Math.toRadians(xDegs), (float) Math.toRadians(yDegs), (float) Math.toRadians(zDegs));
        oldDisplayTransformation.getTranslation().set(oldDisplayTranslation);
        blockDisplay.setTransformation(oldDisplayTransformation);
    }
}
