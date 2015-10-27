/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.match3;

import net.java.html.json.Model;
import net.java.html.json.Property;

/**
 *
 * @author antonepple
 */
@Model(className = "Tile", properties = {
    @Property(name = "type", type = int.class),
    @Property(name = "swap", type = String.class),
    @Property(name = "drop", type = String.class),
    @Property(name = "explode", type = boolean.class)})
class TileVM {
    
}
