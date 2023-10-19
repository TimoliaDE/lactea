package de.timolia.lactea.source;

import de.timolia.lactea.source.SourceRoot.SourceElementAcceptor;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.function.Consumer;

public class UrlClassPathInjector implements ClassPathInjector {
    private final URLClassLoader classLoader;

    public UrlClassPathInjector(URLClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void addToClassPath(URI file) {
        Method method = ReflectionHolder.addUrlMethod;
        try {
            URL url = file.toURL();
            method.invoke(classLoader, url);
        } catch (IllegalAccessException | InvocationTargetException | MalformedURLException e) {
            throw new AddToClassPathException(file, e);
        }
    }

    @Override
    public void addToClassPath(final SourceRoot sourceRoot) {
        sourceRoot.forEachJar(new SourceElementAcceptor() {
            @Override
            public void accept(URI uri) {
                addToClassPath(uri);
            }
        });
    }

    public static class AddToClassPathException extends RuntimeException {
        private final URI resource;

        public AddToClassPathException(URI resource, Throwable cause) {
            super("Failed to add jar to classpath: " + resource, cause);
            this.resource = resource;
        }

        public URI resource() {
            return resource;
        }
    }

    static class ReflectionHolder {
        static final Method addUrlMethod = createAndMakeAccessible();

        private static Method createAndMakeAccessible() {
            try {
                Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addURL.setAccessible(true);
                return addURL;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InaccessibleObjectException e) {
                throw new RuntimeException("Make sure to open up reflection", e);
            }
        }
    }
}
