package club.athlas.jobless;

import club.athlas.jobless.api.JoblessAPI;
import club.athlas.jobless.api.trauma.TheForbiddenWords;
import club.athlas.jobless.scarywordsdetector.ScaryWordsRemover;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class Jobless extends JavaPlugin implements JoblessAPI {

    private BukkitAudiences aScaryAdventure;
    private TheForbiddenWords theForbiddenWords;

    @Override
    public void onEnable() {
        // TODO -> LOG MESSAGES

        this.aScaryAdventure = BukkitAudiences.create(this);

        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new ScaryWordsRemover(this), this);
        Bukkit.getServicesManager().register(JoblessAPI.class, this, this, ServicePriority.Normal);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public BukkitAudiences getAScaryAdventure() {
        return aScaryAdventure;
    }

    @Override
    public TheForbiddenWords getTheForbiddenWords() {
        return theForbiddenWords;
    }

}
