module agql.csgo.webapi {
    exports com.ibasco.agql.protocols.valve.csgo.webapi;
    exports com.ibasco.agql.protocols.valve.csgo.webapi.interfaces;
    exports com.ibasco.agql.protocols.valve.csgo.webapi.pojos;
    requires agql.steam.webapi;
    requires async.http.client;
    requires com.google.gson;
    requires agql.lib.core;
}