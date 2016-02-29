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
package org.blockserver.core.server.loaders;

import org.blockserver.core.modules.config.ConfigComponent;
import org.blockserver.core.modules.file.FileComponent;
import org.blockserver.core.modules.logging.LoggingComponent;
import org.blockserver.core.modules.network.NetworkComponent;
import org.blockserver.core.modules.player.PlayerComponent;
import org.blockserver.core.modules.scheduler.SchedulerComponent;
import org.blockserver.core.modules.thread.ExecutorComponent;
import org.blockserver.core.server.Server;
import org.blockserver.core.server.module.ComponentLoader;

/**
 * @author BlockServer Team
 * @see ComponentLoader
 */
public class CoreComponentLoader implements ComponentLoader {

    @Override
    public void loadComponents(Server server) {

        //Logging Component
        LoggingComponent loggingComponent = new LoggingComponent();
        loggingComponent.info("[CoreComponentLoader]: LoggingComponent online, continuing load with logging capabilities!");

        //No Depends
        FileComponent fileComponent = new FileComponent();
        NetworkComponent networkComponent = new NetworkComponent();

        //Server Depends
        PlayerComponent playerComponent = new PlayerComponent(server);

        //Single Component Depends
        ConfigComponent configComponent = new ConfigComponent(fileComponent);
        ExecutorComponent executorComponent = new ExecutorComponent(configComponent);
        SchedulerComponent schedulerComponent = new SchedulerComponent(executorComponent);

        //Multiple Component Depends


        //No Depends
        server.addComponent(loggingComponent);
        server.addComponent(fileComponent);
        server.addComponent(networkComponent);


        //Server Depends
        server.addComponent(playerComponent);

        //Single Component Depends
        server.addComponent(configComponent);
        server.addComponent(executorComponent);
        server.addComponent(schedulerComponent);

        loggingComponent.info("[CoreComponentLoader]: Loading complete!");
    }
}