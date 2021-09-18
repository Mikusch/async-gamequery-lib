module agql.coc.webapi {
    exports com.ibasco.agql.protocols.supercell.coc.webapi;
    exports com.ibasco.agql.protocols.supercell.coc.webapi.enums;
    exports com.ibasco.agql.protocols.supercell.coc.webapi.interfaces;
    exports com.ibasco.agql.protocols.supercell.coc.webapi.pojos;
    requires com.google.gson;
    requires io.netty.codec.http;
    requires org.slf4j;
    requires async.http.client;
    requires agql.lib.core;
    opens com.ibasco.agql.protocols.supercell.coc.webapi.pojos;
}