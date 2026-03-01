package org.catools.mcp.server;

import org.catools.mcp.configuration.McpServerCapabilities;
import org.catools.mcp.configuration.McpServerChangeNotification;
import org.apache.logging.log4j.util.Strings;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to define the common configuration for the MCP server.
 */
public class McpServerInfo {

    /**
     * The name of the MCP server.
     */
    private final String name;

    /**
     * The groups list that the MCP server belong to.
     */
    private final Set<String> groups;

    /**
     * The version of the MCP server.
     */
    private final String version;

    /**
     * The instructions of the MCP server.
     */
    private final String instructions;

    /**
     * The request timeout of the MCP server.
     */
    private final Duration requestTimeout;

    /**
     * The capabilities of the MCP server.
     */
    private final McpServerCapabilities capabilities;

    /**
     * The change notification of the MCP server.
     */
    private final McpServerChangeNotification changeNotification;

    /**
     * Creates a new instance of {@code McpServerInfo} with the specified builder.
     *
     * @param builder the builder to use
     */
    protected McpServerInfo(Builder<?> builder) {
        this.name = builder.name;
        this.groups = builder.groups;
        this.version = builder.version;
        this.instructions = builder.instructions;
        this.requestTimeout = builder.requestTimeout;
        this.capabilities = builder.capabilities;
        this.changeNotification = builder.changeNotification;
    }

    /**
     * Returns a new instance of {@code Builder} to create a {@code McpServerInfo} instance.
     *
     * @return a new instance of {@code Builder}
     */
    public static Builder<?> builder() {
        return new Builder<>();
    }

    /**
     * Returns the name of the MCP server.
     *
     * @return the name of the MCP server
     */
    public String name() {
        return name;
    }

    /**
     * Returns the groups list that the MCP server belong to.
     *
     * @return the groups list that the MCP server belong to
     */
    public Set<String> groups() {
        return groups;
    }

    /**
     * Returns the version of the MCP server.
     *
     * @return the version of the MCP server
     */
    public String version() {
        return version;
    }

    /**
     * Returns the instructions of the MCP server.
     *
     * @return the instructions of the MCP server
     */
    public String instructions() {
        return instructions;
    }

    /**
     * Returns the request timeout of the MCP server.
     *
     * @return the request timeout of the MCP server
     */
    public Duration requestTimeout() {
        return requestTimeout;
    }

    /**
     * Returns the capabilities of the MCP server.
     *
     * @return the capabilities of the MCP server
     */
    public McpServerCapabilities capabilities() {
        return capabilities;
    }

    /**
     * Returns the change notification of the MCP server.
     *
     * @return the change notification of the MCP server
     */
    public McpServerChangeNotification changeNotification() {
        return changeNotification;
    }

    /**
     * The builder class for {@code McpServerInfo}.
     *
     * @param <T> the type of the builder
     */
    @SuppressWarnings("unchecked")
    public static class Builder<T extends Builder<T>> {

        /**
         * The name of the MCP server. Default value is {@code "mcp-server"}.
         */
        protected String name = "mcp-server";

        /**
         * The groups list that the MCP server belong to.
         */
        private Set<String> groups = new HashSet<>();

        /**
         * The version of the MCP server. Default value is {@code "1.0.0"}.
         */
        protected String version = "1.0.0";

        /**
         * The instructions of the MCP server. Default value is {@code ""}.
         */
        protected String instructions = Strings.EMPTY;

        /**
         * The request timeout of the MCP server. Default value is {@code 20} seconds.
         */
        protected Duration requestTimeout = Duration.ofSeconds(20);

        /**
         * The capabilities of the MCP server. Default value is {@code new McpServerCapabilities()}.
         */
        protected McpServerCapabilities capabilities = new McpServerCapabilities();

        /**
         * The change notification of the MCP server. Default value is {@code new
         * McpServerChangeNotification()}.
         */
        protected McpServerChangeNotification changeNotification = new McpServerChangeNotification();

        /**
         * Returns the self reference of the builder, which is used to chain the method calls and ensure
         * the subclasses of {@code McpServerInfo} can also inherit the builder methods with
         * parameterized type.
         *
         * @return the self reference of the builder
         */
        protected T self() {
            return (T) this;
        }

        /**
         * Builds a new instance of {@code McpServerInfo} with the specified configuration.
         *
         * @return a new instance of {@code McpServerInfo}
         */
        public McpServerInfo build() {
            return new McpServerInfo(this);
        }

        /**
         * Sets the name of the MCP server.
         *
         * @param name the name of the MCP server
         * @return the self reference of the builder
         */
        public T name(String name) {
            this.name = name;
            return self();
        }

        /**
         * Sets the groups of the MCP server.
         *
         * @param groups the set of the MCP server group filter
         * @return the self reference of the builder
         */
        public T groups(Set<String> groups) {
            this.groups = groups;
            return self();
        }

        /**
         * Sets the version of the MCP server.
         *
         * @param version the version of the MCP server
         * @return the self reference of the builder
         */
        public T version(String version) {
            this.version = version;
            return self();
        }

        /**
         * Sets the instructions of the MCP server.
         *
         * @param instructions the instructions of the MCP server
         * @return the self reference of the builder
         */
        public T instructions(String instructions) {
            this.instructions = instructions;
            return self();
        }

        /**
         * Sets the request timeout of the MCP server.
         *
         * @param requestTimeout the request timeout of the MCP server
         * @return the self reference of the builder
         */
        public T requestTimeout(Duration requestTimeout) {
            this.requestTimeout = requestTimeout;
            return self();
        }

        /**
         * Sets the capabilities of the MCP server.
         *
         * @param capabilities the capabilities of the MCP server
         * @return the self reference of the builder
         */
        public T capabilities(McpServerCapabilities capabilities) {
            this.capabilities = capabilities;
            return self();
        }

        /**
         * Sets the change notification of the MCP server.
         *
         * @param changeNotification the change notification of the MCP server
         * @return the self reference of the builder
         */
        public T changeNotification(McpServerChangeNotification changeNotification) {
            this.changeNotification = changeNotification;
            return self();
        }
    }
}
