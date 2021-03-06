/*
 * Copyright (c) 2010-2014 Stefan Zoerner
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
package de.dokchess.regeln;

import de.dokchess.allgemein.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static de.dokchess.allgemein.Felder.*;

public class SpringerZuegeTest {

    private static final Figur SPRINGER_WEISS = new Figur(FigurenArt.SPRINGER,
            Farbe.WEISS);

    @Test
    public void springerRad() {
        Stellung weisserSpringerD5 = new Stellung("8/8/8/3N4/8/8/8/8 w - - 0 1");

        SpringerZuege springerZuege = new SpringerZuege();
        List<Zug> zuege = new ArrayList<Zug>();

        springerZuege.fuegeZugkandidatenHinzu(d5, weisserSpringerD5, zuege);

        Assert.assertEquals(8, zuege.size());
        for (Zug zug : zuege) {
            Assert.assertEquals(d5, zug.getVon());
        }
        Feld[] ziele = {c7, e7, b6, f6, b4, f4, c3, e3};
        for (Feld ziel : ziele) {
            Zug zuTesten = new Zug(SPRINGER_WEISS, d5, ziel);
            Assert.assertTrue(zuTesten.toString(), zuege.contains(zuTesten));
        }
    }

    @Test
    public void einzelnerSpringerInDerEcke() {

        {
            Stellung weisserSpringerA1 = new Stellung(
                    "8/8/8/8/8/8/8/N7 w - - 0 1");

            SpringerZuege springerZuege = new SpringerZuege();
            List<Zug> zuege = new ArrayList<Zug>();

            springerZuege.fuegeZugkandidatenHinzu(a1, weisserSpringerA1, zuege);

            Assert.assertEquals(2, zuege.size());

            for (Zug zug : zuege) {
                Assert.assertEquals(a1, zug.getVon());
            }
            Feld[] ziele = {b3, c2};
            for (Feld ziel : ziele) {
                Zug zuTesten = new Zug(SPRINGER_WEISS, a1, ziel);
                Assert.assertTrue(zuege.contains(zuTesten));
            }
        }

        {
            Stellung weisserSpringerH8 = new Stellung(
                    "7N/8/8/8/8/8/8/8 w - - 0 1");

            SpringerZuege springerZuege = new SpringerZuege();
            List<Zug> zuege = new ArrayList<Zug>();

            springerZuege.fuegeZugkandidatenHinzu(h8, weisserSpringerH8, zuege);
            Assert.assertEquals(2, zuege.size());

            for (Zug zug : zuege) {
                Assert.assertEquals(h8, zug.getVon());
            }
            Feld[] ziele = {g6, f7};
            for (Feld ziel : ziele) {
                Zug zuTesten = new Zug(SPRINGER_WEISS, h8, ziel);
                Assert.assertTrue(zuege.contains(zuTesten));
            }
        }
    }

    @Test
    public void einzelnerSpringerSchlaegt() {
        Stellung weisserSpringKannZweiSchlagen = new Stellung(
                "8/8/5B2/3N4/1n6/4r3/8/8 w - - 0 1");

        SpringerZuege springerZuege = new SpringerZuege();
        List<Zug> zuege = new ArrayList<Zug>();

        springerZuege.fuegeZugkandidatenHinzu(d5,
                weisserSpringKannZweiSchlagen, zuege);

        Assert.assertEquals(7, zuege.size());

        for (Zug zug : zuege) {
            Assert.assertEquals(d5, zug.getVon());
        }

        Zug schwarzenSpringerSchlagen = new Zug(SPRINGER_WEISS, d5, b4, true);
        Zug schwarzenTurmSchlagen = new Zug(SPRINGER_WEISS, d5, e3, true);

        Assert.assertTrue(schwarzenSpringerSchlagen.toString(),
                zuege.contains(schwarzenSpringerSchlagen));
        Assert.assertTrue(schwarzenTurmSchlagen.toString(),
                zuege.contains(schwarzenTurmSchlagen));
    }

}
