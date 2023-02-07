package com.holic.randomhandler;

import org.dreambot.api.Client;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.randoms.RandomSolver;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * DismissSolver - Dismisses the randoms I haven't solved yet
 *
 * @author holic
 * @url https://github.com/blakeaholics/DreamBot-RandomHandler
 */
public class DismissSolver extends RandomSolver {
    String[] randoms = {"Bee keeper", "Capt' Arnav", "Dunce", "Evil Bob", "Flippa", "Tilt", "Giles", "Jekyll and Hyde", "Leo", "Miles", "Molly", "Mr. Mordaut", "Niles", "Pillory Guard", "Postie Pete", "Prison Pete", "Quiz Master", "Sandwich lady", "Sergeant Damien", "Servant", "Strange plant"};
    ArrayList<String> lstRandoms = new ArrayList(Arrays.asList(randoms));

    public DismissSolver() {
        super("DismissyWitItSolver");
    }

    @Override
    public boolean shouldExecute() {
        NPC random = NPCs.closest(randoms);
        return (Players.getLocal().getInteractingCharacter() != null && lstRandoms.contains(Players.getLocal().getInteractingCharacter().getName()))
                || (random != null && random.getInteractingCharacter() != null && random.getInteractingCharacter().equals(Players.getLocal()));
    }

    @Override
    public void onPaint(Graphics2D graphics) {
        Rectangle screen = Client.getCanvas().getBounds();
        graphics.setFont(new Font("default", Font.BOLD, 16));
        graphics.setColor(new Color(74, 255, 129, 161));
        graphics.fillRect(0, 0, screen.width, screen.height);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Dismissing Random Event", 301, 501);
        graphics.setColor(Color.WHITE);
        graphics.drawString("Dismissing Random Event", 300, 500);
    }

    @Override
    public int onLoop() {
        NPC random = NPCs.closest(randoms);
        if (random != null) {
            if (Calculations.random(2) == 1) {
                int ran = Calculations.random(1, 6);
                RandomHandler.log("Delaying speaking to " + random.getName() + " for " + ran + " seconds...", "DismissSolver");
                Sleep.sleep(ran * 1000L);
            }
            Sleep.sleep(350, 850);
            RandomHandler.log("Dismissing " + random.getName(), "DismissSolver");
            if (random.interact("Dismiss")) {
                Sleep.sleep(450, 1850);
                return -1;
            }
        }

        return 1;
    }
}