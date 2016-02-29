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
package org.blockserver.core.modules.serverlist;

import lombok.Getter;
import org.blockserver.core.modules.event.EventComponent;
import org.blockserver.core.modules.event.events.packets.PacketEvent;
import org.blockserver.core.modules.event.handler.ServerEventListener;
import org.blockserver.core.modules.scheduler.SchedulerComponent;
import org.blockserver.core.server.module.Component;
import org.blockserver.core.server.module.JarComponent;

/**
 * Written by Exerosis!
 *
 * @author BlockServer Team
 * @see JarComponent
 */
public class ServerListComponent implements Component {
    private final SchedulerComponent schedulerComponent;
    private final EventComponent eventComponent;
    @Getter private final Runnable task;
    private final ServerEventListener<PacketEvent> listener;
    private EventComponent eventModule;

    public ServerListComponent(SchedulerComponent schedulerComponent, EventComponent eventComponent) {
        this.schedulerComponent = schedulerComponent;
        this.eventComponent = eventComponent;
        task = () -> {
            //networkModule.sendPackets();
            //send things
        };
        listener = new ServerEventListener<PacketEvent>() {
            @Override
            public void onEvent(PacketEvent event) {
                //receive pings
                //send pongs
            }
        };
    }

    @Override
    public void enable() {
        schedulerComponent.registerTask(task, 1.0, Integer.MAX_VALUE);
        listener.register(PacketEvent.class, eventModule);
        Component.super.enable();
    }

    @Override
    public void disable() {
        schedulerComponent.cancelTask(task);
        listener.unregister(eventModule);
        Component.super.disable();
    }
}
