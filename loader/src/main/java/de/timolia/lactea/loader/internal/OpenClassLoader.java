package de.timolia.lactea.loader.internal;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author David (_Esel)
 */
public class OpenClassLoader extends URLClassLoader {
    private static final URL[] EMPTY_URLS = new URL[0];

    public OpenClassLoader(ClassLoader parent) {
        super(EMPTY_URLS, parent);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}
