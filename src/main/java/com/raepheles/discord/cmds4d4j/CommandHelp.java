package com.raepheles.discord.cmds4d4j;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class CommandHelp {

    private CommandManager manager;

    CommandHelp(CommandManager manager) {
        this.manager = manager;
    }

    @EventSubscriber
    public void onMessageReceive(MessageReceivedEvent event) {
        String prefix = manager.getMapPrefix().getOrDefault(event.getGuild(), manager.getDefaultPrefix());
        if(event.getMessage().getContent().startsWith(prefix)) {
            String commandWithoutPrefix = event.getMessage().getContent().substring(prefix.length(), event.getMessage().getContent().length());
            String parts[] = commandWithoutPrefix.split(" ", 2);
            String baseCommand = parts[0];
            String moduleArg = "";
            if(parts.length > 1) {
                moduleArg = parts[1];
            }
            String moduleName = moduleArg;
            System.out.println(baseCommand);
            if(baseCommand.equalsIgnoreCase("help")) {
                if(parts.length == 1) {
                    List<String> moduleList = new ArrayList<>();
                    manager.getCommands().forEach(c -> {
                        if(!moduleList.contains(c.getModule())) {
                            moduleList.add(c.getModule());
                        }
                    });
                    StringJoiner sj = new StringJoiner("\n", "**Module List:**\n```\n", "\n```");
                    for(String module: moduleList) {
                        sj.add(String.format("%-50s | Use %shelp %s for module commands.", module, prefix, module));
                    }
                    sendMessage(event.getChannel(), sj.toString());
                } else {
                    List<Command> moduleCommands = manager.getCommands().stream().filter(c -> {
                        return c.getModule().equalsIgnoreCase(moduleName);
                    }).collect(Collectors.toList());
                    StringJoiner sj = new StringJoiner("\n", "Commands for **" + moduleCommands.get(0).getModule() + "** module:\n```\n", "\n```");
                    for(Command command: moduleCommands) {
                        sj.add(String.format("%s%-20s: %-70s", prefix, command.getKeyword(), command.getDescription()));
                    }
                    sendMessage(event.getChannel(), sj.toString());
                }
            }
        }
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
