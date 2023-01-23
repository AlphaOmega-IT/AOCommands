package de.alphaomega.it.aocommands.commands;

import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.cmdhandler.Command;
import de.alphaomega.it.aocommands.cmdhandler.CommandArgs;
import de.alphaomega.it.aocommands.cmdhandler.Completer;
import de.alphaomega.it.aocommands.msghandler.Message;
import de.alphaomega.it.aocommands.utils.InputCheck;
import de.alphaomega.it.aocommands.utils.ItemEditor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemEdit {

    private final AOCommands aoCommands;

    public ItemEdit(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @Command(
            name = "itemedit",
            aliases = {"iedit", "editi", "edititem"},
            permission = "aocommands.itemedit"
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(player);

        if (args.length < 2) {
            msg.sendMessage("itemedit-syntax", false, true);
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand().clone();
        if (item.getType().isAir()) {
            msg.sendMessage("noValidItemInHand-itemedit", false, true);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "name", "displayname" -> {
                switch (args[1].toLowerCase()) {
                    case "edit" ->
                            updatePlayersItem(new ItemEditor(item).setName(String.join(" ", Arrays.copyOfRange(args, 2, args.length))).build(), player);
                    case "editrainbow" ->
                            updatePlayersItem(new ItemEditor(item).setName("<rainbow>" + String.join(" ", Arrays.copyOfRange(args, 2, args.length)) + "</rainbow>").build(), player);
                }
            }
            case "lore", "line", "lores", "lines" -> {
                switch (args[1].toLowerCase()) {
                    case "add", "new", "create" ->
                            updatePlayersItem(new ItemEditor(item).addLoreLine(String.join(" ", Arrays.copyOfRange(args, 2, args.length))).build(), player);
                    case "remove", "delete" -> {
                        if (args.length < 3) {
                            msg.sendMessage("itemedit-syntax-lore", false, true);
                            return;
                        }
                        if (InputCheck.isFullNumber(args[2]))
                            updatePlayersItem(new ItemEditor(item).removeLoreLine(Integer.parseInt(args[2])).build(), player);
                        else
                            msg.sendMessage("noValidInputNumber-itemedit", false, true);
                    }
                    case "clear" -> updatePlayersItem(new ItemEditor(item).removeLore().build(), player);
                }
            }
            case "amount", "setamount", "count" -> {
                if (InputCheck.isFullNumber(args[1]))
                    updatePlayersItem(new ItemEditor(item).setAmount(Math.min(Integer.parseInt(args[1]), 64)).build(), player);
                else
                    msg.sendMessage("noValidInputNumber-itemedit", false, true);
            }
            case "enchant", "ench", "enchantment" -> {
                switch (args[1].toLowerCase()) {
                    case "add", "new", "create" -> {
                        Enchantment e = Enchantment.getByKey(NamespacedKey.minecraft(args[2].replaceAll(" ", "_").toLowerCase()));
                        if (e == null) {
                            msg.setArgs(List.of(args[2]));
                            msg.sendMessage("enchantNotValid", true, true);
                            return;
                        }

                        if (args.length == 3 || InputCheck.isFullNumber(args[3]))
                            updatePlayersItem(new ItemEditor(item).addEnchantment(e, args.length == 3 ? 1 : Integer.parseInt(args[3])).build(), player);
                        else
                            msg.sendMessage("noValidInputNumber-itemedit", false, true);
                    }
                    case "remove", "delete" -> {
                        Enchantment e = Enchantment.getByKey(NamespacedKey.minecraft(args[2].replaceAll(" ", "_").toLowerCase()));
                        if (e == null) {
                            msg.setArgs(List.of(args[2]));
                            msg.sendMessage("enchantNotValid", true, true);
                            return;
                        }

                        updatePlayersItem(new ItemEditor(item).removeEnchantment(e).build(), player);
                    }
                    case "adjust", "change", "edit" -> {
                        if (args.length < 3) {
                            msg.sendMessage("itemedit-syntax-enchant", false, true);
                            return;
                        }

                        Enchantment e = Enchantment.getByKey(NamespacedKey.minecraft(args[2].replaceAll(" ", "_").toLowerCase()));
                        if (e == null) {
                            msg.setArgs(List.of(args[2]));
                            msg.sendMessage("enchantNotValid", true, true);
                            return;
                        }

                        if (InputCheck.isFullNumber(args[3]))
                            updatePlayersItem(new ItemEditor(item).adjustEnchantment(e, Integer.parseInt(args[3])).build(), player);
                        else
                            msg.sendMessage("noValidInputNumber-itemedit", false, true);
                    }
                }
            }
        }
    }

    @Completer(
            name = "itemedit",
            aliases = {"iedit", "editi", "edititem"}
    )
    public List<String> onTabComplete(final CommandArgs arg) {
        final String[] args = arg.getArgs();
        final List<String> completer = new ArrayList<>();
        final Player p = arg.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (args.length == 1) {
            completer.addAll(List.of("amount", "enchant", "lore", "name"));
            return completer;
        }

        if (args.length == 2) {
            if (List.of("name", "displayname").contains(args[0].toLowerCase())) {
                completer.addAll(List.of("edit", "editrainbow"));
                return completer;
            }

            if (List.of("lore", "line", "lines", "lores").contains(args[0].toLowerCase())) {
                completer.addAll(List.of("create", "clear", "delete"));
                return completer;
            }

            if (List.of("enchant", "ench", "enchantment").contains(args[0].toLowerCase())) {
                completer.addAll(List.of("add", "edit", "remove"));
                return completer;
            }
            return completer;
        }

        if (args.length == 3) {
            if (List.of("lore", "line", "lines", "lores").contains(args[0].toLowerCase()) && List.of("delete", "remove").contains(args[1].toLowerCase())) {
                if (item.lore() == null || item.lore().isEmpty()) return completer;
                for (int i = 0; i < item.lore().size(); i++) {
                    completer.add(String.valueOf(i));
                }
            } else if (List.of("enchant", "ench", "enchantment").contains(args[0].toLowerCase())) {
                if (List.of("remove", "delete").contains(args[1].toLowerCase())) {
                    if (item.getEnchantments().isEmpty()) return completer;
                    completer.addAll(item.getEnchantments().keySet().stream().map(Enchantment::getKey).map(NamespacedKey::getKey).toList());
                } else if (List.of("add", "new", "create").contains(args[1].toLowerCase())) {
                    for (Enchantment e : Enchantment.values()) {
                        if (item.containsEnchantment(e)) continue;
                        if (!this.aoCommands.getBaseConfig().getBoolean("allow_not_valid_enchantments"))
                            if (!e.canEnchantItem(item)) continue;
                        completer.add(e.getKey().getKey());
                    }
                } else if (List.of("adjust", "edit", "change", "set").contains(args[1].toLowerCase())) {
                    completer.addAll(item.getEnchantments().keySet().stream().map(Enchantment::getKey).map(NamespacedKey::getKey).toList());
                }
            }
            return completer;
        }

        if (args.length == 4) {
            if (List.of("enchant", "ench", "enchantment").contains(args[0].toLowerCase())) {
                if (List.of("add", "create", "new", "adjust", "edit", "change", "set").contains(args[1].toLowerCase())) {
                    Enchantment e = Enchantment.getByKey(NamespacedKey.minecraft(args[2].toLowerCase()));
                    if (e == null) return completer;
                    for (int i = 0; i < e.getMaxLevel(); i++) {
                        completer.add(String.valueOf(i));
                    }
                    return completer;
                }
            }
        }
        return completer;
    }

    private void updatePlayersItem(final ItemStack item, final Player p) {
        p.getInventory().setItemInMainHand(item);
        p.updateInventory();
    }
}
