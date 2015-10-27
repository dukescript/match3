/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.match3.js;

import net.java.html.js.JavaScriptBody;
import net.java.html.js.JavaScriptResource;

@JavaScriptResource(value = "ko-swap-binding.js")
public class SwapBinding {
    
    public static void init() {
        Jqueryui.init();
        init_impl();
    }
    
    @JavaScriptBody(args = {  },body = "")
    private static native void init_impl();
}
