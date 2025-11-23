package org.catools.mcp.annotation;

import org.catools.common.utils.CStringUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for defining JSON schema properties on fields within custom data classes
 * used by MCP (Model Context Protocol) servers.
 *
 * <p>This annotation enables the definition of complex JSON schema types that extend
 * beyond native MCP protocol support, allowing richer data models in server implementations.
 *
 * @see CMcpJsonSchemaDefinition
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CMcpJsonSchemaProperty {
    /**
     * Specifies the JSON schema property name. When not provided, the field name is used as default.
     *
     * @return the property name in the JSON schema
     */
    String name() default CStringUtil.EMPTY;

    /**
     * Provides a description of the JSON schema property for documentation purposes.
     * Defaults to empty string when not specified.
     *
     * @return the property description
     */
    String description() default CStringUtil.EMPTY;

    /**
     * Indicates whether this JSON schema property is mandatory.
     * Defaults to {@code false} if not explicitly set.
     *
     * @return {@code true} if the property is required, {@code false} otherwise
     */
    boolean required() default false;
}
