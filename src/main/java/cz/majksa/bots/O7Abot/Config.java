package cz.majksa.bots.O7Abot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private final String prefix;
    private final String token;
    private final String channel;
    private final String announcements;
    private final String yes;
    private final String no;

    public Config() throws IOException {
        final Properties properties = new Properties();
        properties.load(new FileInputStream("settings.properties"));
        prefix = properties.getProperty("prefix", "!results ");
        token = properties.getProperty("token");
        channel = properties.getProperty("channel");
        announcements = properties.getProperty("voting-channel");
        yes = properties.getProperty("yes");
        no = properties.getProperty("no");
    }

    public String getPrefix() {
        return prefix;
    }

    public String getToken() {
        return token;
    }

    public String getChannel() {
        return channel;
    }

    public String getAnnouncements() {
        return announcements;
    }

    public String getYes() {
        return yes;
    }

    public String getNo() {
        return no;
    }
}
