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
 * RickyTurpentineSolver - solves Ricky Turpentine
 *
 * @author holic
 * @url https://github.com/blakeaholics/DreamBot-RandomHandler
 */
public class RickyTurpentineSolver extends RandomSolver {

    public RickyTurpentineSolver() {
        super("RickyTurpentineSolver");
    }

    @Override
    public boolean shouldExecute() {
        NPC rickTurps = NPCs.closest(375, 376);//Just need to click ok
        rickTurps = (rickTurps == null ? NPCs.closest("Rick Turpentine") : rickTurps);
        return (rickTurps != null && rickTurps.getInteractingCharacter() != null && rickTurps.getInteractingCharacter().equals(Players.getLocal()));
    }

    @Override
    public void onPaint(Graphics2D graphics) {
        Rectangle screen = Client.getCanvas().getBounds();
        graphics.setFont(new Font("default", Font.BOLD, 16));
        graphics.setColor(new Color(74, 255, 129, 161));
        graphics.fillRect(0, 0, screen.width, screen.height);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Ugh, Ricky is back", 301, 501);
        graphics.setColor(Color.WHITE);
        graphics.drawString("Ugh, Ricky is back", 300, 500);
    }

    @Override
    public int onLoop() {
        int ran = Calculations.random(1, 6);
        NPC rickTurps = NPCs.closest(375, 376);//Just need to click ok
        rickTurps = (rickTurps == null ? NPCs.closest("Rick Turpentine") : rickTurps);

        if (rickTurps != null) {
            RandomHandler.log("Shit, it's Ricky Turps!", "RickyTurpentineSolver");
            if (Calculations.random(2) == 1) {
                RandomHandler.log("Pretending not to see Ricky for " + ran + " seconds...", "RickyTurpentineSolver");
                Sleep.sleep(ran * 1000L);
            }
            Sleep.sleep(350, 850);
            RandomHandler.log("[fake joy] Oh! Hey, Ricky", "RickyTurpentineSolver");
            if (rickTurps.interact()) {
                Sleep.sleep(1450, 2850);
                Sleep.sleepUntil(Dialogues::inDialogue, 10000);
                RandomHandler.powerThroughDialogue();
                Sleep.sleep(1350, 2850);
            }
        } else {
            return -1;
        }
        return 1;
    }
}
