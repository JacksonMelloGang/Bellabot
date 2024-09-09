package fr.askyna.bellabot;

import fr.askyna.bellabot.commands.cmd.SetupCommand;

public class SetupManager {

    public SetupManager(){
        BellaBot.getCommandManager().registerCommand(new SetupCommand());
    }

}
