package de.alphaomega.it.commands;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.cmdHandler.Completer;
import de.alphaomega.it.msgHandler.Message;
import de.alphaomega.it.utils.InputCheck;
import de.alphaomega.it.utils.ItemEditor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record ItemEdit(AOCommands pl) {

    @Command(
            name = "itemedit",
            aliases = {"iedit", "editi", "edititem"},
            permission = "aocommands.itemedit"
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(p);

        if (args.length < 2) {
            msg.sendMessage("itemedit-syntax", false, true);
            return;
        }

        ItemStack iS = p.getInventory().getItemInMainHand().clone();
        if (iS.getType().isAir()) {
            msg.sendMessage("noValidItemInHand-itemedit", false, true);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "name", "displayname" -> {
                switch (args[1].toLowerCase()) {
                    case "edit" -> updatePlayersItem(new ItemEditor(iS).setName(String.join(" ", Arrays.copyOfRange(args, 2, args.length))).build(), p);
                    case "editrainbow" -> updatePlayersItem(new ItemEditor(iS).setName("<rainbow>" + String.join(" ", Arrays.copyOfRange(args, 2, args.length)) + "</rainbow>").build(), p);
                }
            }
            case "lore", "line", "lores", "lines" -> {
                switch (args[1].toLowerCase()) {
                    case "add", "new", "create" -> updatePlayersItem(new ItemEditor(iS).addLoreLine(String.join(" ", Arrays.copyOfRange(args, 2, args.length))).build(), p);
                    case "remove", "delete" -> {
                        if (args.length < 3) {
                            msg.sendMessage("itemedit-syntax-lore", false, true);
                            return;
                        }
                        if (InputCheck.isFullNumber(args[2]))
                            updatePlayersItem(new ItemEditor(iS).removeLoreLine(Integer.parseInt(args[2])).build(), p);
                        else
                            msg.sendMessage("noValidInputNumber-itemedit", false, true);
                    }
                    case "clear" -> updatePlayersItem(new ItemEditor(iS).removeLore().build(), p);
                }
            }
            case "amount", "setamount", "count" -> {
                if (InputCheck.isFullNumber(args[1]))
                    updatePlayersItem(new ItemEditor(iS).setAmount(Math.min(Integer.parseInt(args[1]), 64)).build(), p);
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
                            updatePlayersItem(new ItemEditor(iS).addEnchantment(e, args.length == 3 ? 1 : Integer.parseInt(args[3])).build(), p);
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

                        updatePlayersItem(new ItemEditor(iS).removeEnchantment(e).build(), p);
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
                            updatePlayersItem(new ItemEditor(iS).adjustEnchantment(e, Integer.parseInt(args[3])).build(), p);
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
        ItemStack iS = p.getInventory().getItemInMainHand();

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
                if (iS.lore() == null || iS.lore().isEmpty()) return completer;
                for (int i = 0; i < iS.lore().size(); i++) {
                    completer.add(String.valueOf(i));
                }
            } else if (List.of("enchant", "ench", "enchantment").contains(args[0].toLowerCase())) {
                if (List.of("remove", "delete").contains(args[1].toLowerCase())) {
                    if (iS.getEnchantments().isEmpty()) return completer;
                    completer.addAll(iS.getEnchantments().keySet().stream().map(Enchantment::getKey).map(NamespacedKey::getKey).toList());
                } else if (List.of("add", "new", "create").contains(args[1].toLowerCase())) {
                    for (Enchantment e : Enchantment.values()) {
                        if (iS.containsEnchantment(e)) continue;
                        if (!pl.getBaseConfig().getBoolean("allow_not_valid_enchantments"))
                            if (!e.canEnchantItem(iS)) continue;
                        completer.add(e.getKey().getKey());
                    }
                } else if (List.of("adjust", "edit", "change", "set").contains(args[1].toLowerCase())) {
                    completer.addAll(iS.getEnchantments().keySet().stream().map(Enchantment::getKey).map(NamespacedKey::getKey).toList());
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

    private void updatePlayersItem(final ItemStack iS, final Player p) {
        p.getInventory().setItemInMainHand(iS);
        p.updateInventory();
    }
}
