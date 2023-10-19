package de.timolia.lactea.loader.config;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector();
        Injector c1 = injector.createChildInjector(binder -> binder.bind(Module.class).toInstance(new Module("a")));
        Injector c2 = injector.createChildInjector(binder -> binder.bind(Module.class).toInstance(new Module("b")));
        System.out.println(c1.getInstance(Test.class).module.name);
        System.out.println(c2.getInstance(Test.class).module.name);
    }

    record Module(String name) {}

    private static class Test {
        @Inject
        private Module module;
    }
}
