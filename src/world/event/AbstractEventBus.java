package world.event;


import world.event.impl.AbstractEvent;

import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Abstract event bus.
 */
public abstract class AbstractEventBus implements EventBus {
    private static final Logger logger = Logger.getLogger(AbstractEventBus.class.getName());

    /**
     * The Registered impl.
     */
    protected final HashMap<Class<?>, List<EventHandler>> registeredEvents = new HashMap<>();

    @Override
    public <T extends AbstractEvent> void register(Class<T> klazz, EventHandler<? super T> handler) {
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

            AbstractEventBus.logger.log(Level.INFO, "registering event handler under  " + klazz.getName() + " for event " + paramClass.getName());
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
    public abstract <T extends AbstractEvent> void fire(T event);


    /**
     * Gets registered impl.
     *
     * @return the registered impl
     */
    protected HashMap<Class<?>, List<EventHandler>> getRegisteredEvents() {
        return registeredEvents;
    }
}
