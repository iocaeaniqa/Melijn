package com.pixelatedsource.jda.commands.fun;

import com.pixelatedsource.jda.Helpers;
import com.pixelatedsource.jda.blub.Category;
import com.pixelatedsource.jda.blub.Command;
import com.pixelatedsource.jda.blub.CommandEvent;

import static com.pixelatedsource.jda.PixelSniper.PREFIX;

public class CatCommand extends Command {

    public CatCommand() {
        this.commandName = "cat";
        this.description = "Shows you a random kitty";
        this.usage = PREFIX + commandName;
        this.aliases = new String[]{"kitten", "kat", "poes"};
        this.category = Category.FUN;
    }

    @Override
    protected void execute(CommandEvent event) {
        boolean acces = false;
        if (event.getGuild() == null) acces = true;
        if (!acces) acces = Helpers.hasPerm(event.getGuild().getMember(event.getAuthor()), this.commandName, 0);
        if (acces) {
            event.reply("Currently broken sorry :/");
        } else {
            event.reply("You need the permission `" + commandName + "` to execute this command.");
        }
    }
}
