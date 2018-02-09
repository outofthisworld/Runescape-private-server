/*
 Project by outofthisworld24
 All rights reserved.
 */

/*
 * Project by outofthisworld24
 * All rights reserved.
 */

/*------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 Project by outofthisworld24
 All rights reserved.
 -----------------------------------------------------------------------------*/

package world.event;


import world.event.impl.Event;

import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * The type Abstract event bus.
 */
public abstract class AbstractEventBus implements EventBus {

    /**
     * The Registered network.
     */
    protected final HashMap<Class<?>, List<EventHandler>> registeredEvents = new HashMap<>();

    @Override
    public <T extends Event> void register(Class<T> klazz, EventHandler<? super T> handler) {
        List<EventHandler> handlers;

        if (registeredEvents.containsKey(klazz)) {
            handlers = registeredEvents.get(klazz);
        } else {
            handlers = new ArrayList<>();
            registeredEvents.put(klazz, handlers);
        }

        handlers.add(handler);
    }

    @Override
    public void register(Object obj) {
        Class<?> klazz = obj.getClass();

        Arrays.stream(klazz.getDeclaredMethods()).map(m -> {
            m.setAccessible(true);
            return m;
        }).filter(m -> m.getAnnotation(world.event.Event.class) != null).forEach(m -> {

            Class<?>[] paramTypes = m.getParameterTypes();

            if (paramTypes.length == 0 || paramTypes.length > 1) {
                throw new AnnotationTypeMismatchException(m, "Invalid annotated method " + m.getName()
                        + " for class " + klazz.getName() + " takes " + paramTypes.length + " args, should be 1.");
            }

            Class paramClass = paramTypes[0];

            register(paramClass, event -> {
                try {
                    m.invoke(obj, event);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public abstract <T extends Event> void fire(T event);


    /**
     * Gets registered network.
     *
     * @return the registered network
     */
    protected HashMap<Class<?>, List<EventHandler>> getRegisteredEvents() {
        return registeredEvents;
    }
}
