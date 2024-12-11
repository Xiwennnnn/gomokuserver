package whpu.gomoku.gomokuserver;

import io.netty.channel.Channel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.dto.UserDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public final class GomokuContext {
    public static ApplicationContext applicationContext;
    public static final Map<Long, Channel> userChannelMap = new ConcurrentHashMap<>();
    public static final Map<Integer, Long> userIdMap = new ConcurrentHashMap<>();

    @Resource
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        applicationContext = context;
    }
}
