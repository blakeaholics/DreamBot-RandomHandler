package com.holic.randomhandler;

import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.randoms.RandomSolver;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;

import java.awt.*;

/**
 * FreakyForesterSolver - solves Freaky Forester with a commented out option to exit your script if it gets stuck. You take care of the rest.
 *
 * @author holic
 * @url https://github.com/blakeaholics/DreamBot-RandomHandler
 */
public class FreakyForesterSolver extends RandomSolver {
    Area areaFreak = new Area(2589, 4785, 2616, 4763);
    private int tailID = 0;
    private boolean leave = false;

    public FreakyForesterSolver() {
        super("FreakyForesterSolver");
    }

    @Override
    public boolean shouldExecute() {
        NPC forester = NPCs.closest(372);
        forester = (forester == null ? NPCs.closest("Freaky Forester") : forester);
        return (forester != null && forester.getInteractingCharacter() != null && forester.getInteractingCharacter().equals(Players.getLocal()))
                || areaFreak.contains(Players.getLocal().getTile());
    }

    @Override
    public void onPaint(Graphics2D graphics) {
        Rectangle screen = Client.getCanvas().getBounds();
        graphics.setFont(new Font("default", Font.BOLD, 16));
        graphics.setColor(new Color(74, 255, 129, 161));
        graphics.fillRect(0, 0, screen.width, screen.height);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Getting Freaky", 301, 501);
        graphics.setColor(Color.WHITE);
        graphics.drawString("Getting Freaky", 300, 500);
    }

