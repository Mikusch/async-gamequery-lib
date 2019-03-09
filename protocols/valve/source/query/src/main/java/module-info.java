module agql.source.query {
    exports com.ibasco.agql.protocols.valve.source.query.logger;
    exports com.ibasco.agql.protocols.valve.source.query;
    exports com.ibasco.agql.protocols.valve.source.query.client;
    exports com.ibasco.agql.protocols.valve.source.query.exceptions;
    exports com.ibasco.agql.protocols.valve.source.query.enums;
    exports com.ibasco.agql.protocols.valve.source.query.pojos;
    requires agql.lib.core;
    requires org.apache.commons.lang3;
    requires guava;
    requires slf4j.api;
    requires io.netty.transport;
    requires io.netty.buffer;
    requires io.netty.common;
    requires io.netty.codec;
}