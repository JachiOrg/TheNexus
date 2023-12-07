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
import net.blueva.core.managers.EconomyManager;
import net.blueva.core.utils.MessagesUtil;
import net.blueva.core.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class PayCommand implements CommandExecutor {

    private final Main main;

    public PayCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessagesUtil.format(null, ConfigManager.language.getString("messages.other.only_player")));
            return true;
        }

        if (args.length <= 1) {
            sender.sendMessage(MessagesUtil.format((Player) sender, ConfigManager.language.getString("messages.other.use_pay_command")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(MessagesUtil.format((Player) sender, ConfigManager.language.getString("messages.error.player_offline")));
            return true;
        }

        double money = 0.0;
        if(StringUtil.isNumber(args[1])) {
            money = Double.parseDouble(args[1]);
        } else {
            sender.sendMessage(MessagesUtil.format((Player) sender, ConfigManager.language.getString("messages.other.use_pay_command")));
            return true;
        }

        if (!sender.hasPermission("bluecore.pay")) {
            sender.sendMessage(MessagesUtil.format((Player) sender, ConfigManager.language.getString("messages.error.no_perms")));
            return true;
        }

        if(EconomyManager.balancePlayer((Player) sender, main) >= money) {
            try {
                EconomyManager.withdrawMoney((Player) sender, money, main);
                EconomyManager.depositMoney(target, money, main);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sender.sendMessage(MessagesUtil.format((Player) sender, Objects.requireNonNull(ConfigManager.language.getString("messages.success.money_paid"))
                    .replace("{amount}", String.valueOf(money))));
            target.sendMessage(MessagesUtil.format(target, Objects.requireNonNull(ConfigManager.language.getString("messages.success.money_received"))
                    .replace("{amount}", String.valueOf(money))));
        } else {
            sender.sendMessage(MessagesUtil.format((Player) sender, ConfigManager.language.getString("messages.error.insufficient_money")));
        }

        return true;
    }
}
