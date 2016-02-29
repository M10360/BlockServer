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
package org.blockserver.core.modules.network;

import org.blockserver.core.modules.event.EventComponent;
import org.blockserver.core.modules.event.events.packets.PacketReceiveEvent;
import org.blockserver.core.modules.event.events.packets.PacketSendEvent;
import org.blockserver.core.modules.event.handler.Priority;
import org.blockserver.core.modules.event.handler.ServerEventListener;
import org.blockserver.core.modules.network.pipeline.PacketPipeline;
import org.blockserver.core.modules.network.pipeline.PipelineProviderImplementation;
import org.blockserver.core.modules.network.pipeline.PipelineReceiver;
import org.blockserver.core.modules.network.pipeline.packet.RawPacket;
import org.blockserver.core.server.module.Component;

public class PacketEventComponent extends PipelineProviderImplementation implements PipelineReceiver, Component {
    private final ServerEventListener<PacketSendEvent> listener;
    private final EventComponent eventComponent;

    public PacketEventComponent(EventComponent eventModule, PacketPipeline handler) {
        super(handler);
        this.eventComponent = eventModule;
        listener = new ServerEventListener<PacketSendEvent>() {
            @Override
            public void onEvent(PacketSendEvent event) {
                if (!event.isCancelled())
                    provide(event.getPacket());
            }
        }.priority(Priority.INTERNAL).post();
    }

    @Override
    public void enable() {
        listener.register(PacketSendEvent.class, eventComponent);
        Component.super.enable();
    }

    @Override
    public void disable() {
        listener.unregister(eventComponent);
        Component.super.disable();
    }

    @Override
    public void receive(RawPacket packet) {
        eventComponent.getEventManager().fire(new PacketReceiveEvent(packet));
    }
}