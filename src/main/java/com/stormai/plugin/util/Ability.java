package com.stormai.plugin.util;

import org.bukkit.Material;

public class Ability {

    private final String name;
    private final Material material;
    private final int cost;

    public Ability(String name, Material material, int cost) {
        this.name = name;
        this.material = material;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public int getCost() {
        return cost;
    }
}