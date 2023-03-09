package com.holic.randomhandler;

import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.randoms.RandomSolver;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.message.Message;

import java.awt.*;
import java.util.Comparator;
import java.util.List;


/**
 * FreakyForesterSolver - solves Freaky Forester with a commented out option to exit your script if it gets stuck. You take care of the rest.
 *
 * @author holic
 * @url https://github.com/blakeaholics/DreamBot-RandomHandler
 */
public class FreakyForesterSolver extends RandomSolver implements ChatListener {
    private final int freakOverworld = 6748;
    private final int freakInstance = 372;
    Area areaFreak = new Area(2589, 4785, 2616, 4763);
    private int tailID = 0;
    private boolean leave = false;
    private boolean drop = false;

    public FreakyForesterSolver() {
        super("FreakyForesterSolver");
    }

    @Override
    public boolean shouldExecute() {
        NPC forester = NPCs.closest(freakOverworld, freakInstance);
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
        NPC forester = NPCs.closest(freakOverworld, freakInstance);
        forester = (forester == null ? NPCs.closest("Freaky Forester") : forester);
        if (drop) {
            if (!Tabs.isOpen(Tab.INVENTORY)) {
                Tabs.open(Tab.INVENTORY);
                Sleep.sleep(150, 500);
            }
            if (Tabs.isOpen(Tab.INVENTORY)) {
                List<Item> items = Inventory.all();
                items.sort(Comparator.comparingInt(LivePrices::get));
                if (items.get(0) != null) {
                    if (Inventory.interact(items.get(0), "Drop")) Sleep.sleep(350, 2500);
                }
            }
        }
        if (areaFreak.contains(Players.getLocal().getTile()) && Inventory.contains("Raw pheasant") || leave) {
            if (!leave) {
                forester = NPCs.closest(freakInstance);
                forester = (forester == null ? NPCs.closest("Freaky Forester") : forester);
                RandomHandler.log("Talking to the freak...", "FreakyForesterSolver");
                if (forester.interact()) {
                    Sleep.sleep(550, 2500);
                    Sleep.sleepWhile(() -> Players.getLocal().isMoving(), 5000);
                    Sleep.sleepUntil(() -> Dialogues.getNPCDialogue() != null, 10000);
                    RandomHandler.powerThroughDialogue();
                    leave = true;
                    Sleep.sleep(550, 2500);
                }
                /*if (Dialogues.inDialogue() && (Dialogues.getOptions() != null || Dialogues.canContinue())) {
                    leave = false;
                    RandomHandler.log("Something went wrong in the process, restarting...", "FreakyForesterSolver");
                }*/
            } else {
                GameObject portal = GameObjects.closest("Portal", "Exit portal");
                if (portal != null && portal.interact()) {
                    Sleep.sleep(550, 2500);
                    Sleep.sleepWhile(() -> Players.getLocal().isMoving(), 10000);

                    if (Dialogues.inDialogue() && (Dialogues.getOptions() != null || Dialogues.canContinue())) {
                        if (Dialogues.getOptions() != null & Dialogues.chooseFirstOptionContaining("leave", "Leave")) {
                            Sleep.sleep(1550, 4500);
                            Sleep.sleepWhile(() -> Client.getGameState().equals(GameState.LOADING), 10000);
                        }
                        if (Dialogues.getOptions() != null & Dialogues.chooseFirstOptionContaining("Yes", "yes")) {
                            Sleep.sleep(1550, 4500);
                            Sleep.sleepWhile(() -> Client.getGameState().equals(GameState.LOADING), 10000);
                        }
                        leave = true;
                        return 1;

                    }
                    RandomHandler.log("And getting the hell out of here!", "FreakyForesterSolver");

                    Sleep.sleep(350, 850);
                    RandomHandler.increaseSolvedCount();
                    drop = false;
                    leave = false;
                    return -1;
                }
            }
            return 1;
        }

        //Before entering Freak's realm
        if (!areaFreak.contains(Players.getLocal().getTile())) {
            if (Calculations.random(2) == 1) {
                int ran = Calculations.random(2, 6);
                RandomHandler.log("Ignoring the freak for " + ran + " seconds", "FreakyForesterSolver");
                Sleep.sleep(ran * 1000L);
            }
            Sleep.sleep(350, 850);
            if (Inventory.isItemSelected()) {
                RandomHandler.log("Oops, item is selected", "DismissSolver");
                Inventory.deselect();
                Sleep.sleep(350, 850);
            }
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
        }


        //In Freak's realm but no assignment yet
        if (areaFreak.contains(Players.getLocal().getTile()) && tailID == 0 && !leave) {
            forester = NPCs.closest(freakInstance);
            forester = (forester == null ? NPCs.closest("Freaky Forester") : forester);
            RandomHandler.log("Lets find out what he wants...", "FreakyForesterSolver");
            if (forester.interact()) {
                Sleep.sleep(550, 3850);
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
        }

        //We have our target pheasant
        if (areaFreak.contains(Players.getLocal().getTile()) && tailID > 0 && !Inventory.contains("Raw pheasant") && !leave && !drop) {
            GroundItem rawPheasant = GroundItems.closest("Raw pheasant");
            if (rawPheasant == null) {
                NPC pheasant = NPCs.closest(tailID);
                if (pheasant != null && pheasant.interact()) {
                    RandomHandler.log("Killing pheasant", "FreakyForesterSolver");
                    Sleep.sleep(550, 2500);
                    Sleep.sleepWhile(() -> pheasant != null && pheasant.exists(), 20000);
                }
            }

            if (rawPheasant != null && !drop) {
                if (rawPheasant.interact()) {
                    RandomHandler.log("Grabbing pheasant corpse", "FreakyForesterSolver");
                    Sleep.sleep(550, 2500);
                    Sleep.sleepWhile(() -> Players.getLocal().isMoving(), 10000);
                    Sleep.sleep(350, 850);
                    return 1;
                }
            }

            if (Inventory.contains("Raw pheasant")) {
                RandomHandler.log("Successfully obtained a pheasant corpse", "FreakyForesterSolver");
                Sleep.sleep(50, 2500);
                leave = true;
            }
        }
        return 1;
    }

    @Override
    public void onGameMessage(Message message) {
        if (message.getMessage().contains("allowed to leave")) {
            leave = true;
        }
        if (message.getMessage().contains("space")) {
            drop = true;
        }
    }
}