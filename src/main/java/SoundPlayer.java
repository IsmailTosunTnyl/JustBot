import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer extends ListenerAdapter {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    Dotenv dotenv;

    public SoundPlayer() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
         dotenv = Dotenv.load();
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        LastEvent.lastEvent=event;

        String[] command = event.getMessage().getContentRaw().split(" ");
          if ("help".equalsIgnoreCase(command[1])){
             new HelpListener(event);
          return;
          }

         if (command[1].equalsIgnoreCase("8")){

             if (event.getMember().getId().equalsIgnoreCase("751901481728213112") || isAdmin(event.getMember().getId())){


             }else{
                 event.getChannel().sendMessage(
              new EmbedBuilder().setTitle("Olmaz!!",null).setColor(Color.RED)
                      .setDescription("Sadece Secilmis Kisi Gercegi Gorebilir").build()).queue();

                 return;

             }
         }
        if ("d".equalsIgnoreCase(command[0])){
            if (Integer.parseInt(command[1])>Dses.dsesArray.length|| Integer.parseInt(command[1])<1){
                event.getChannel().sendMessage("Yakinda \" O \" da gelecek").queue();
                return;

            }else {

                loadAndPlay(event.getChannel(),Dses.dsesArray[Integer.parseInt(command[1])-1].link);

            }



        }




        if ("-play".equals(command[0]) && command.length == 2) {
            loadAndPlay(event.getChannel(), command[1]);
        } else if ("~skip".equals(command[0])) {
            skipTrack(event.getChannel());
        }

        super.onGuildMessageReceived(event);
    }

    private boolean isAdmin(String id) {

      return id.equalsIgnoreCase(dotenv.get("ADMINID"));
    }

    private void loadAndPlay(final TextChannel channel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {


                play(channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }



                play(channel.getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Boyle bisi yok " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Oynamıyor " + exception.getMessage()).queue();
            }
        });
    }

    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
        connectToFirstVoiceChannel(guild.getAudioManager());

        musicManager.scheduler.queue(track);
    }

    private void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("Atladım bunu").queue();
    }

    private static void connectToFirstVoiceChannel(AudioManager audioManager) {
      if (LastEvent.lastEvent.getMember().getVoiceState().inVoiceChannel()){
          audioManager.openAudioConnection(LastEvent.lastEvent.getMember().getVoiceState().getChannel());

      }else
          LastEvent.lastEvent.getChannel().sendMessage("sesli gel sesli").queue();


       /*if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {

            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                audioManager.openAudioConnection(voiceChannel);
                break;
            }
        }*/
    }
}
