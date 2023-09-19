package de.timolia.lactea.loader.startup.internal;

import de.timolia.lactea.loader.module.LacteaModule;
import de.timolia.lactea.loader.module.ModuleDescription;
import de.timolia.lactea.loader.module.ModuleDescription.DependencyDescription;
import de.timolia.lactea.loader.module.ModuleRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModuleBootOrder {
    private final ModuleRegistry registry;
    private final List<ModuleDescription> orderedModules = new ArrayList<>();
    private final List<ModuleDescription> orderedModuleView = Collections.unmodifiableList(orderedModules);
    private final Stack<ModuleDescription> trace = new Stack<>();
    private volatile boolean locked;

    public void addBatch(Collection<ModuleDescription> definitions) {
        if (locked) {
            throw new IllegalStateException("Already locked");
        }
        List<ModuleDescription> copy = new ArrayList<>(definitions);
        copy.sort(Comparator.comparing(ModuleDescription::getName, String::compareTo));
        copy.forEach(this::tryAdd);
    }

    public List<ModuleDescription> ordered() {
        if (!locked) {
            throw new IllegalStateException("Not yet locked");
        }
        return orderedModuleView;
    }

    public List<LacteaModule> orderedModules() {
        return ordered().stream()
            .map(description -> registry.requireByName(description.getName()))
            .collect(Collectors.toList());
    }

    public void lock() {
        locked = true;
    }

    public void addBatchAndLock(Collection<ModuleDescription> definitions) {
        addBatch(definitions);
        lock();
    }

    private void handleMissingDependency(DependencyDescription description) {
        if (description.required()) {
            throw new IllegalStateException("Missing dependency " + description.value());
        }
    }

    private void tryAdd(ModuleDescription description) {
        try {
            add(description);
        } catch (Exception exception) {
            throw new RuntimeException("Unable to build dependency tree for" + description.getName());
        }
    }

    private State currentState(ModuleDescription description) {
        if (trace.contains(description)) {
            return State.IN_LOAD_TRACE;
        }
        if (orderedModules.contains(description)) {
            return State.SAFELY_LOADED;
        }
        return State.NOT_INVOLVED;
    }

    private void handleCircularDependency() {
        String formatted = trace.stream()
            .map(ModuleDescription::getName)
            .collect(Collectors.joining(" <-> "));
        throw new RuntimeException("Circular dependency between " + formatted);
    }

    private void addNext(ModuleDescription description) {
        orderedModules.add(description);
        for (DependencyDescription dependencyDescription : description.getDependencies()) {
            ModuleDescription dependency = registry.definition(dependencyDescription.value());
            if (dependency == null) {
                handleMissingDependency(dependencyDescription);
                continue;
            }
            tryAdd(dependency);
        }
        trace.pop();
    }

    private void add(ModuleDescription description) {
        State state = currentState(description);
        trace.push(description);
        switch (state) {
            case IN_LOAD_TRACE -> handleCircularDependency();
            case NOT_INVOLVED -> addNext(description);
            case SAFELY_LOADED -> {}
        }
    }

    enum State {
        IN_LOAD_TRACE,
        SAFELY_LOADED,
        NOT_INVOLVED
    }
}
