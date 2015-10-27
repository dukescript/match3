/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.match3.js;

import net.java.html.js.JavaScriptBody;
import net.java.html.js.JavaScriptResource;

public class Jqueryui {

    public static void init() {
        Jquery.init();
        Impl.init_impl();
    }

    @JavaScriptResource(value = "jquery-ui.min.js")
    private static final class Impl {

        @JavaScriptBody(args = {}, body = "")
        static native void init_impl();

    }

    @JavaScriptResource(value = "jquery-1.7.2.min.js")
    public static class Jquery {

        @JavaScriptBody(args = {}, body = "")
        public static native void init();
    }
}
