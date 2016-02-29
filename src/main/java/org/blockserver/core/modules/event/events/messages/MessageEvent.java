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
package org.blockserver.core.modules.event.events.messages;

import lombok.Getter;
import lombok.Setter;
import org.blockserver.core.modules.event.handler.CancellableImplementation;
import org.blockserver.core.modules.message.Message;

/**
 * Written by Exerosis!
 */
public class MessageEvent<T extends Message> implements CancellableImplementation {
    @Getter @Setter private T message;

    public MessageEvent(T message) {
        this.message = message;
    }
}