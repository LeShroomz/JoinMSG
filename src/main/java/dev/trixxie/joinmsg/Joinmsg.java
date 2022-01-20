package dev.trixxie.joinmsg;

import com.tchristofferson.configupdater.ConfigUpdater;
import dev.trixxie.joinmsg.Commands.generalCommand;
import dev.trixxie.joinmsg.Events.PlayerConnections;
import dev.trixxie.joinmsg.Utils.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public final class Joinmsg extends JavaPlugin {

    private static Joinmsg plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");
        try {
            ConfigUpdater.update(plugin, "config.yml", configFile, Arrays.asList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadConfig();
        Metrics metrics = new Metrics(this);

        getServer().getPluginManager().registerEvents(new PlayerConnections(), this);

        //COMMANDS
        this.getCommand("joinmsg").setExecutor(new generalCommand());

    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public static Joinmsg get(){
        return plugin;
    }
}
