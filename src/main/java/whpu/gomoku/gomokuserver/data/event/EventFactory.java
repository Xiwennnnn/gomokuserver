package whpu.gomoku.gomokuserver.data.event;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import whpu.gomoku.gomokuserver.exception.JsonException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class EventFactory {
    private static final Map<String, Class<? extends AbstractRequest>> eventMap;

    static {
        eventMap = new ConcurrentHashMap<>();
        String packageName = AbstractRequest.class.getPackage().getName();
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage(packageName));
        Set<Class<? extends AbstractRequest>> classes = reflections.getSubTypesOf(AbstractRequest.class);
        try {
            for (Class<? extends AbstractRequest> clazz : classes) {
                Method method = clazz.getMethod("getActionType");
                String actionType = (String) method.invoke(clazz.getConstructor().newInstance());
                eventMap.put(actionType, clazz);
            }
        } catch (Exception ignored) {}
    }

    public static AbstractRequest createEvent(JsonNode json) throws JsonException {
        Class<? extends AbstractRequest> clazz = eventMap.get(json.get("action").asText());
        try {
            Method method = clazz.getMethod("fromJson", JsonNode.class);
            return (AbstractRequest) method.invoke(null, json);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            // ignore
        }
        return null;
    }
}
