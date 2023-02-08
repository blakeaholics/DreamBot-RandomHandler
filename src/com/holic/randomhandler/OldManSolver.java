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
 * OldManSolver - solves Mysterious Old Man
 *
 * @author holic
 * @url https://github.com/blakeaholics/DreamBot-RandomHandler
 */
public class OldManSolver extends RandomSolver {

    private final Integer[] OLD_MAN = {2830, 6742, 6750, 6751, 6752, 6753};
    private final String STRING_OLD_MAN = "Mysterious Old Man";

    public OldManSolver() {
        super("OldManSolver");
    }

    @Override
    public boolean shouldExecute() {
        NPC oldMan = NPCs.closest(OLD_MAN);
        oldMan = (oldMan == null ? NPCs.closest(STRING_OLD_MAN) : oldMan);
        return (oldMan != null && oldMan.getInteractingCharacter() != null && oldMan.getInteractingCharacter().equals(Players.getLocal()));
    }

    @Override
    public void onPaint(Graphics2D graphics) {
        Rectangle screen = Client.getCanvas().getBounds();
        graphics.setFont(new Font("default", Font.BOLD, 16));
        graphics.setColor(new Color(74, 255, 129, 161));
        graphics.fillRect(0, 0, screen.width, screen.height);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Robbing an Old Man", 301, 501);
        graphics.setColor(Color.WHITE);
        graphics.drawString("Robbing an Old Man", 300, 500);
    }

    @Override
    public int onLoop() {
        NPC oldMan = NPCs.closest(OLD_MAN);
        oldMan = (oldMan == null ? NPCs.closest(STRING_OLD_MAN) : oldMan);
        if (oldMan != null) {
            if (Dialogues.inDialogue()) {
                if (!Dialogues.getNPCDialogue().contains("mime") && !Dialogues.getNPCDialogue().contains("maze")) {
                    RandomHandler.powerThroughDialogue();
                    Sleep.sleep(550, 2500);
                } else {
                    RandomHandler.log("Old Man is offering a maze or mime, nah!", "OldManSolver");
                    if (Dialogues.canContinue()) {
                        Sleep.sleep(550, 2500);
                        Dialogues.continueDialogue();
                        Sleep.sleep(550, 2500);
                        if (Dialogues.chooseOption(2)) {
                            Sleep.sleep(550, 2500);
                            if (Dialogues.canContinue()) {
                                Sleep.sleep(550, 2500);
                                Dialogues.continueDialogue();
                                Sleep.sleep(350, 850);
                            }
                        }
                    }
                }
                Sleep.sleep(550, 2500);

                return -1;
            }

            if (!Dialogues.inDialogue()) {
                RandomHandler.log("Old Man! Old Man! We've got Old Man here! See? Nobody cares", "OldManSolver");
                if (Calculations.random(2) == 1) {
                    int ran = Calculations.random(2, 6);
                    RandomHandler.log("Delaying speaking to Old Man by " + ran + " seconds", "OldManSolver");
                    Sleep.sleep(ran * 1000L);
                }
                Sleep.sleep(350, 850);
                if (oldMan.interact()) {
                    Sleep.sleep(450, 1850);
                    Sleep.sleepUntil(Dialogues::inDialogue, 10000);
                    if (Calculations.random(3) == 1)
                        RandomHandler.powerThroughDialogue();
                    RandomHandler.increaseSolvedCount();
                }
            }
        }
        return 1;
    }
}