package cz.majksa.bots.O7Abot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.EnumSet;

public class Bot {

    public static void main(String[] args) {
        try {
            final Config config = new Config();
            final JDA jda = JDABuilder
                    .createDefault(config.getToken(), EnumSet.allOf(GatewayIntent.class))
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .build()
                    .awaitReady();
            new CommandListener(config, jda);
        } catch (LoginException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
