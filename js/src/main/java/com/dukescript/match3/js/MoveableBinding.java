/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.match3.js;

import net.java.html.js.JavaScriptBody;
import net.java.html.js.JavaScriptResource;

@JavaScriptResource(value = "ko-moveable-binding.js")
public class MoveableBinding {
    
    @JavaScriptBody(args = {  }, body = 
            "")
    public static native void init();
}
