module agql.dota2.webapi {
    exports com.ibasco.agql.protocols.valve.dota2.webapi;
    exports com.ibasco.agql.protocols.valve.dota2.webapi.enums;
    exports com.ibasco.agql.protocols.valve.dota2.webapi.interfaces;
    exports com.ibasco.agql.protocols.valve.dota2.webapi.pojos;
    requires gson;
    requires org.apache.commons.lang3;
    requires agql.steam.webapi;
    requires slf4j.api;
    requires agql.lib.core;
}