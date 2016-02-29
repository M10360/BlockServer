package org.blockserver.core.modules.entity;

import lombok.Getter;
import org.blockserver.core.server.module.Component;

/**
 * Created by Exerosis.
 */
public class EntityComponent implements Component {
    @Getter private final Entity entity;

    public EntityComponent(Entity entity) {
        this.entity = entity;
    }
}