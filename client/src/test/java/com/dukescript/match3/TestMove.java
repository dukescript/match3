/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.match3;

import java.io.FileInputStream;
import java.io.IOException;
import net.java.html.BrwsrCtx;
import net.java.html.json.Models;
import org.junit.Assert;
import org.junit.Test;
import org.netbeans.html.context.spi.Contexts;

/**
 *
 * @author antonepple
 */
public class TestMove {

    public TestMove() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
//    @Test
//    public void hello() throws IOException {
//        Level level = Models.parse(parsingContext(), Level.class,
//                 new FileInputStream("/Users/antonepple/NetBeansProjects/match3/client/src/test/java/com/dukescript/match3/newfile"));
//         }

    private static BrwsrCtx parsingContext() {
        Contexts.Builder builder = Contexts.newBuilder("tyrus");
        Contexts.fillInByProviders(Main.class, builder);
        return builder.build();
    }
}
