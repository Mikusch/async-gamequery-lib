module agql.coc.webapi {
    exports com.ibasco.agql.protocols.supercell.coc.webapi;
    exports com.ibasco.agql.protocols.supercell.coc.webapi.enums;
    exports com.ibasco.agql.protocols.supercell.coc.webapi.interfaces;
    exports com.ibasco.agql.protocols.supercell.coc.webapi.pojos;
    requires gson;
    requires agql.lib.core;
    requires io.netty.codec.http;
    requires slf4j.api;
    requires org.apache.commons.lang3;
    opens com.ibasco.agql.protocols.supercell.coc.webapi.pojos;
}