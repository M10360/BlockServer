/*
 * This file is part of BlockServer.
 *
 * BlockServer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BlockServer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with BlockServer.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.blockserver.core.modules.logging;

import org.blockserver.core.server.module.Component;
import org.blockserver.core.server.module.JarComponent;

/**
 * Logging JarComponent with different log levels. (debug, info, warn, error)
 * TODO: Implement SLF4j and/or log4j2
 *
 * @author BlockServer Team
 * @see JarComponent
 */
public class LoggingComponent implements Component {

    public LoggingComponent() {

    }

    public void debug(String message) {
        System.out.println("[DEBUG]: " + message);
    }

    public void info(String message) {
        System.out.println("[INFO]: " + message);
    }

    public void warn(String message) {
        System.out.println("[WARN]: " + message);
    }

    public void error(String message) {
        System.err.println("[ERROR]: " + message);
    }
}
