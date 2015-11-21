package org.blockserver.logging;

import org.blockserver.Server;
import org.blockserver.module.Module;

/**
 * Created by jython234 on 11/21/2015.
 *
 * @author BlockServer Team
 */
public class LoggingModule extends Module{

    public LoggingModule(Server server) {
        super(server);
        setName("Logging");
    }

    public void info(String message) {
        System.out.println("[INFO]: "+message);
    }
}
