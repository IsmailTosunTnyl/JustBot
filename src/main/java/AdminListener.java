import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class AdminListener extends ListenerAdapter {
    Dotenv dotenv ;
    String TextChannelId="775351096276287541";
    JDA jda;

    public AdminListener(Dotenv dotenv, JDA jda) {
        this.dotenv = dotenv;
        this.jda = jda;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);
        String[] command = event.getMessage().getContentRaw().split(" ");
        if (dotenv.get("ADMINID").equalsIgnoreCase(event.getMember().getId())){

            if ("setId".equalsIgnoreCase(command[0])){
                TextChannelId = command[1];
                event.getChannel().sendMessage("Id Degistirildi").queue();
            }
            else if ("send".equalsIgnoreCase(command[0])){
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 1; i <command.length ; i++) {
             stringBuilder.append(command[i]+" ");
                }
                jda.getTextChannelById(TextChannelId).sendMessage(stringBuilder.toString()).queue();
            }






        }else
            return;




    }
}
