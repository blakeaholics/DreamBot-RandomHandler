package com.holic.randomhandler;

import org.dreambot.api.Client;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import java.awt.*;

/**
 * RandomHandler - A collection of random event solvers
 *
 * @author holic
 * @version 1.75
 * @url https://github.com/blakeaholics/DreamBot-RandomHandler
 */

public class RandomHandler {

    private static final int WIDGET_LAMP = 240;
    private static final int WIDGET_LAMP_ENTER = 26;
    private static int solvedCount = 0;

    public static void loadRandoms() {
        Client.getInstance().getRandomManager().disableSolver(RandomEvent.DISMISS);
        Client.getInstance().getRandomManager().disableSolver(RandomEvent.GENIE);
        Client.getInstance().getRandomManager().registerSolver(new DismissSolver());
        Client.getInstance().getRandomManager().registerSolver(new GenieSolver());
        Client.getInstance().getRandomManager().registerSolver(new RickyTurpentineSolver());
        Client.getInstance().getRandomManager().registerSolver(new FreakyForesterSolver());
        Client.getInstance().getRandomManager().registerSolver(new OldManSolver());
        Client.getInstance().getRandomManager().registerSolver(new DrunkenDwarfSolver());
        Client.getInstance().getRandomManager().registerSolver(new FrogSolver());
        Client.getInstance().getRandomManager().registerSolver(new BeekeeperSolver());
    }

    public static void loadSolver(Event solver) {
        switch (solver) {
            case DISMISS:
                Client.getInstance().getRandomManager().registerSolver(new DismissSolver());
                break;
            case GENIE:
                Client.getInstance().getRandomManager().registerSolver(new GenieSolver());
                break;
            case RICKY_TURPENTINE:
                Client.getInstance().getRandomManager().registerSolver(new RickyTurpentineSolver());
                break;
            case FREAKY_FORESTER:
                Client.getInstance().getRandomManager().registerSolver(new FreakyForesterSolver());
                break;
            case OLD_MAN:
                Client.getInstance().getRandomManager().registerSolver(new OldManSolver());
                break;
            case DRUNKEN_DWARF:
                Client.getInstance().getRandomManager().registerSolver(new DrunkenDwarfSolver());
                break;
            case FROG:
                Client.getInstance().getRandomManager().registerSolver(new FrogSolver());
                break;
            case BEEKEEPER:
                Client.getInstance().getRandomManager().registerSolver(new BeekeeperSolver());
                break;
        }
    }

    public static void unloadSolver(Event solver) {
        switch (solver) {
            case DISMISS:
                Client.getInstance().getRandomManager().unregisterSolver("DismissyWitItSolver");
                break;
            case GENIE:
                Client.getInstance().getRandomManager().unregisterSolver("IDreamOfGenieSolver");
                break;
            case RICKY_TURPENTINE:
                Client.getInstance().getRandomManager().unregisterSolver("RickyTurpentineSolver");
                break;
            case FREAKY_FORESTER:
                Client.getInstance().getRandomManager().unregisterSolver("FreakyForesterSolver");
                break;
            case OLD_MAN:
                Client.getInstance().getRandomManager().unregisterSolver("OldManSolver");
                break;
            case DRUNKEN_DWARF:
                Client.getInstance().getRandomManager().unregisterSolver("DrunkenDwarfSolver");
                break;
            case FROG:
                Client.getInstance().getRandomManager().unregisterSolver("FrogSolver");
                break;
            case BEEKEEPER:
                Client.getInstance().getRandomManager().unregisterSolver("BeekeeperSolver");
                break;
        }
    }

