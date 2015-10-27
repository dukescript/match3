/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.match3;

import java.util.List;
import net.java.html.json.ComputedProperty;
import net.java.html.json.Model;
import net.java.html.json.ModelOperation;
import net.java.html.json.Property;

/**
 *
 * @author antonepple
 */
@Model(className = "Goal", properties = {
    @Property(name = "type1", type = int.class),
    @Property(name = "type2", type = int.class),
    @Property(name = "type3", type = int.class),
    @Property(name = "num1", type = int.class),
    @Property(name = "num2", type = int.class),
    @Property(name = "num3", type = int.class),
    @Property(name = "deletedTiles", type = int.class, array = true)})
class GoalVM {
    
    @ComputedProperty
    public static boolean solved(List<Integer> deletedTiles, int type1, int type2, int type3, int num1, int num2, int num3) {
        return deletedTiles.get(type1) >= num1 && deletedTiles.get(type2) >= num2 && deletedTiles.get(type3) >= num3;
    }

    @ModelOperation
    public static void delete(Goal goal, int type) {
        List<Integer> deletedTiles = goal.getDeletedTiles();
        deletedTiles.set(type, deletedTiles.get(type) + 1);
    }

    @ComputedProperty
    public static int toDelete1(List<Integer> deletedTiles, int type1, int num1) {
        return num1 - deletedTiles.get(type1) < 0 ? 0 : num1 - deletedTiles.get(type1);
    }

    @ComputedProperty
    public static int toDelete2(List<Integer> deletedTiles, int type2, int num2) {
        return num2 - deletedTiles.get(type2) < 0 ? 0 : num2 - deletedTiles.get(type2);
    }

    @ComputedProperty
    public static int toDelete3(List<Integer> deletedTiles, int type3, int num3) {
        return num3 - deletedTiles.get(type3) < 0 ? 0 : num3 - deletedTiles.get(type3);
    }
    
}
