/*
 *  ____  _             ____
 * | __ )| |_   _  ___ / ___|___  _ __ ___
 * |  _ \| | | | |/ _ | |   / _ \| '__/ _ \
 * | |_) | | |_| |  __| |__| (_) | | |  __/
 * |____/|_|\__,_|\___|\____\___/|_|  \___|
 *
 * This file is part of Blue Core.
 *
 * Blue Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation.
 *
 * Blue Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License version 3 for more details.
 *
 * Blue Core plugin developed by Blueva Development.
 * Website: https://blueva.net/
 * GitHub repository: https://github.com/BluevaDevelopment/BlueCore
 *
 * Copyright (c) 2023 Blueva Development. All rights reserved.
 */

package net.blueva.core.commands;

import net.blueva.core.Main;
import net.blueva.core.configuration.ConfigManager;
import net.blueva.core.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GamemodeCommand implements CommandExecutor {

    private final Main main;

    public GamemodeCommand(Main main) {
        this.main = main;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (!sender.hasPermission("bluecore.gamemode")) {
            sender.sendMessage(MessagesUtils.format(player, ConfigManager.language.getString("messages.error.no_perms")));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(MessagesUtils.format(player, ConfigManager.language.getString("messages.other.use_gm_command")));
            return true;
        }
        String gamemode = args[0].toLowerCase();
        GameMode mode;
        switch (gamemode) {
            case "survival", "surv", "su", "s" -> {
                if (!sender.hasPermission("bluecore.gamemode.survival")) {
                    sender.sendMessage(MessagesUtils.format(player, ConfigManager.language.getString("messages.error.no_perms")));
                    return true;
                }
                mode = GameMode.SURVIVAL;
            }
            case "creative", "crea", "cr", "c" -> {
                if (!sender.hasPermission("bluecore.gamemode.creative")) {
                    sender.sendMessage(MessagesUtils.format(player, ConfigManager.language.getString("messages.error.no_perms")));
                    return true;
                }
                mode = GameMode.CREATIVE;
            }
            case "adventure", "adven", "adv", "a" -> {
                if (!sender.hasPermission("bluecore.gamemode.adventure")) {
                    sender.sendMessage(MessagesUtils.format(player, ConfigManager.language.getString("messages.error.no_perms")));
                    return true;
                }
                mode = GameMode.ADVENTURE;
            }
            case "spectator", "spect", "spec", "sp" -> {
                if (!sender.hasPermission("bluecore.gamemode.spectator")) {
                    sender.sendMessage(MessagesUtils.format(player, ConfigManager.language.getString("messages.error.no_perms")));
                    return true;
                }
                mode = GameMode.SPECTATOR;
            }
            default -> {
                sender.sendMessage(MessagesUtils.format(player, ConfigManager.language.getString("messages.other.use_gm_command")));
                return true;
            }
        }

        if (!(sender instanceof Player)) {
            if (args.length == 1) {
                sender.sendMessage(MessagesUtils.format(null, ConfigManager.language.getString("messages.other.use_gm_command")));
            }
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    if (mode == GameMode.SURVIVAL) {
                        target.setGameMode(mode);
                        target.sendMessage(MessagesUtils.format(target, Objects.requireNonNull(ConfigManager.language.getString("messages.success.gamemode_changed")).replace("%gamemode%", mode.toString())));
                        sender.sendMessage(MessagesUtils.format(player, ConfigManager.language.getString("messages.success.gamemode_changed_others")).replace("%gamemode%", mode.toString()).replace("%player%", target.getName()));
                    }
                } else {
                    sender.sendMessage(MessagesUtils.format(player, ConfigManager.language.getString("messages.error.player_not_found")).replace("%player%", args[1]));
                }
            }
        } else {
            if (args.length == 1) {
                if (player.hasPermission("bluecore.gamemode." + mode.toString().toLowerCase())) {
                    player.setGameMode(mode);
                    player.sendMessage(MessagesUtils.format(player, Objects.requireNonNull(ConfigManager.language.getString("messages.success.gamemode_changed")).replace("%gamemode%", mode.toString())));
                } else {
                    player.sendMessage(MessagesUtils.format(player, ConfigManager.language.getString("messages.error.no_perms")));
                }
            }
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    if (player.hasPermission("bluecore.gamemode." + mode.toString().toLowerCase() + ".others")) {
                        target.setGameMode(mode);
                        target.sendMessage(MessagesUtils.format(target, Objects.requireNonNull(ConfigManager.language.getString("messages.success.gamemode_changed")).replace("%gamemode%", mode.toString())));
                        player.sendMessage(MessagesUtils.format(player, ConfigManager.language.getString("messages.success.gamemode_changed_others")).replace("%gamemode%", mode.toString()).replace("%player%", target.getName()));
                    } else {
                        player.sendMessage(MessagesUtils.format(player, ConfigManager.language.getString("messages.error.no_perms")));
                    }
                }
                return true;
            }
        }
        return true;
    }
}