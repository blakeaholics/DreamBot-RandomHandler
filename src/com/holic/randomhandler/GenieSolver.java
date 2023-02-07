package com.holic.randomhandler;

import org.dreambot.api.Client;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.randoms.RandomSolver;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;

import java.awt.*;

/**
 * GenieSolver - solves Genie, lamp solving will be added soon
 *
 * @author holic
 * @url https://github.com/blakeaholics/DreamBot-RandomHandler
 */
public class GenieSolver extends RandomSolver {

    private final Integer[] GENIE = {326, 327, 4738};
    private final String STRING_GENIE = "Genie";

    public GenieSolver() {
        super("IDreamOfGenieSolver");
    }

    @Override
    public boolean shouldExecute() {
        NPC genie = NPCs.closest(GENIE);
        genie = (genie == null ? NPCs.closest(STRING_GENIE) : genie);
        return (genie != null && genie.getInteractingCharacter() != null && genie.getInteractingCharacter().equals(Players.getLocal()));
    }

    @Override
    public void onPaint(Graphics2D graphics) {
        Rectangle screen = Client.getCanvas().getBounds();
        graphics.setFont(new Font("default", Font.BOLD, 16));
        graphics.setColor(new Color(74, 255, 129, 161));
        graphics.fillRect(0, 0, screen.width, screen.height);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Having our wishes granted", 301, 501);
        graphics.setColor(Color.WHITE);
        graphics.drawString("Having our wishes granted", 300, 500);
    }

    @Override
    public int onLoop() {
        int ran = Calculations.random(1, 6);
        NPC genie = NPCs.closest(GENIE);
        genie = (genie == null ? NPCs.closest(STRING_GENIE) : genie);
        if (genie != null) {
            RandomHandler.log("We've got a Genie!", "GenieSolver");
            Sleep.sleep(350, 850);
            if (Calculations.random(2) == 1) {
                RandomHandler.log("Delaying speaking to Genie by " + ran + " seconds", "GenieSolver");
                Sleep.sleep(ran * 1000L);
            }
            if (genie.interact()) {
                Sleep.sleep(1550, 3500);
                Sleep.sleepWhile(() -> Players.getLocal().isMoving(), 10000);
                Sleep.sleep(1450, 2850);
                RandomHandler.powerThroughDialogue();
                Sleep.sleep(550, 2500);
            }
            if (Inventory.contains("Lamp")) return RandomHandler.useLamp() ? -1 : 1;
        } else {
            return -1;
        }
        return 1;
    }

}
