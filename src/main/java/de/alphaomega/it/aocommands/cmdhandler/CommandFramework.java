package de.alphaomega.it.aocommands.cmdhandler;


import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.msghandler.Message;
import lombok.NonNull;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;


public class CommandFramework implements CommandExecutor {

    private final Map<String, Entry<Method, Object>> commandMap = new HashMap<>();
    private final AOCommands aoCommands;
    private CommandMap map;


    public CommandFramework(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
        if (this.aoCommands.getServer().getPluginManager() instanceof SimplePluginManager manager) {
            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                this.map = (CommandMap) field.get(manager);
            } catch (SecurityException | IllegalAccessException | NoSuchFieldException | IllegalArgumentException exc) {
                exc.printStackTrace();
            }
        }
        registerHelp();
    }

    public boolean onCommand(final @NonNull CommandSender sender, final @NonNull Command cmd, final @NonNull String label, final @NonNull String[] args) {
        return this.handleCommand(sender, cmd, label, args);
    }

    public boolean handleCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        for (int i = args.length; i >= 0; --i) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(label.toLowerCase());

            for (int x = 0; x < i; ++x) {
                buffer.append(".").append(args[x].toLowerCase());
            }

            String cmdLabel = buffer.toString();
            if (this.commandMap.containsKey(cmdLabel)) {
                Method method = (Method) ((Entry<?, ?>) this.commandMap.get(cmdLabel)).getKey();
                Object methodObject = this.commandMap.get(cmdLabel).getValue();
                de.alphaomega.it.aocommands.cmdhandler.Command command = method.getAnnotation(de.alphaomega.it.aocommands.cmdhandler.Command.class);
                if (sender instanceof Player p) {
                    if (!command.permission().equals("") && !sender.hasPermission(command.permission()) && !sender.isOp()) {
                        final Message msg = new Message(p);
                        if (p.locale().toString().equals("de_DE")) {
                            sender.sendMessage(MiniMessage.miniMessage().deserialize(msg.showMessage("prefix", false, false) + this.aoCommands.getNoPermsMessage().get("de_DE")));
                        } else {
                            sender.sendMessage(MiniMessage.miniMessage().deserialize(msg.showMessage("prefix", false, false) + this.aoCommands.getNoPermsMessage().get("en_US")));
                        }
                        return true;
                    }
                    try {
                        method.invoke(methodObject, new CommandArgs(sender, cmd, label, args, cmdLabel.split("\\.").length - 1));
                    } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException exc) {
                        exc.printStackTrace();
                    }
                    return true;
                } else if (sender instanceof ConsoleCommandSender console) {
                    if (command.inGameOnly()) {
                        console.sendMessage("This command is only performable in game");
                        return true;
                    }
                    try {
                        method.invoke(methodObject, new CommandArgs(console, cmd, label, args, cmdLabel.split("\\.").length - 1));
                    } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException exc) {
                        exc.printStackTrace();
                    }
                    return true;
                }
            }
        }

        this.defaultCommand(new CommandArgs(sender, cmd, label, args, 0));
        return true;
    }

    public void registerCommands(Object obj) {
        Method[] methods = obj.getClass().getMethods();

        for (Method m : methods) {
            String[] strings;
            int length;
            int i;
            String alias;
            if (m.getAnnotation(de.alphaomega.it.aocommands.cmdhandler.Command.class) != null) {
                de.alphaomega.it.aocommands.cmdhandler.Command command = m.getAnnotation(de.alphaomega.it.aocommands.cmdhandler.Command.class);
                if (m.getParameterTypes().length <= 1 && m.getParameterTypes()[0] == CommandArgs.class) {
                    this.registerCommand(command, command.name(), m, obj);
                    strings = command.aliases();
                    length = strings.length;

                    for (i = 0; i < length; ++i) {
                        alias = strings[i];
                        this.registerCommand(command, alias, m, obj);
                    }
                } else {
                    System.out.println("Unable to register command " + m.getName() + ". Unexpected method arguments");
                }
            } else if (m.getAnnotation(Completer.class) != null) {
                Completer comp = m.getAnnotation(Completer.class);
                if (m.getParameterTypes().length == 1 && m.getParameterTypes()[0] == CommandArgs.class) {
                    if (m.getReturnType() != List.class) {
                        System.out.println("Unable to register tab completer " + m.getName() + ". Unexpected return type");
                    } else {
                        this.registerCompleter(comp.name(), m, obj);
                        strings = comp.aliases();
                        length = strings.length;

                        for (i = 0; i < length; ++i) {
                            alias = strings[i];
                            this.registerCompleter(alias, m, obj);
                        }
                    }
                } else {
                    System.out.println("Unable to register tab completer " + m.getName() + ". Unexpected method arguments");
                }
            }
        }

    }

    public void registerHelp() {
        Set<HelpTopic> help = new TreeSet<>(HelpTopicComparator.helpTopicComparatorInstance());

        for (String s : this.commandMap.keySet()) {
            if (!s.contains(".")) {
                Command cmd = this.map.getCommand(s);
                if (cmd != null) {
                    HelpTopic topic = new GenericCommandHelpTopic(cmd);
                    help.add(topic);
                }
            }
        }

        IndexHelpTopic topic = new IndexHelpTopic(this.aoCommands.getName(), "All commands for " + this.aoCommands.getName(), null, help, "Below is a list of all " + this.aoCommands.getName() + " commands:");
        Bukkit.getServer().getHelpMap().addTopic(topic);
    }

    public void registerCommand(de.alphaomega.it.aocommands.cmdhandler.Command command, String label, Method m, Object obj) {
        this.commandMap.put(label.toLowerCase(), new SimpleEntry<>(m, obj));
        this.commandMap.put(this.aoCommands.getName() + ':' + label.toLowerCase(), new SimpleEntry<>(m, obj));
        String cmdLabel = label.split("\\.")[0].toLowerCase();
        if (this.map.getCommand(cmdLabel) == null) {
            Command cmd = new BukkitCommand(cmdLabel, this, this.aoCommands);
            this.map.register(this.aoCommands.getName(), cmd);
        }

        if (!command.description().equalsIgnoreCase("") && cmdLabel.equals(label)) {
            this.map.getCommand(cmdLabel).setDescription(command.description());
        }

        if (!command.usage().equalsIgnoreCase("") && cmdLabel.equals(label)) {
            this.map.getCommand(cmdLabel).setUsage(command.usage());
        }

    }

    public void registerCompleter(String label, Method m, Object obj) {
        String cmdLabel = label.split("\\.")[0].toLowerCase();
        BukkitCommand bCommand;
        if (this.map.getCommand(cmdLabel) == null) {
            bCommand = new BukkitCommand(cmdLabel, this, this.aoCommands);
            this.map.register(this.aoCommands.getName(), bCommand);
        }

        if (this.map.getCommand(cmdLabel) instanceof BukkitCommand) {
            bCommand = (BukkitCommand) this.map.getCommand(cmdLabel);
            if (bCommand.completer == null) {
                bCommand.completer = new BukkitCompleter();
            }

            bCommand.completer.addCompleter(label, m, obj);
        } else if (this.map.getCommand(cmdLabel) instanceof PluginCommand) {
            try {
                Object command = this.map.getCommand(cmdLabel);
                Field field = command.getClass().getDeclaredField("completer");
                field.setAccessible(true);
                BukkitCompleter completer;
                if (field.get(command) == null) {
                    completer = new BukkitCompleter();
                    completer.addCompleter(label, m, obj);
                    field.set(command, completer);
                } else if (field.get(command) instanceof BukkitCompleter) {
                    completer = (BukkitCompleter) field.get(command);
                    completer.addCompleter(label, m, obj);
                } else {
                    System.out.println("Unable to register tab completer " + m.getName() + ". A tab completer is already registered for that command!");
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }

    }

    private void defaultCommand(CommandArgs args) {
        args.getSender().sendMessage(args.getLabel() + " is not handled! Oh noes!");
    }

}