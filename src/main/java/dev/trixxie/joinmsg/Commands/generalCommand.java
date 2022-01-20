package dev.trixxie.joinmsg.Commands;

import dev.trixxie.joinmsg.Joinmsg;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class generalCommand implements CommandExecutor {
    private Joinmsg plugin = Joinmsg.get();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length > 0){
            if(args[0].equalsIgnoreCase("reload")){
                if(sender instanceof Player){
                    Player p = (Player) sender;
                    if(p.hasPermission("joinmsg.reload")){
                        plugin.reloadConfig();
                        p.sendMessage(ChatColor.GREEN + "JoinMSG config reloaded!");
                    }
                }
            }
        }
        return true;
    }
}