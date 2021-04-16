# O7.A Bot
## How to run?
1. You need to create a new bot application [here](https://discord.com/developers/applications).
2. You need to enable Server Members Intent of the application, so bot can see the list of all members.
3. Copy the token and create a [settings.properties file](#settings).
4. Download the latest release .jar file of the bot.
5. Place the settings.properties file next to the downloaded jar and run `java -jar ./O7A-bot-1.0.0.jar` from the location where both files are

## Settings
- **prefix** is the bot prefix
- **token** is the token you copied in the fourth step in [How to run?](#how-to-run)
- **channel** is the id of the channel where bot will listen for commands
- **voting-channel** is the default channel where voting happens (makes the bot more efficient but will work even if the message is not in this channel)
- **yes** the agree reaction, can be either unicode or emote id
- **no** the disagree reaction, can be either unicode or emote id

### Example properties
```properties
prefix = !
token = ${TOKEN}
channel = 832653538039300096
voting-channel = 831069928970256404
yes = 831077817990119525
no = 831094427887599628
```

## How to contribute
WIP