    @Override
    public int onLoop() {
        NPC forester = NPCs.closest(372);
        forester = (forester == null ? NPCs.closest("Freaky Forester") : forester);
        //Before entering Freak's realm
        if (!areaFreak.contains(Players.getLocal().getTile())) {
            if (Calculations.random(2) == 1) {
                int ran = Calculations.random(2, 6);
                RandomHandler.log("Ignoring the freak for " + ran + " seconds", "FreakyForesterSolver");
                Sleep.sleep(ran * 1000L);
            }
            Sleep.sleep(350, 850);
            return 1;
		}
            if (!areaFreak.contains(Players.getLocal().getTile()) && forester.interact()) {
                Sleep.sleep(1450, 3850);
                Sleep.sleepUntil(Dialogues::inDialogue, 10000);
                RandomHandler.powerThroughDialogue();
                if (Dialogues.chooseFirstOptionContaining("Okay")) {
                    Sleep.sleep(450, 1850);
                    RandomHandler.powerThroughDialogue();
                    Sleep.sleep(1450, 3850);
                }
                Sleep.sleep(2000, 5500);
                Sleep.sleepWhile(() -> Client.getGameState().equals(GameState.LOADING), Calculations.random(8500, 11000));
            return 1;
            }
        

        //In Freak's realm but no assignment yet
        if (areaFreak.contains(Players.getLocal().getTile()) && tailID == 0) {
            forester = NPCs.closest(372);
            forester = (forester == null ? NPCs.closest("Freaky Forester") : forester);
            RandomHandler.log("Lets find out what he wants...", "FreakyForesterSolver");
            if (forester.interact()) {
                Sleep.sleep(1450, 3850);
                Sleep.sleepUntil(Dialogues::inDialogue, 10000);//Maybe pull this if out of forester if?
                if (Dialogues.getNPCDialogue().contains("tail")) {
                    String tailFeathers = Dialogues.getNPCDialogue().split("kill")[1].split("tail")[0];
                    if (!tailFeathers.equals("")) {
                        int feathers = 1;
                        if (tailFeathers.contains("two") || tailFeathers.contains("2")) {
                            feathers = 2;
                        } else if (tailFeathers.contains("three") || tailFeathers.contains("3")) {
                            feathers = 3;
                        } else if (tailFeathers.contains("four") || tailFeathers.contains("4")) {
                            feathers = 4;
                        }
                        tailID = 5496 + feathers;
                        RandomHandler.log("Okay the freak wants " + feathers + "[ID:" + tailID + "] tail feathers", "FreakyForesterSolver");

                        return 1;
                    }
                } else if (Dialogues.getNPCDialogue().contains("leave")) {
                    leave = true;
                }
            }
            return 1;
        }

        //We have our target pheasant
        if (areaFreak.contains(Players.getLocal().getTile()) && tailID > 0 && !Inventory.contains("Raw pheasant")) {
            GroundItem rawPheasant = GroundItems.closest("Raw pheasant");
            if (rawPheasant == null) {
                NPC pheasant = NPCs.closest(tailID);
                RandomHandler.log("Killing pheasant", "FreakyForesterSolver");
                if (pheasant != null && pheasant.interact()) {
                    Sleep.sleep(550, 2500);
                    Sleep.sleepWhile(() -> pheasant != null && pheasant.exists(), 20000);
                }
                return 1;
            }

            if (rawPheasant != null) {
                if (rawPheasant.interact()) {
                    Sleep.sleep(550, 2500);
                    Sleep.sleepWhile(() -> Players.getLocal().isMoving(), 10000);
                    Sleep.sleep(350, 850);
                    return 1;
                }
            }
        }

        if (areaFreak.contains(Players.getLocal().getTile()) && Inventory.contains("Raw pheasant") || leave) {
            forester = NPCs.closest(372);
            forester = (forester == null ? NPCs.closest("Freaky Forester") : forester);
            RandomHandler.log("Talking to the freak...", "FreakyForesterSolver");

            if (forester.interact() && !leave) {
                Sleep.sleep(550, 2500);
                Sleep.sleepWhile(() -> Players.getLocal().isMoving(), 10000);
                Sleep.sleepUntil(() -> Dialogues.getNPCDialogue()!= null, 10000);
                RandomHandler.powerThroughDialogue();
                leave = true;
                Sleep.sleep(550, 2500);
            }
			if (Dialogues.inDialogue() 
				&& (Dialogues.getOptions() != null || Dialogues.canContinue())) {
				leave = false;
				RandomHandler.log("Something went wrong in the process, restarting...", "FreakyForesterSolver");
			}
			
            if (leave) {
                GameObject portal = GameObjects.closest("Portal", "Exit portal");
                if (portal != null && portal.interact()) {
                    Sleep.sleep(550, 2500);
                    Sleep.sleepWhile(() -> Players.getLocal().isMoving(), 10000);
					
			if (Dialogues.inDialogue() 
				&& (Dialogues.getOptions() != null || Dialogues.canContinue())) {
					if (Calculations.random(10) > 2) {
				leave = false;
				RandomHandler.log("Something went wrong in the process, restarting...", "FreakyForesterSolver");
					} else {
				RandomHandler.log("Something went wrong in, let's bail", "FreakyForesterSolver");
					}
			}
                    RandomHandler.log("And getting the hell out of here!", "FreakyForesterSolver");

                    Sleep.sleep(350, 850);
                    return -1;
                }
            }
            return 1;
        }

        /*if (script.getScriptTile().distance() >= 45) {
            RandomHandler.log("Failed to solve Freaky Forester!", "FreakyForesterSolver");
            Sleep.sleep(350, 2550);
            RandomHandler.log("Attempting to get the hell out of here...", "FreakyForesterSolver");
            GameObject portal = GameObjects.closest("Portal", "Exit portal");
            if (portal != null) {
                if (portal.distance() > 13) {
                    WalkUtil.walk(portal);
                    Sleep.sleep(350, 950);
                }
                if (portal.interact()) {
                    RandomHandler.log("Getting the hell out successful!", "FreakyForesterSolver");
                    Sleep.sleep(350, 850);
                    Sleep.sleepWhile(() -> Players.getLocal().isMoving(), 10000);
                    
                    return -1;
                }
            } else if (script.getScriptTile().distance() >= 45) {
                RandomHandler.log("Completely failed Freaky Forester, ending...", "FreakyForesterSolver");
                script.config.setEnd(true);
            }
        } else {
            RandomHandler.log("Failed Freaky Forester, continuing..", "FreakyForesterSolver");
        }*/
        return 1;
    }
}