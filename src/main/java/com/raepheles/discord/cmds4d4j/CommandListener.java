package com.raepheles.discord.cmds4d4j;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CommandListener {

    private CommandManager manager;

    CommandListener(CommandManager manager) {
        this.manager = manager;
    }

    @EventSubscriber
    public void messageReceived(MessageReceivedEvent event) {
        // Get prefix
        String prefix = manager.getMapPrefix().getOrDefault(event.getGuild(), manager.getDefaultPrefix());

        // If message doesn't start with prefix return
        if(!event.getMessage().getContent().startsWith(prefix))
            return;

        String commandWithoutPrefix = event.getMessage().getContent().substring(prefix.length(), event.getMessage().getContent().length());
        boolean isPm = event.getChannel().isPrivate();
        Command cmd = null;

        for(Command command: manager.getCommands()) {
            // Check command
            if(command.getKeyword().equalsIgnoreCase(commandWithoutPrefix)) {
                cmd = command;
            }
            // Check aliases
            for(String alias: command.getAliases()) {
                if(commandWithoutPrefix.equalsIgnoreCase(alias)) {
                    cmd = command;
                }
            }
            if(cmd != null)
                break;
        }

        // If message isn't a command return
        if(cmd == null)
            return;

        // If private channel and pm isn't allowed return
        if(isPm && !cmd.isAllowPm())
            return;

        // Check permissions if it's not PM
        if(!isPm) {
            boolean hasPerms = true;
            List<Permissions> missingPerms = new ArrayList<>();
            for(Permissions perm: cmd.getRequiredPerms()) {
                if(!event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(perm)) {
                    hasPerms = false;
                    missingPerms.add(perm);
                }
            }
            if(!hasPerms) {
                StringJoiner sj = new StringJoiner(",", "`", "`");
                missingPerms.forEach(perm -> sj.add(perm.name()));
                sendMessage(event.getChannel(),
                        String.format("You need following permissions to use this command: %s", sj.toString()));
                return;
            }
        }

        // Execute the command
        cmd.preExecute();
        if(cmd.willPreExecute()) {
            manager.preExecute(cmd);
        }
        cmd.execute(event);

    }

    private void sendMessage(IChannel channel, String msg) {
        RequestBuffer.request( () -> {
            try {
                channel.sendMessage(msg);
            } catch(DiscordException de) {
                de.printStackTrace();
            }
        });
    }
}
