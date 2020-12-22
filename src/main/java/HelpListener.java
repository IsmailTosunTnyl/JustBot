import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HelpListener  {
    GuildMessageReceivedEvent event;

    public HelpListener(GuildMessageReceivedEvent event) {
        this.event = event;


        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Menu");
        embedBuilder.setColor(Color.GREEN);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <Dses.dsesArray.length ; i++) {
           stringBuilder.append((i+1)+"."+Dses.dsesArray[i].name+"\n");

        }
        embedBuilder.setDescription(stringBuilder.toString());
        event.getChannel().sendMessage(embedBuilder.build()).queue();
           embedBuilder.clear();

    }















        }








