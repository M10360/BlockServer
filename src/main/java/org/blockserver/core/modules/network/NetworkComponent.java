package org.blockserver.core.modules.network;

import lombok.Getter;
import org.blockserver.core.modules.network.pipeline.PacketPipeline;
import org.blockserver.core.server.module.Component;

/**
 * Created by Exerosis.
 */
public class NetworkComponent implements Component {
    @Getter private PacketPipeline inboundHandler = new PacketPipeline();
    @Getter private PacketPipeline outboundHandler = new PacketPipeline();

}
