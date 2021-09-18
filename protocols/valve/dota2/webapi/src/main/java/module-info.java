module agql.dota2.webapi {
    exports com.ibasco.agql.protocols.valve.dota2.webapi;
    exports com.ibasco.agql.protocols.valve.dota2.webapi.enums;
    exports com.ibasco.agql.protocols.valve.dota2.webapi.interfaces;
    exports com.ibasco.agql.protocols.valve.dota2.webapi.pojos;
    requires com.google.gson;
    requires agql.steam.webapi;
    requires org.slf4j;
    requires agql.lib.core;
    requires async.http.client;
}