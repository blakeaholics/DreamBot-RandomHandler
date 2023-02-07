package com.holic.randomhandler;

import org.dreambot.api.Client;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.randoms.RandomSolver;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;

import java.awt.*;

/**
 * DrunkenDwarfSolver - solves Drunken Dwarf
 *
 * @author holic
 * @url https://github.com/blakeaholics/DreamBot-RandomHandler
 */
public class DrunkenDwarfSolver extends RandomSolver {

    public DrunkenDwarfSolver() {
        super("DrunkenDwarfSolver");
    }

    @Override
    public boolean shouldExecute() {
        NPC dwarf = NPCs.closest(322, 2429, 4305);//Just need to click ok
        dwarf = (dwarf == null ? NPCs.closest("Drunken Dwarf") : dwarf);
        return (dwarf != null && dwarf.getInteractingCharacter() != null && dwarf.getInteractingCharacter().equals(Players.getLocal()));
    }

    @Override
    public void onPaint(Graphics2D graphics) {
        Rectangle screen = Client.getCanvas().getBounds();
        graphics.setFont(new Font("default", Font.BOLD, 16));
        graphics.setColor(new Color(74, 255, 129, 161));
        graphics.fillRect(0, 0, screen.width, screen.height);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Drinking with our Homie", 301, 501);
        graphics.setColor(Color.WHITE);
        graphics.drawString("Drinking with our Homie", 300, 500);
    }

    @Override
    public int onLoop() {
        NPC dwarf = NPCs.closest(322, 2429, 4305);//Just need to click ok
        dwarf = (dwarf == null ? NPCs.closest("Drunken Dwarf") : dwarf);
        if (dwarf != null) {
            if (Calculations.random(2) == 1) {
                int ran = Calculations.random(1, 6);
                RandomHandler.log("Trying not to seem desperate for " + ran + " seconds...", "DrunkenDwarfSolver");
                Sleep.sleep(ran * 1000L);
            }
            Sleep.sleep(350, 850);
            RandomHandler.log("Oh, snap! It's beer o'clock!", "DrunkenDwarfSolver");
            if (dwarf.interact()) {
                Sleep.sleep(450, 1850);
                Sleep.sleepUntil(Dialogues::inDialogue, 10000);
                RandomHandler.powerThroughDialogue();

                Sleep.sleep(350, 850);
                return -1;
            }
        }

        return 1;
    }
}