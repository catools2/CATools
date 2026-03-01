package org.catools.mcp.di;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import java.util.Optional;

/**
 * This class implements the {@link DependencyInjector} interface using Google Guice.
 */
public final class DependencyInjector {

    /**
     * The Guice {@link Injector} used for dependency injection.
     */
    private final Injector injector;

    /**
     * Creates a new instance of {@code GuiceDependencyInjector} with the specified Guice {@link
     * Injector}.
     *
     * @param injector the Guice {@link Injector} to use for dependency injection
     */
    public DependencyInjector(Injector injector) {
        this.injector = injector;
    }

    public <T> T getInstance(Class<T> type) {
        if (isInitialized()) {
            return injector.getInstance(type);
        }
        throw new IllegalStateException("GuiceDependencyInjector is not initialized");
    }

    public <T> T getVariable(Class<T> type, String name) {
        if (isInitialized()) {
            return injector.getInstance(Key.get(type, Names.named(name)));
        }
        throw new IllegalStateException("GuiceDependencyInjector is not initialized");
    }

    public boolean isInitialized() {
        return injector != null;
    }

    public <T> Optional<T> tryGetInstance(Class<T> type) {
        if (!isInitialized()) {
            return Optional.empty();
        }
        Binding<T> binding = injector.getExistingBinding(Key.get(type));
        return binding == null ? Optional.empty() : Optional.of(injector.getInstance(type));
    }

    public <T> Optional<T> tryGetVariable(Class<T> type, String name) {
        if (!isInitialized()) {
            return Optional.empty();
        }
        Binding<T> binding = injector.getExistingBinding(Key.get(type, Names.named(name)));
        return binding == null
                ? Optional.empty()
                : Optional.of(injector.getInstance(Key.get(type, Names.named(name))));
    }
}