    public static void clearRandoms() {
        Client.getInstance().getRandomManager().enableSolver(RandomEvent.DISMISS);
        Client.getInstance().getRandomManager().enableSolver(RandomEvent.GENIE);
        Client.getInstance().getRandomManager().unregisterSolver("IDreamOfGenieSolver");
        Client.getInstance().getRandomManager().unregisterSolver("DismissyWitItSolver");
        Client.getInstance().getRandomManager().unregisterSolver("DrunkenDwarfSolver");
        Client.getInstance().getRandomManager().unregisterSolver("FreakyForesterSolver");
        Client.getInstance().getRandomManager().unregisterSolver("OldManSolver");
        Client.getInstance().getRandomManager().unregisterSolver("RickyTurpentineSolver");
        Client.getInstance().getRandomManager().unregisterSolver("FrogSolver");
        Client.getInstance().getRandomManager().unregisterSolver("BeekeeperSolver");
    }

    public static boolean useLamp() {
        if (!Inventory.contains("Lamp")) return false;

        if (!Tabs.isOpen(Tab.INVENTORY)) {
            Tabs.open(Tab.INVENTORY);
            Sleep.sleep(550, 1500);
        }

        if (Tabs.isOpen(Tab.INVENTORY)) {
            log("I love lamp. I love lamp! I love lamp!");
            if (Inventory.interact("Lamp", "Rub")) {
                Sleep.sleep(1350, 2500);
                Sleep.sleepUntil(() -> Widgets.getWidget(WIDGET_LAMP) != null, Calculations.random(6000, 9000));
                Widget skills = Widgets.getWidget(WIDGET_LAMP);
                WidgetChild skill = null;
                if (skills != null) {
                    switch (Calculations.random(5)) {
                        case 0:
                            if (Calculations.random(2) == 1) {
                                skill = skills.getChild(7); //HITPOINTS
                            } else {
                                skill = skills.getChild(8); //PRAYER
                            }
                            break;
                        case 1:
                        case 2:
                            switch (org.dreambot.api.methods.combat.Combat.getCombatStyle()) {
                                case ATTACK:
                                    skill = skills.getChild(2);
                                    break;
                                case STRENGTH:
                                    skill = skills.getChild(3);
                                    break;
                                case RANGED:
                                    skill = skills.getChild(4);
                                    break;
                                case MAGIC:
                                    skill = skills.getChild(5);
                                    break;
                                case DEFENCE:
                                    skill = skills.getChild(6);
                                    break;
                            }
                            break;
                        default:
                            skill = Client.isMembers() ? skills.getChild(Calculations.random(2, 24)) : skills.getChild(Calculations.random(2, 16));
                    }

                    if (skill != null) {
                        skill = skill.getChild(4);
                        if (skill.interact()) {
                            Sleep.sleep(550, 1500);
                            WidgetChild enter = skills.getChild(WIDGET_LAMP_ENTER).getChild(0);
                            if (enter != null) {
                                if (enter.interact()) {
                                    log("(We've got a lamp and we're using it)");
                                    Sleep.sleep(550, 1500);
                                    increaseSolvedCount();
                                    return true;
                                }
                            }
                            Widgets.closeAll();
                            return false;
                        }
                    }
                }
                log("Failed to get the skill widget for the lamp");
            } else {
                log("Failed to use the lamp :(");
            }
        }
        return false;
    }

    public static void powerThroughDialogue() {
        if (Dialogues.inDialogue()) {
            while (Dialogues.canContinue() || Dialogues.isProcessing()) {
                if (Dialogues.continueDialogue()) {
                    log("Continuing dialogue");
                    Sleep.sleep(800, 3500);
                }
            }
        }
    }

    public static void increaseSolvedCount() {
        solvedCount++;
    }

    public static int getSolvedCount() {
        return solvedCount;
    }

    public static void log(String msg, String solver) {
        Logger.log(new Color(93, 180, 82), "[" + solver + "] " + msg);
    }

    public static void log(String msg) {
        Logger.log(new Color(93, 180, 82), "[RandomHandler] " + msg);
    }

    enum Event {
        DISMISS, GENIE, RICKY_TURPENTINE, FREAKY_FORESTER, OLD_MAN, DRUNKEN_DWARF, FROG, BEEKEEPER
    }
}