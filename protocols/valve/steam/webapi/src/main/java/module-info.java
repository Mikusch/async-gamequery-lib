module agql.steam.webapi {
    exports com.ibasco.agql.protocols.valve.steam.webapi;
    exports com.ibasco.agql.protocols.valve.steam.webapi.interfaces;
    exports com.ibasco.agql.protocols.valve.steam.webapi.pojos;
    exports com.ibasco.agql.protocols.valve.steam.webapi.enums;
    opens com.ibasco.agql.protocols.valve.steam.webapi.pojos to gson;

    requires gson;
    requires org.apache.commons.lang3;
    requires async.http.client;
    requires slf4j.api;
    requires agql.lib.core;
    requires io.netty.codec.http;
}
