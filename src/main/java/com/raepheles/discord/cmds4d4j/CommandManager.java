package com.raepheles.discord.cmds4d4j;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;

import java.util.*;
import java.util.stream.Collectors;

public class CommandManager {

    private List<Command> commands;
    private String defaultPrefix;
    private Map<IGuild, String> mapPrefix;
    private IDiscordClient client;
    private Logger logger = LoggerFactory.getLogger(CommandManager.class);

    /**
     * This method will execute before each command that's willPreExecute value is true.
     * Override this method while initializing CommandManager if you want to execute
     * certain method before every command.
     * @param command Command that will execute after this method.
     */
    protected void preExecute(Command command) {

    }

    /**
     * Initializes Command API.
     * @param packageName Package directory that contains your commands.
     * @param defaultPrefix Default prefix to use when guild doesn't have unique prefix.
     */
    public CommandManager(IDiscordClient client, String packageName, String defaultPrefix) {
        // Load commands
        logger.info("Loading commands");
        Reflections r = new Reflections(packageName);
        commands = r.getSubTypesOf(Command.class).stream().map(c -> {
            try {
                return c.getDeclaredConstructor().newInstance();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if(defaultPrefix.length() == 0) {
            logger.error("Prefix cannot be empty.");
            throw new IllegalArgumentException("Default prefix cannot be empty.");
        } else if(defaultPrefix.length() > 3) {
            logger.error("Prefix length cannot be greater than 3.");
            throw new IllegalArgumentException("Default prefix's length cannot be greater than 3.");
        } else if(defaultPrefix.contains(" ")) {
            logger.error("Prefix cannot contain space");
            throw new IllegalArgumentException("Default prefix cannot contain space");
        }
        this.defaultPrefix = defaultPrefix;
        mapPrefix = new HashMap<>();
        this.client = client;

        // Register command listener
        getClient().getDispatcher().registerListener(new CommandListener(this));
        getClient().getDispatcher().registerListener(new CommandHelp(this));
    }

    public List<Command> getCommands() {
        return commands;
    }

    public String getDefaultPrefix() {
        return defaultPrefix;
    }

    public Map<IGuild, String> getMapPrefix() {
        return mapPrefix;
    }

    public IDiscordClient getClient() {
        return client;
    }

}
