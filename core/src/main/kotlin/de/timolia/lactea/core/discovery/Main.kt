package de.timolia.lactea.core.discovery

import com.google.inject.*
import de.timolia.lactea.core.discovery.Main.Module
import java.util.Objects
import javax.inject.Provider
import javax.inject.Singleton

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val injector = Guice.createInjector()
        var injectoProvder: Provider<com.google.inject.Injector>? = null
        val c1 = injector.createChildInjector(
            object: PrivateModule() {
                override fun configure() {
                    bind(Module::class.java).toInstance(Module("a"))
                    bind(Test2::class.java)
                    expose(Test2::class.java)
                    injectoProvder = getProvider(com.google.inject.Injector::class.java)
                }
            },
            object: PrivateModule() {
                override fun configure() {
                    bind(Module::class.java).toInstance(Module("b"))
                }
            }
        )
        println(injectoProvder!!.get()!!.getInstance(Test::class.java))
        //println(c1.getInstance(Test::class.java)!!.module!!.name)
    }

    internal data class Module(val name: String)

    @Singleton
    private class Injector {
        @Inject
        val injector : com.google.inject.Injector? = null
    }

    @Singleton
    private class Test2 {
        @Inject
        val test : Test? = null;
    }

    @Exposed
    @Singleton
    private class Test {
        @Inject
        val module: Module? = null
    }
}
