package fr.askyna.bellabot.cron;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CronScheduler {

    private ScheduledExecutorService scheduler;

    public CronScheduler() {
        // Initialiser le ScheduledExecutorService avec un pool de threads
        scheduler = Executors.newScheduledThreadPool(1);
    }

    public void scheduleCronTask(Cron task, long initialDelay, long period, TimeUnit unit) {
        // Planifier l'exécution périodique de la tâche
        scheduler.scheduleAtFixedRate(() -> task.run(), initialDelay, period, unit);
    }

    public void shutdown() {
        // Arrêter le scheduler si nécessaire
        scheduler.shutdown();
    }

    public static void main(String[] args) {




    }
}
