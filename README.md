# Commands For Discord4J

Simple Command API for Discord4j.

## Seting up the API

```java
IDiscordClient client = new ClientBuilder().withToken(token).build();
CommandManager manager = new CommandManager(client, "com.raepheles.discord.testbot", defaultPrefix);
try {
  client.login();
} catch(DiscordException e) {
  e.printStackTrace();
}
```

## Sample Command

```java
public class PingPongCommand extends Command {
  
  @Override
  public void execute(MessageReceivedEvent event) {
    event.getChannel().sendMessage("Pong!");
  }
  
  public PingPongCommand() {
    super("ping", "Test", "Simple ping-pong command", "ping");
  }
}
```
