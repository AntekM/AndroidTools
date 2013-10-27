package com.mysliborski.tools.async;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.mysliborski.tools.helper.LogHelper.log;

/**
 * Created by antonimysliborski on 25/09/2013.
 */
public class EventBusBase<E> {

    private Map<E, Set<ObserverReference>> observers = new HashMap<E, Set<ObserverReference>>();

    private Set<E> persistentEvents = new HashSet<E>();

    private class ObserverReference extends WeakReference<EventObserver<E>> {

        public ObserverReference(EventObserver<E> r) {
            super(r);
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof WeakReference) && get() != null && get().equals(((ObserverReference) o).get());
        }

        @Override
        public int hashCode() {
            if (get() != null)
                return get().hashCode();
            else
                return 0;
        }
    }

    public synchronized void dispatchEvent(E event) {
        Set<ObserverReference> observers = this.observers.get(event);
        if (observers != null) {
            for (ObserverReference observer : new ArrayList<ObserverReference>(observers)) {
                if (observer.get() != null) {
                    log("Dispatching event: ", event, " for observer: ", observer.get());
                    observer.get().onEvent(event);
                } else
                    observers.remove(observer);
            }
        }
    }

    /**
     * Dispatch one-time event (EventBus will save it) - such as data loaded
     *
     * @param event
     */
    public synchronized void dispatchPersistentEvent(E event) {
        log("Dispatching persistent event: ", event);
        persistentEvents.add(event);
        dispatchEvent(event);
        observers.put(event, null);
    }

    public synchronized void clearPersistentEvent(E event) {
        persistentEvents.remove(event);
    }

    /**
     * Waits for one-time event. If event has already happened, will fire immediately
     *
     * @param event
     * @param observer
     */
    public synchronized void waitFor(E event, EventObserver<E> observer) {
        log("Observer: " + observer + " waiting for: ", event);
        if (persistentEvents.contains(event)) {
            log("Event was ready: ", event);
            observer.onEvent(event);
        } else {
            log("Registering for event: ", event);
            registerObserver(event, observer);
        }
    }

    public synchronized void registerObserver(E event, EventObserver<E> observer) {
        Set<ObserverReference> eventTypeObservers = this.observers.get(event);
        if (eventTypeObservers == null) {
            eventTypeObservers = new HashSet<ObserverReference>();
            this.observers.put(event, eventTypeObservers);
        }
        eventTypeObservers.add(new ObserverReference(observer));
    }

    public void unregisterObserver(E event, EventObserver<E> observer) {
        Set<ObserverReference> eventTypeObservers = this.observers.get(event);
        if (eventTypeObservers != null) {
            eventTypeObservers.remove(new ObserverReference(observer));
        }
    }

    public void clearAll() {
        this.observers.clear();
        this.persistentEvents.clear();
    }

    public interface EventObserver<E> {
        void onEvent(E event);
    }

}
