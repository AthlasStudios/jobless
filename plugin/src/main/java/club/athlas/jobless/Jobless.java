package club.athlas.jobless;

import club.athlas.jobless.api.JoblessAPI;
import club.athlas.jobless.api.trauma.TheForbiddenWords;
import club.athlas.jobless.importedtrauma.UnpaidInternDownloader;
import club.athlas.jobless.server.scarywords.ForbiddenWordsContainer;
import club.athlas.jobless.server.scarywordsdetector.ScaryWordsRemover;
import club.athlas.jobless.server.task.UnpaidInternIntern;
import club.athlas.jobless.unemployementcfg.FearSettingsManager;
import club.athlas.jobless.unemployementcfg.constantfear.UnemploymentSetting;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Jobless extends JavaPlugin implements JoblessAPI {

    private final FearSettingsManager fearSettingsManager = new FearSettingsManager(this);
    private final UnpaidInternDownloader unpaidInternDownloader = new UnpaidInternDownloader(getLogger());

    private BukkitAudiences aScaryAdventure;
    private TheForbiddenWords theForbiddenWords;

    private UnpaidInternIntern unpaidInternIntern;

    @Override
    public void onEnable() {
        fearSettingsManager.load();
        makeTheIntersSayEverything();

        this.aScaryAdventure = BukkitAudiences.create(this);
        this.theForbiddenWords = new ForbiddenWordsContainer(grabTheUserNightmares());
        this.unpaidInternIntern = new UnpaidInternIntern(this);

        unpaidInternIntern.obeyTheConfigAndHauntPeriodically();
        unpaidInternIntern.run(); // RUN ONCE

        Bukkit.getPluginManager().registerEvents(new ScaryWordsRemover(this), this);
        Bukkit.getServicesManager().register(JoblessAPI.class, this, this, ServicePriority.Normal);
    }

    @Override
    public void onDisable() {
        if (unpaidInternIntern != null) unpaidInternIntern.cancel();

        unpaidInternDownloader.shutdown();
    }

    public void reload() {
        fearSettingsManager.reassessLifeChoices();

        if (unpaidInternIntern != null) unpaidInternIntern.cancel();
        unpaidInternIntern = new UnpaidInternIntern(this);

        theForbiddenWords.flushTheSpookyWords();
        theForbiddenWords.summonTheWholeJobFam(grabTheUserNightmares());

        unpaidInternIntern.obeyTheConfigAndHauntPeriodically();
        unpaidInternIntern.run(); // RUN ONCE
    }

    @Override
    public TheForbiddenWords getTheForbiddenWords() {
        return theForbiddenWords;
    }

    public FearSettingsManager getFearSettingsManager() {
        return fearSettingsManager;
    }

    public BukkitAudiences getAScaryAdventure() {
        return aScaryAdventure;
    }

    public UnpaidInternDownloader getUnpaidInternDownloader() {
        return unpaidInternDownloader;
    }

    private @NotNull Set<String> grabTheUserNightmares() {
        return new HashSet<>(getConfig().getStringList(UnemploymentSetting.CUSTOM_WORDS.getPath()));
    }

    private void makeTheIntersSayEverything() {
        if (!fearSettingsManager.getBool(UnemploymentSetting.GENERIC_DEBUG, false)) return;

        Logger logger = getLogger();
        logger.setLevel(Level.FINE);

        for (Handler handler : logger.getHandlers()) {
            handler.setLevel(Level.FINE);
        }
    }

}
