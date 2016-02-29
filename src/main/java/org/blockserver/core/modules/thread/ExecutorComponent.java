package org.blockserver.core.modules.thread;

import lombok.Getter;
import lombok.Setter;
import org.blockserver.core.modules.config.ConfigComponent;
import org.blockserver.core.server.module.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Exerosis.
 */
public class ExecutorComponent implements Component {
    private final ConfigComponent configComponent;
    @Getter @Setter private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public ExecutorComponent(ConfigComponent configModule) {
        this.configComponent = configModule;
    }
}