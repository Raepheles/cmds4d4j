package com.raepheles.discord.cmds4d4j;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    // Command keyword
    private String keyword;
    // Command module (For grouping commands)
    private String module;
    // Command description
    private String description;
    // Command usage
    private String usage;
    // Required permissions to use the command
    private List<Permissions> requiredPerms;
    // Aliases for the command
    private List<String> aliases;
    // Whether or not command should use preExecute method set at CommandManager. Default is true.
    private boolean preExecute;
    // Allow direct message. Default true.
    private boolean allowPm;

    // Executes command
    public abstract void execute(MessageReceivedEvent event);
    // Executes before the preExecute in CommandManager
    public void preExecute() {
        // Unique preExecute for the command
        // This method will execute before preExecute in CommandManager
    }

    /**
     * Command.
     * @param keyword Command keyword.
     * @param module Command module used for grouping commands.
     * @param description Command description.
     * @param usage Command usage.
     */
    public Command(String keyword, String module, String description, String usage) {
        this.keyword = keyword;
        this.module = module;
        this.description = description;
        this.usage = usage;
        this.requiredPerms = new ArrayList<>();
        this.preExecute = true;
        this.aliases = new ArrayList<>();
        this.allowPm = true;
    }

    /**
     * Command.
     * @param keyword Command keyword.
     * @param module Command module used for grouping commands.
     * @param description Command description.
     * @param usage Command usage.
     * @param preExecute When set to false preExecute method at CommandManager won't execute before this command.
     */
    public Command(String keyword, String module, String description, String usage, boolean preExecute) {
        this.keyword = keyword;
        this.module = module;
        this.description = description;
        this.usage = usage;
        this.requiredPerms = new ArrayList<>();
        this.preExecute = preExecute;
        this.aliases = new ArrayList<>();
        this.allowPm = true;
    }

    /**
     * Command.
     * @param keyword Command keyword.
     * @param module Command module used for grouping commands.
     * @param description Command description.
     * @param usage Command usage.
     * @param requiredPerms Required permissions for users to use this command.
     */
    public Command(String keyword, String module, String description, String usage, List<Permissions> requiredPerms) {
        this.keyword = keyword;
        this.module = module;
        this.description = description;
        this.usage = usage;
        this.requiredPerms = requiredPerms;
        this.preExecute = true;
        this.aliases = new ArrayList<>();
        this.allowPm = true;
    }

    /**
     * Command.
     * @param keyword Command keyword.
     * @param module Command module used for grouping commands.
     * @param description Command description.
     * @param usage Command usage.
     * @param requiredPerms Required permissions for users to use this command.
     * @param preExecute When set to false preExecute method at CommandManager won't execute before this command.
     */
    public Command(String keyword, String module, String description, String usage, List<Permissions> requiredPerms, boolean preExecute) {
        this.keyword = keyword;
        this.module= module;
        this.description = description;
        this.usage = usage;
        this.requiredPerms = requiredPerms;
        this.preExecute = preExecute;
        this.aliases = new ArrayList<>();
        this.allowPm = true;
    }

    /**
     * Returns true if command will execute the preExecute method in CommandManager before the command.
     * @return preExecute
     */
    public boolean willPreExecute() {
        return preExecute;
    }

    /**
     * Returns command keyword.
     * @return keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Returns command module.
     * @return module
     */
    public String getModule() {
        return module;
    }

    /**
     * Returns command description.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns command usage.
     * @return usage
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Returns the list of Permissions required to execute command.
     * @return requiredPerms
     */
    public List<Permissions> getRequiredPerms() {
        return requiredPerms;
    }

    public void addAlias(String alias) {
        aliases.add(alias);
    }

    public List<String> getAliases() {
        return aliases;
    }

    public boolean isAllowPm() {
        return allowPm;
    }

    public void setAllowPm(boolean allowPm) {
        this.allowPm = allowPm;
    }
}
