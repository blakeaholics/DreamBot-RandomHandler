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
import java.util.Arrays;

/**
 * FrogSolver - Kisses the frog
 *
 * @author holic
 * @url https://github.com/blakeaholics/DreamBot-RandomHandler
 */
public class FrogSolver extends RandomSolver {

    private final Integer[] FROGS = {5903, 5429, 5901, 5900, 5899, 5902};
    private final int MAIN_FROG = 5901;
    private final Integer[] ROYAL_FROGS = {5434, 5435};

    public FrogSolver() {
        super("FrogSolver");
    }

    @Override
    public boolean shouldExecute() {
        NPC froggyFrog = NPCs.closest(FROGS);// Any of the frog chars
        NPC froggyPrince = NPCs.closest(ROYAL_FROGS);
        return (froggyFrog != null && froggyFrog.getInteractingCharacter() != null && froggyFrog.getInteractingCharacter().equals(Players.getLocal()))
                || (froggyPrince != null && froggyPrince.getInteractingCharacter() != null && froggyPrince.getInteractingCharacter().equals(Players.getLocal()));
    }

    @Override
    public void onPaint(Graphics2D graphics) {
        Rectangle screen = Client.getCanvas().getBounds();
        graphics.setFont(new Font("default", Font.BOLD, 16));
        graphics.setColor(new Color(74, 255, 129, 161));
        graphics.fillRect(0, 0, screen.width, screen.height);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Mmmm frog slobber", 301, 501);
        graphics.setColor(Color.WHITE);
        graphics.drawString("Mmmm frog slobber", 300, 500);
    }

    @Override
    public int onLoop() {
        int ran = Calculations.random(1, 6);
        NPC froggyFrog = NPCs.closest(MAIN_FROG);//Just the main frog
        NPC froggyPrince = NPCs.closest(ROYAL_FROGS);
        froggyPrince = (froggyPrince == null ? NPCs.closest("Frog Prince", "Frog Princess") : froggyPrince);

        if (froggyFrog != null) {
            RandomHandler.log("Yum yum! It's kissy time :* :* :*", "FrogSolver");
            if (Calculations.random(2) == 1) {
                RandomHandler.log("Licking lips for " + ran + " seconds...", "FrogSolver");
                Sleep.sleep(ran * 1000L);
            }
            Sleep.sleep(350, 850);
            if (froggyFrog.interact()) {
                RandomHandler.log("[fake surprise] Oh! A kiss from me?", "FrogSolver");
                Sleep.sleep(1450, 2850);
                Sleep.sleepUntil(Dialogues::inDialogue, 10000);
                RandomHandler.powerThroughDialogue();
                Sleep.sleep(1350, 2850);
                if (Arrays.stream(Dialogues.getOptions()).noneMatch(o -> o.toLowerCase().contains("no"))) {
                    Sleep.sleep(1450, 2850);
                    Sleep.sleepUntil(Dialogues::canContinue, 10000);
                    RandomHandler.powerThroughDialogue();
                    Sleep.sleep(1350, 2850);
                    return 1;
                }
            }

            if (Dialogues.inDialogue()) {
                Sleep.sleep(1350, 2850);
                if (Arrays.stream(Dialogues.getOptions()).noneMatch(o -> o.toLowerCase().contains("no"))) {
                    Sleep.sleep(1450, 2850);
                    Sleep.sleepUntil(Dialogues::canContinue, 10000);
                    RandomHandler.powerThroughDialogue();
                    Sleep.sleep(1350, 2850);
                    return 1;
                }
            } else if (!Dialogues.inDialogue() && froggyPrince != null && froggyPrince.exists()) {
                if (froggyPrince.interact()) {
                    RandomHandler.log("Talking to their highness again", "FrogSolver");
                    Sleep.sleep(1450, 2850);
                    Sleep.sleepUntil(Dialogues::inDialogue, 10000);
                    RandomHandler.powerThroughDialogue();
                    Sleep.sleep(1350, 2850);
                    RandomHandler.increaseSolvedCount();
                    return -1;
                }
            }
        } else {
            return -1;
        }
        return 1;
    }
}
