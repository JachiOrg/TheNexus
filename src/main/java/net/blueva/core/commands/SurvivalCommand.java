package net.blueva.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.blueva.core.Main;
import net.blueva.core.utils.MessagesUtil;

public class SurvivalCommand implements CommandExecutor {

    private Main main;

    public SurvivalCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player) && args.length != 1) {
            sender.sendMessage(MessagesUtil.format(null, main.configManager.getLang().getString("messages.other.use_survival_command")));
            return true;
        }

        Player target;
        if (args.length == 1) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(MessagesUtil.format((Player) sender, main.configManager.getLang().getString("messages.error.player_offline")));
                return true;
            }
            if (!sender.hasPermission("bluecore.gamemode.survival.others") || !sender.hasPermission("bluecore.gamemode.*") || !sender.hasPermission("bluecore.*")) {
                sender.sendMessage(MessagesUtil.format((Player) sender, main.configManager.getLang().getString("messages.error.no_perms")));
                return true;
            }
        } else {
            target = (Player) sender;
            if (!sender.hasPermission("bluecore.gamemode.survival") || !sender.hasPermission("bluecore.gamemode.*") || !sender.hasPermission("bluecore.*")) {
                sender.sendMessage(MessagesUtil.format(target, main.configManager.getLang().getString("messages.error.no_perms")));
                return true;
            }
        }

        target.setGameMode(GameMode.SURVIVAL);
        target.sendMessage(MessagesUtil.format(target, main.configManager.getLang().getString("messages.success.gamemode_changed").replace("%gamemode%", "SURVIVAL")));
        if (args.length == 1) {
            sender.sendMessage(MessagesUtil.format(target, main.configManager.getLang().getString("messages.success.gamemode_changed_others").replace("%gamemode%", "SURVIVAL").replace("%player%", target.getName())));
        }

        return true;
    }
}