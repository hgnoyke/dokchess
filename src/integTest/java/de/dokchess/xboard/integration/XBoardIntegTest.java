/*
 * Copyright (c) 2010-2015 Stefan Zoerner
 *
 * This file is part of DokChess.
 *
 * DokChess is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DokChess is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DokChess.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.dokchess.xboard.integration;

import de.dokchess.engine.DefaultEngine;
import de.dokchess.engine.Engine;
import de.dokchess.regeln.DefaultSpielregeln;
import de.dokchess.regeln.Spielregeln;
import de.dokchess.xboard.XBoard;
import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Starte das XBoard-Protokoll inkl. echter Engine. Mache einen Zug und warte
 * die Antwort ab.
 */
public class XBoardIntegTest {

    @Test
    public void anspielen() throws InterruptedException {

        String eingegeben = "xboard\nprotover 2\ne2e4\n";
        final StringWriter ausgabe = new StringWriter();

        XBoard xBoard = new XBoard();
        xBoard.setAusgabe(ausgabe);
        Spielregeln spielregeln = new DefaultSpielregeln();
        Engine engine = new DefaultEngine(spielregeln);

        xBoard.setEngine(engine);
        xBoard.setSpielregeln(spielregeln);

        Reader eingabe = new StringReader(eingegeben);
        xBoard.setEingabe(eingabe);

        xBoard.spielen();

        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                String text;
                synchronized (ausgabe) {
                    text = ausgabe.toString();
                }
                if (text.contains("move ")) {
                    executor.shutdown();
                }
            }
        }, 3, 1, TimeUnit.SECONDS);
        executor.awaitTermination(1, TimeUnit.MINUTES);

        String text;
        synchronized (ausgabe) {
            text = ausgabe.toString();
        }

        Assert.assertTrue(text.contains("move "));
    }
}