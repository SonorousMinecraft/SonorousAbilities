package com.sereneoasis.util.methods.collections;

import java.util.Random;
import java.util.Set;

public class CollectionUtils {

    public static <E> E getRandomSetElement(Set<E> set) {
        return set.stream().skip(new Random().nextInt(set.size())).findFirst().orElse(null);
    }
}
