package cz.majksa.bots.O7Abot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class CommandListener extends ListenerAdapter {

    private final TextChannel channel;
    private final VotingManager votingManager;
    private final String prefix;

    public CommandListener(Config config, JDA jda) {
        votingManager = new VotingManager(config);
        channel = jda.getTextChannelById(config.getChannel());
        prefix = config.getPrefix();
        jda.addEventListener(this);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!channel.equals(event.getChannel())) {
            return;
        }
        final String raw = event.getMessage().getContentRaw();
        if (!raw.startsWith(prefix)) {
            return;
        }
        final String[] args = raw.substring(prefix.length()).split(" ");
        if (args.length == 0) {
            return;
        }
        final String[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);
        try {
            switch (args[0]) {
                case "results":
                    results(event, cmdArgs);
                    break;
                // ADD COMMANDS HERE
            }
        } catch (Exception e) {
            event.getMessage().reply(e.getMessage()).queue();
        }
    }

    private void results(GuildMessageReceivedEvent event, String[] args) {
        boolean allowBoth = false;
        String id;
        switch (args.length) {
            case 2:
                allowBoth = Boolean.parseBoolean(args[1]);
            case 1:
                id = args[0];
                break;
            default:
                throw new RuntimeException("The usage of this command is `!results <id>[ <allow-both=false>]`");
        }
        final VotingManager.Result result = votingManager.getVotes(event.getGuild(), id);
        final EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Voting")
                .addField("Reacted " + result.getYesEmoji() + ": " + result.getYes().size(), result.getYes().stream().map(User::getAsMention).collect(Collectors.joining("\n")), false)
                .addField("Reacted " + result.getNoEmoji() + ": " + result.getNo().size(), result.getNo().stream().map(User::getAsMention).collect(Collectors.joining("\n")), false);
        if (allowBoth) {
            embedBuilder.addField("Reacted both: " + result.getBoth().size(), result.getBoth().stream().map(User::getAsMention).collect(Collectors.joining("\n")), false);
        }
        embedBuilder
                .addField("Have not reacted: " + result.getNeither().size(), result.getNeither().stream().map(User::getAsMention).collect(Collectors.joining("\n")), false)
                .setTimestamp(new Date().toInstant())
                .setFooter(event.getJDA().getSelfUser().getName(), event.getJDA().getSelfUser().getAvatarUrl());
        event.getMessage().reply(embedBuilder.build()).queue();
    }

}
