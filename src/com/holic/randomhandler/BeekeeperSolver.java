package com.holic.randomhandler;


import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.randoms.RandomSolver;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import java.awt.*;
import java.util.List;

/**
 * BeekeeperSolver - Builds a hive
 *
 * @author holic
 * @url https://github.com/blakeaholics/DreamBot-RandomHandler
 */
public class BeekeeperSolver extends RandomSolver {

    private final int LID = 28806;
    private final int BODY = 28428;
    private final int ENTRANCE = 28803;
    private final int LEGS = 28808;
    private final int PIECE1 = 10;
    private final int PIECE2 = 11;
    private final int PIECE3 = 12;
    private final int PIECE4 = 13;
    private final int WIDGET = 420;
    private final int beekeeperOverworld = 6747;
    private final int beekeeperInstance = 3235;
    private final Area areaHives = new Area(0, 0, 0, 0);
    int successCount = 0;

    public BeekeeperSolver() {
        super("BeekeeperSolver");
    }

    @Override
    public boolean shouldExecute() {
        NPC beeKeeper = NPCs.closest(beekeeperInstance, beekeeperOverworld);
        return beeKeeper != null && beeKeeper.getInteractingCharacter() != null && beeKeeper.getInteractingCharacter().equals(Players.getLocal());
    }

    @Override
    public void onPaint(Graphics2D graphics) {
        Rectangle screen = Client.getCanvas().getBounds();
        graphics.setFont(new Font("default", Font.BOLD, 16));
        graphics.setColor(new Color(74, 255, 129, 161));
        graphics.fillRect(0, 0, screen.width, screen.height);
        graphics.setColor(Color.BLACK);
        graphics.drawString("BZZZZZZ", 301, 501);
        graphics.setColor(Color.WHITE);
        graphics.drawString("BZZZZZZ", 300, 500);
    }

    @Override
    public int onLoop() {
        int ran = Calculations.random(1, 6);
        NPC beeKeeper = NPCs.closest(beekeeperInstance, beekeeperOverworld);
        beeKeeper = (beeKeeper == null ? NPCs.closest("Beekeeper", "Bee keeper") : beeKeeper);

        if (NPCs.closest(beekeeperOverworld) != null) {
            RandomHandler.log("Bzzz bzzz bzzz", "BeekeeperSolver");
            if (Calculations.random(2) == 1) {
                RandomHandler.log("Bzzz bzzz bzzz for " + ran + " bzzzs...", "BeekeeperSolver");
                Sleep.sleep(ran * 1000L);
            }
            Sleep.sleep(350, 850);
            if (Inventory.isItemSelected()) {
                RandomHandler.log("Oops, item is selected", "DismissSolver");
                Inventory.deselect();
                Sleep.sleep(350, 850);
            }
            if (/*!areaHives.contains(Players.getLocal().getTile())*/NPCs.closest(beekeeperInstance) == null && beeKeeper.interact()) {
                RandomHandler.log("Honey time!", "BeekeeperSolver");
                Sleep.sleep(1450, 2850);
                Sleep.sleepUntil(Dialogues::inDialogue, 10000);
                RandomHandler.powerThroughDialogue();
                Sleep.sleep(1350, 2850);
                if (Dialogues.getOptionIndexContaining("Ooh") > -1) {
                    Sleep.sleep(1450, 2850);
                    Sleep.sleepUntil(Dialogues::canContinue, 10000);
                    RandomHandler.powerThroughDialogue();
                    Sleep.sleep(1350, 2850);
                    return 1;
                }
            }

            if (/*!areaHives.contains(Players.getLocal().getTile())*/NPCs.closest(beekeeperInstance) == null && Dialogues.inDialogue()) {
                Sleep.sleep(1350, 2850);
                if (Dialogues.getOptionIndexContaining("Ooh") > -1) {
                    Sleep.sleep(1450, 2850);
                    Sleep.sleepUntil(Dialogues::canContinue, 10000);
                    RandomHandler.powerThroughDialogue();
                    Sleep.sleep(1350, 2850);
                    return 1;
                }
            }
        }

        if (NPCs.closest(beekeeperInstance) != null && Widgets.getWidget(WIDGET) == null/*areaHives.contains(Players.getLocal().getTile())*/) {
            if (Dialogues.inDialogue()) {
                RandomHandler.powerThroughDialogue();
                Sleep.sleep(1350, 2850);
                if (Dialogues.inDialogue()) {
                    RandomHandler.powerThroughDialogue();
                    Sleep.sleep(1350, 2850);
                    if (Dialogues.getOptionIndexContaining("Yeah") > -1) {
                        Sleep.sleep(1450, 2850);
                        Sleep.sleepUntil(Dialogues::canContinue, 10000);
                        RandomHandler.powerThroughDialogue();
                        Sleep.sleep(1350, 2850);
                    }
                }
            }
        }

        if (Widgets.getWidget(WIDGET) != null && Widgets.getWidget(WIDGET).isVisible()) {
            int[] parts = {LID, ENTRANCE, BODY, LEGS};
            int[] pieces_int = {PIECE1, PIECE2, PIECE3, PIECE4};
            for (int piece : pieces_int) {
                WidgetChild object = Widgets.getChildWidget(WIDGET, piece);
                if (object != null) {
                    for (int part : parts) {
                        if (object.getDisabledMediaID() == part) {
                            String place = "";
                            if (part == LID) {
                                place = "Lid";
                            } else if (part == ENTRANCE) {
                                place = "Entrance";
                            } else if (part == BODY) {
                                place = "Body";
                            } else if (part == LEGS) {
                                place = "Legs";
                            }
                            List<WidgetChild> widgetChildrenContainingText = Widgets.getWidgetChildrenContainingText(place);
                            if (widgetChildrenContainingText.size() == 1) {
                                RandomHandler.log("Attempting to move part " + part + " to piece " + place, "BeekeeperSolver");
                                if (Mouse.move(object.getRectangle())) {
                                    RandomHandler.log("Moving part " + part + " to piece " + place, "BeekeeperSolver");
                                    Sleep.sleep(350, 850);
                                    if (Mouse.drag(widgetChildrenContainingText.get(0).getRectangle()))
                                        Sleep.sleep(350, 850);
                                    widgetChildrenContainingText = Widgets.getWidgetChildrenContainingText(place);
                                    if (widgetChildrenContainingText.isEmpty()) {
                                        RandomHandler.log("Successfully move " + place, "BeekeeperSolver");
                                        successCount++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (successCount >= 4) {
            RandomHandler.log("Successfully moved all pieces!", "BeekeeperSolver");
            if (!Widgets.getWidgetChildrenContainingText("CONFIRM").isEmpty()) {
                if (!Widgets.getWidgetChildrenContainingText("CONFIRM").get(0).interact()) {
                    Sleep.sleep(350, 1850);
                    RandomHandler.increaseSolvedCount();
                    return -1;
                }
            }

        } else if (Dialogues.inDialogue()) {
            Sleep.sleep(1350, 2850);
            RandomHandler.powerThroughDialogue();
            Sleep.sleep(1350, 2850);
            return 1;


        } else {
            return -1;
        }

        return 1;
    }
}

