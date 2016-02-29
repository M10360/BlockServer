package org.blockserver.core.modules.config;

import org.blockserver.core.modules.file.FileComponent;
import org.blockserver.core.server.module.Component;

/**
 * Created by Exerosis.
 */
public class ConfigComponent implements Component {
    private final FileComponent fileComponent;

    public ConfigComponent(FileComponent fileModule) {
        this.fileComponent = fileModule;
    }
}