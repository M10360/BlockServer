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
package org.blockserver.core.server;

import org.blockserver.core.server.module.Component;
import org.blockserver.core.server.module.ComponentLoader;
import org.blockserver.core.server.module.EnableableImplementation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the core server implementation.
 *
 * @author BlockServer Team
 */
@SuppressWarnings("unchecked")
public class Server implements EnableableImplementation {
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();

    public Server(ComponentLoader... componentLoaders) {
        for (ComponentLoader componentLoader : componentLoaders) {
            componentLoader.loadComponents(this);
        }
    }

    public void addComponent(Component component) {
        components.put(component.getClass(), component);
    }

    public void removeComponent(Component component) {
        removeComponent(component.getClass());
    }

    public void removeComponent(Class<? extends Component> moduleClass) {
        components.remove(moduleClass);
    }

    public <T extends Component> T getComponent(Class<T> moduleClass) {
        return (T) components.get(moduleClass);
    }


    @Override
    public void enable() {
        components.values().forEach(Component::enable);
        EnableableImplementation.super.enable();
    }

    @Override
    public void disable() {
        components.values().forEach(Component::disable);
        EnableableImplementation.super.disable();
    }


    public Map<Class<? extends Component>, Component> getComponents() {
        return Collections.unmodifiableMap(components);
    }
}