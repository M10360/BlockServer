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
package org.blockserver.core.modules.player;

import org.blockserver.core.modules.logging.LoggingComponent;
import org.blockserver.core.modules.network.pipeline.PipelineProviderImplementation;
import org.blockserver.core.server.Server;
import org.blockserver.core.server.module.Component;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Component that handles players.
 *
 * @author BlockServer Team
 * @see Component
 */
public class PlayerComponent implements Component {
    private final Set<Player> players = Collections.synchronizedSet(new HashSet<>());
    private Server server;

    public PlayerComponent(Server server) {
        this.server = server;
    }


    @Override
    public void disable() {
        players.clear();
        Component.super.disable();
    }

    /**
     * Attempts to find a current online {@linkplain Player} with the specified name. If
     * there is no {@linkplain Player} found this method will return null.
     *
     * @param name ({@linkplain String}): The name of the {@linkplain Player} to locate.
     * @return player - ({@linkplain Player}): The {@linkplain Player} with the given name or null.
     */
    public Player getPlayer(String name) {
        for (Player player : players) {
            if (player.getName().equals(name))
                return player;
        }
        return null;
    }

    /**
     * Attempts to find a current online {@linkplain Player} with the specified {@linkplain UUID}. If
     * there is no {@linkplain Player} found this method will return null.
     *
     * @param name ({@linkplain UUID}): The {@linkplain UUID} of the {@linkplain Player} to locate.
     * @return player - ({@linkplain Player}): The {@linkplain Player} with the given {@linkplain UUID} or null.
     */
    public Player getPlayer(UUID name) {
        for (Player player : players) {
            if (player.getUUID().equals(name))
                return player;
        }
        return null;
    }

    /**
     * Attempts to find a current online {@linkplain Player} with the specified {@linkplain InetSocketAddress}. If
     * there is no {@linkplain Player} found this method will return null.
     *
     * @param address ({@linkplain InetSocketAddress}): The {@linkplain InetSocketAddress} of the {@linkplain Player} to locate.
     * @return player - ({@linkplain Player}): The {@linkplain Player} with the given {@linkplain InetSocketAddress} or null.
     */
    public Player getPlayer(InetSocketAddress address) {
        for (Player player : players) {
            if (player.getAddress().equals(address))
                return player;
        }
        return null;
    }


    /**
     * Opens a new session, and adds the specified {@linkplain Player} to the list of online {@linkplain Player}s.
     * <br>
     * NOTE: THIS METHOD IS FOR INTERNAL USE ONLY!
     *
     * @param address ({@linkplain InetSocketAddress}): The new {@linkplain Player}'s {@linkplain InetSocketAddress}.
     * @param name ({@linkplain String}): The new {@linkplain Player}'s {@linkplain String}.
     * @param UUID ({@linkplain UUID}): The new {@linkplain Player}'s {@linkplain UUID}.
     * @param provider {{@linkplain PipelineProviderImplementation}}: The {@linkplain Player}'s {@linkplain PipelineProviderImplementation} that is used
     *                 to communicate with the client.
     */
    public void internalOpenSession(InetSocketAddress address, String name, UUID UUID, PipelineProviderImplementation provider) {
        players.add(new Player(server, address, name, UUID/*, provider*/));
        server.getComponent(LoggingComponent.class).debug("New session from " + address.getHostString() + ":" + address.getPort());
    }

    /**
     * Removes a Player from the list of online players.
     * <br>
     * NOTE: THIS METHOD IS FOR INTERNAL USE ONLY!
     *
     * @param player The player to be removed.
     */
    public void internalCloseSession(Player player) {
        players.remove(player);
        server.getComponent(LoggingComponent.class).debug("Session " + player.getAddress().getHostString() + ":" + player.getAddress().getPort() + " closed.");
    }

    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(players);
    }
}