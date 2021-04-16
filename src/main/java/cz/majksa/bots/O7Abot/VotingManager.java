package cz.majksa.bots.O7Abot;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class VotingManager {

    private final Config config;

    public VotingManager(Config config) {
        this.config = config;
    }

    public Result getVotes(Guild guild, String id) {
        Message message = null;
        final TextChannel textChannelById = guild.getTextChannelById(config.getAnnouncements());
        if (textChannelById != null) {
            try {
                message = textChannelById.retrieveMessageById(id).submit().get();
            } catch (InterruptedException | ExecutionException ignored) {
            }
        }
        for (TextChannel textChannel : guild.getTextChannels()) {
            if (message != null) {
                break;
            }
            try {
                message = textChannel.retrieveMessageById(id).submit().get();
            } catch (InterruptedException | ExecutionException ignored) {
            }
        }
        if (message == null) {
            throw new RuntimeException("Message with this id was not found!");
        }
        return getVotes(message);
    }

    public Result getVotes(Message message) {
        final Guild guild = message.getGuild();
        final List<User> notReacted = guild.getMembers()
                .stream()
                .map(Member::getUser)
                .filter(user -> !user.isBot())
                .collect(Collectors.toList());
        final List<User> reactedYes = new ArrayList<>();
        final List<User> reactedNo = new ArrayList<>();
        final String yes = handleVotes(message, guild, notReacted, reactedYes, config.getYes());
        final String no = handleVotes(message, guild, notReacted, reactedNo, config.getNo());
        final List<User> reactedBoth = ListUtils.intersection(reactedYes, reactedNo);
        reactedYes.removeAll(reactedBoth);
        reactedNo.removeAll(reactedBoth);
        return new Result(reactedYes, reactedNo, reactedBoth, notReacted, yes, no);
    }

    private String handleVotes(Message message, Guild guild, List<User> notReacted, List<User> reacted, String emoji) {
        final Emote emote = guild.getEmoteById(emoji);
        final ReactionPaginationAction users = emote == null ? message.retrieveReactionUsers(emoji) : message.retrieveReactionUsers(emote);
        for (User user : users) {
            if (user.isBot()) continue;
            reacted.add(user);
            notReacted.remove(user);
        }
        return emote == null ? emoji : emote.getAsMention();
    }


    public static class Result {
        private final List<User> yes;
        private final List<User> no;
        private final List<User> both;
        private final List<User> neither;
        private final String yesEmoji;
        private final String noEmoji;

        public Result(List<User> yes, List<User> no, List<User> both, List<User> neither, String yesEmoji, String noEmoji) {
            this.yes = yes;
            this.no = no;
            this.both = both;
            this.neither = neither;
            this.yesEmoji = yesEmoji;
            this.noEmoji = noEmoji;
        }

        public List<User> getYes() {
            return yes;
        }

        public List<User> getNo() {
            return no;
        }

        public List<User> getBoth() {
            return both;
        }

        public List<User> getNeither() {
            return neither;
        }

        public String getYesEmoji() {
            return yesEmoji;
        }

        public String getNoEmoji() {
            return noEmoji;
        }
    }
}
