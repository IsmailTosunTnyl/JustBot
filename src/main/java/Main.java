import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Map;

public class Main {
    private static JDA jda;

    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
       jda = JDABuilder.createDefault(dotenv.get("TOKEN")).build();
        jda.addEventListener(new SoundPlayer());






    }
    public static JDA getJDA(){
        return jda;

    }

}
