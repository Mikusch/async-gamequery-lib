module agql.lib.core {
    exports com.ibasco.agql.core;
    exports com.ibasco.agql.core.client;
    exports com.ibasco.agql.core.exceptions;
    exports com.ibasco.agql.core.utils;
    exports com.ibasco.agql.core.enums;
    exports com.ibasco.agql.core.messenger;
    exports com.ibasco.agql.core.transport.udp;
    exports com.ibasco.agql.core.session;
    exports com.ibasco.agql.core.transport.tcp;
    exports com.ibasco.agql.core.transport.handlers;
    exports com.ibasco.agql.core.transport;
    exports com.ibasco.agql.core.functions;
    exports com.ibasco.agql.core.reflect.types;

    requires io.netty.buffer;
    requires org.apache.commons.lang3;
    requires slf4j.api;
    requires io.netty.common;
    requires io.netty.transport;
    requires io.netty.transport.epoll;
    requires async.http.client;
    requires io.netty.codec.http;
    requires gson;
    requires org.apache.commons.codec;
    requires java.sql;
    requires com.google.common;
    requires io.netty.codec;
    requires org.apache.commons.text;
    requires guava.retrying;
}