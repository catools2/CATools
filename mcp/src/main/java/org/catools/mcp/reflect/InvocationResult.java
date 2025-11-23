package org.catools.mcp.reflect;

import org.catools.mcp.common.Immutable;

/**
 * This record represents the result of reflection invocation of Java method.
 */
public record InvocationResult(Object result, Immutable<Exception> exception) {

    /**
     * Returns whether the invocation resulted in an error.
     *
     * @return {@code true} if the invocation resulted in an error, {@code false} otherwise
     */
    public boolean isError() {
        return exception != null && exception.get() != null;
    }

    /**
     * Returns a new instance of {@code Builder} for creating a new {@code InvocationResult}.
     *
     * @return a new instance of {@code Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * This class implements the builder pattern for creating a new instance of {@code
     * InvocationResult}.
     */
    public static final class Builder {

        /**
         * The result of the invocation.
         */
        private Object result;

        /**
         * The exception that occurred during the invocation, if any.
         */
        private Immutable<Exception> exception;

        /**
         * Sets the result of the invocation.
         *
         * @param result the result of the invocation
         * @return the builder instance
         */
        public Builder result(Object result) {
            this.result = result;
            return this;
        }

        /**
         * Sets the exception that occurred during the invocation.
         *
         * @param exception the exception that occurred during the invocation
         * @return the builder instance
         */
        public Builder exception(Exception exception) {
            this.exception = Immutable.of(exception);
            return this;
        }

        /**
         * Builds a new instance of {@code InvocationResult} with the configured values.
         *
         * @return a new instance of {@code InvocationResult}
         */
        public InvocationResult build() {
            return new InvocationResult(result, exception);
        }
    }
}
