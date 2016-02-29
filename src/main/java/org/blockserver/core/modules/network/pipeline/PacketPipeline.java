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
package org.blockserver.core.modules.network.pipeline;


import lombok.Getter;
import org.blockserver.core.modules.network.pipeline.packet.RawPacket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PacketPipeline {
    @Getter private final Set<PipelineReceiver> dispatchers = Collections.synchronizedSet(new HashSet<>());

    public PacketPipeline() {

    }

    public void provide(RawPacket packet) {
        for (PipelineReceiver dispatcher : dispatchers) {
            dispatcher.receive(packet);
        }
    }

    public void unregisterDispatcher(PipelineReceiver dispatcher) {
        dispatchers.remove(dispatcher);
    }

    public void registerDispatcher(PipelineReceiver dispatcher) {
        dispatchers.add(dispatcher);
    }
}