import org.dreambot.api.Client;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;

import java.awt.*;

public class RandomHandler {

    public static void loadRandoms() {
        Client.getInstance().getRandomManager().disableSolver(RandomEvent.DISMISS);
        Client.getInstance().getRandomManager().disableSolver(RandomEvent.GENIE);
        Client.getInstance().getRandomManager().registerSolver(new DismissSolver());
        Client.getInstance().getRandomManager().registerSolver(new GenieSolver());
        Client.getInstance().getRandomManager().registerSolver(new RickyTurpentineSolver());
        Client.getInstance().getRandomManager().registerSolver(new FreakyForesterSolver());
        Client.getInstance().getRandomManager().registerSolver(new OldManSolver());
        Client.getInstance().getRandomManager().registerSolver(new DrunkenDwarfSolver());
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
    }

    public static void powerThroughDialogue() {
        if (Dialogues.inDialogue()) {
            while (Dialogues.canContinue() || Dialogues.isProcessing()) {
                if (Dialogues.continueDialogue()) {
                    Logger.log("Continuing dialogue");
                    Sleep.sleep(800, 3500);
                }
            }
        }
    }

    public static void log(String msg, String solver) {
        Logger.log(new Color(93, 180, 82), "[" + solver + "] " + msg);
    }
}
