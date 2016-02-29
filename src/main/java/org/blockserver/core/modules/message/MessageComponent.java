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
package org.blockserver.core.modules.message;

import org.blockserver.core.modules.event.EventComponent;
import org.blockserver.core.modules.event.events.messages.MessageReceiveEvent;
import org.blockserver.core.modules.event.events.messages.MessageSendEvent;
import org.blockserver.core.modules.event.handler.Priority;
import org.blockserver.core.modules.event.handler.ServerEventListener;
import org.blockserver.core.modules.network.NetworkConverter;
import org.blockserver.core.modules.network.pipeline.PacketPipeline;
import org.blockserver.core.modules.network.pipeline.PipelineProviderImplementation;
import org.blockserver.core.modules.network.pipeline.PipelineReceiver;
import org.blockserver.core.modules.network.pipeline.packet.RawPacket;
import org.blockserver.core.modules.thread.ExecutorComponent;
import org.blockserver.core.server.module.Component;

public class MessageComponent extends PipelineProviderImplementation implements PipelineReceiver, Component {
    private final ServerEventListener<MessageSendEvent> listener;
    private final ExecutorComponent executorComponent;
    private final EventComponent eventComponent;
    private final NetworkConverter converter;

    public MessageComponent(ExecutorComponent executorComponent, EventComponent eventComponent, PacketPipeline handler, NetworkConverter converter) {
        super(handler);
        this.executorComponent = executorComponent;
        this.eventComponent = eventComponent;
        this.converter = converter;
        listener = new ServerEventListener<MessageSendEvent>() {
            @Override
            public void onEvent(MessageSendEvent event) {
                if (!event.isCancelled())
                    provide(converter.toPacket(event.getMessage()));
            }
        }.priority(Priority.INTERNAL).post();
    }

    @Override
    public void enable() {
        listener.register(MessageSendEvent.class, eventComponent);
        Component.super.enable();
    }

    @Override
    public void disable() {
        listener.unregister(eventComponent);
        Component.super.disable();
    }

    @Override
    public void receive(RawPacket packet) {
        executorComponent.getExecutorService().execute(() -> eventComponent.getEventManager().fire(new MessageReceiveEvent<>(converter.toMessage(packet))));
    }
}