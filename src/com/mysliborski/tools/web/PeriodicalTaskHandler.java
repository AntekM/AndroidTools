package com.mysliborski.tools.web;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;

import com.mysliborski.tools.exception.ConnectionProblemException;
import com.mysliborski.tools.exception.ServiceException;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.mysliborski.tools.helper.LogHelper.log;
import static com.mysliborski.tools.helper.LogHelper.logTag;
import static com.mysliborski.tools.helper.LogHelper.loge;

/**
 * Created by antonimysliborski on 17/10/2013.
 */
public abstract class PeriodicalTaskHandler implements NetworkStateReceiver.NetworkStateObserver {

    private static final int INIT_TIME_AFTER_ERROR = 15;

    protected ConnectivityManager connectivityManager;

    ScheduledThreadPoolExecutor executor;
    ScheduledFuture<?> scheduledTask;
    SynchroniseTask synchronizeTask = new SynchroniseTask();
    private int timeAfterError = INIT_TIME_AFTER_ERROR;
    private Context context;

    protected abstract boolean periodicalWork() throws ServiceException;

    abstract protected String serviceName();

    /**
     * After how many seconds the tast will be repeated. -1 for one time only
     *
     * @return
     */
    protected abstract int standardInterval();

    private void registerForNetworkNotification() {
        log("SERVICE: ", serviceName() + ": Registering for network notification");
        ComponentName receiver = new ComponentName(context, NetworkStateReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void connectionRestored() {
        log("SERVICE: ", serviceName() + ": Connection restored");
        ComponentName receiver = new ComponentName(context, NetworkStateReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        reschedule(0);
    }

    protected void reschedule(int newTimeout) {
        if (executor != null) {
            if (scheduledTask != null) {
                scheduledTask.cancel(false);
            }
            scheduledTask = executor.schedule(synchronizeTask, newTimeout, TimeUnit.SECONDS);
        }
    }

    public void cancel() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
    }

    /**
     * Should be called only once, at the start of the application (after login)
     */
    public void init(Context context) {
        this.context = context;
        log("SERVICE: ", "Initializing Service: " + serviceName());
        connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        executor = new ScheduledThreadPoolExecutor(1);
        executor.execute(synchronizeTask);
        NetworkStateReceiver.NetworkStateNotifier.registerObserver(this);
    }

    void handleError(ServiceException e) {
        if (e != null) {
            loge(e, "Error in periodical task");
        } else
            log("SERVICE: ", "Periodical operation in " + this.getClass().getCanonicalName() + " resulted in false");
        if (e instanceof ConnectionProblemException) {
            if (timeAfterError == 2 * INIT_TIME_AFTER_ERROR) {
                registerForNetworkNotification();
                timeAfterError = INIT_TIME_AFTER_ERROR;
                return;
            }
        }
        reschedule(timeAfterError);
        timeAfterError *= 2;
    }

    public void forceNow() {
        log("Forcing now for ", serviceName());
        synchronizeTask.run();
//        reschedule(standardInterval());
    }

    class SynchroniseTask implements Runnable {

        private int errorCount = 0;

        @Override
        public void run() {
            try {
                if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()) {
                    int interval = standardInterval();
                    if (periodicalWork()) {
                        if (interval > 0) {
                            log("SERVICE: ", "Operation successful. Rescheduling task for " + serviceName());
                            timeAfterError = INIT_TIME_AFTER_ERROR;
                            reschedule(interval);
                        } else {
                            log("SERVICE: ", "Operation successful for " + serviceName() + ", finishing.");
                        }
                    }
                } else {
                    handleError(new ConnectionProblemException());
                }
            } catch (ServiceException e) {
                handleError(e);
            }
        }
    }


}
