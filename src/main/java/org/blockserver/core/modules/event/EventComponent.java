package org.blockserver.core.modules.event;

import lombok.Getter;
import lombok.Setter;
import org.blockserver.core.modules.event.handler.EventManager;
import org.blockserver.core.server.module.Component;

/**
 * Created by Exerosis.
 */
public class EventComponent implements Component {
    @Getter @Setter private EventManager eventManager = new EventManager();

    public EventComponent() {

    }
}