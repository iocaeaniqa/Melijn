package me.melijn.jda.commands.music;

import me.melijn.jda.Helpers;
import me.melijn.jda.Melijn;
import me.melijn.jda.blub.Category;
import me.melijn.jda.blub.Command;
import me.melijn.jda.blub.CommandEvent;
import me.melijn.jda.blub.Need;
import me.melijn.jda.utils.MessageHelper;
import me.melijn.jda.utils.TaskScheduler;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;

import static me.melijn.jda.Melijn.PREFIX;

public class SetStreamUrlCommand extends Command {


    public SetStreamUrlCommand() {
        this.commandName = "setStreamUrl";
        this.description = "set the stream url of the bot";
        this.usage = PREFIX + commandName + " [list | url]";
        this.aliases = new String[]{"ssu"};
        this.needs = new Need[]{Need.GUILD};
        this.category = Category.MUSIC;
    }

    private HashMap<String, String> linkjes = new HashMap<>() {{
        put("slam-nonstop", "http://stream.radiocorp.nl/web10_mp3");
        put("radio538", "http://18973.live.streamtheworld.com/RADIO538.mp3");
        put("Joe-fm", "http://icecast-qmusic.cdp.triple-it.nl/JOEfm_be_live_128.mp3");
        put("mnm", "http://icecast.vrtcdn.be/mnm-high.mp3");
        put("mnm-hits", "http://icecast.vrtcdn.be/mnm_hits-high.mp3");
        put("Q-music", "http://icecast-qmusic.cdp.triple-it.nl/Qmusic_be_live_64.aac");
        put("Nostalgie", "http://nostalgiewhatafeeling.ice.infomaniak.ch/nostalgiewhatafeeling-128.mp3");
        put("Radio1", "http://icecast.vrtcdn.be/radio1-high.mp3");
        put("Radio2", "http://icecast.vrtcdn.be/ra2wvl-high.mp3");
        put("Studio-Brussel", "http://icecast.vrtcdn.be/stubru-high.mp3");
        put("BBC Radio 1", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio1_mf_p");
        put("BBC Radio 4FM", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio4fm_mf_p");
        put("BBC Radio 6 Music", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_6music_mf_p");
    }};

    @Override
    protected void execute(CommandEvent event) {
        if (Helpers.hasPerm(event.getMember(), commandName, 1)) {
            Guild guild = event.getGuild();
            String[] args = event.getArgs().split("\\s+");
            String url = Melijn.mySQL.getStreamUrl(guild.getIdLong());
            if (url.equals("")) url = "nothing";
            if (args.length == 0 || args[0].equalsIgnoreCase("")) {
                event.reply("StreamURL: " + url);
            } else if (args.length == 1) {
                if (args[0].contains("http://") || args[0].contains("https://")) {
                    TaskScheduler.async(() -> Melijn.mySQL.setStreamUrl(guild.getIdLong(), args[0]));
                    event.reply("Changed the url from **" + url + "** to **" + args[0] + "**");
                } else {
                    if (args[0].equalsIgnoreCase("list")) {
                        event.reply("**Radio**\n" + linkjes.keySet().toString().replaceAll("(,\\s+|,)", "\n+ ").replaceFirst("\\[", "+ ").replaceFirst("]", ""));
                    } else {
                        if (linkjes.keySet().contains(args[0])) {
                            TaskScheduler.async(() -> Melijn.mySQL.setStreamUrl(guild.getIdLong(), linkjes.get(args[0])));
                            event.reply("Changed the url from **" + url + "** to **" + linkjes.get(args[0]) + "**");
                        } else {
                            MessageHelper.sendUsage(this, event);
                        }
                    }
                }
            } else {
                MessageHelper.sendUsage(this, event);
            }
        } else {
            event.reply("You need the permission `" + commandName + "` to execute this command.");
        }
    }
}
