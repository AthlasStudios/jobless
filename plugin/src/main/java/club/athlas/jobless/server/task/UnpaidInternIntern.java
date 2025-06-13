package club.athlas.jobless.server.task;

import club.athlas.jobless.Jobless;
import club.athlas.jobless.unemployementcfg.FearSettingsManager;
import club.athlas.jobless.unemployementcfg.constantfear.UnemploymentSetting;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class UnpaidInternIntern extends BukkitRunnable {

    private final Jobless plugin;

    public UnpaidInternIntern(Jobless plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        String sourceUrl = plugin.getFearSettingsManager().getString(UnemploymentSetting.EXTERNAL_WORDS_SOURCE, "");

        if (sourceUrl == null || sourceUrl.isEmpty()) return;

        plugin.getLogger().log(Level.FINE, "Sniffing the corporate void at " + sourceUrl);

        plugin.getUnpaidInternDownloader().scrapeJobTrauma(sourceUrl).whenComplete((stuff, throwable) -> {
            if (throwable != null) {
                plugin.getLogger().log(Level.SEVERE, "The inter tripped over a resume pile", throwable);
                return;
            }

            plugin.getLogger().log(Level.FINE, "Unholy employment terms retrieved from " + sourceUrl);
            plugin.getTheForbiddenWords().summonTheWholeJobFam(stuff);
        });
    }

    public void obeyTheConfigAndHauntPeriodically() {
        FearSettingsManager fearManager = plugin.getFearSettingsManager();

        if (plugin.getFearSettingsManager().getString(UnemploymentSetting.EXTERNAL_WORDS_SOURCE, "").isEmpty()) return;
        if (!fearManager.getBool(UnemploymentSetting.EXTERNAL_WORDS_FETCH_CONTINUOSLY, true)) return;

        long time = fearManager.getLong(UnemploymentSetting.EXTERNAL_WORDS_FETCH_EVERY_TIME, 2);
        TimeUnit timeUnit = fearManager.getEnum(TimeUnit.class, UnemploymentSetting.EXTERNAL_WORDS_FETCH_EVERY_TIME_UNIT, TimeUnit.HOURS);

        runTaskTimer(plugin, 0, timeUnit.toSeconds(time) * 20);
    }

}
