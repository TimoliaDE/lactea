package de.timolia.lactea.inject

import com.google.inject.Injector

fun interface InjectedInstance<T> {
    fun getInstance(injector: Injector): T

    companion object {
        fun <T> ofInstance(instance: T): InjectedInstance<T> {
            return InjectedInstance { instance }
        }

        fun <T> ofClass(clazz: Class<out T>): InjectedInstance<T> {
            return InjectedInstance { injector -> injector.getInstance(clazz) }
        }

        fun <T, F> ofFactory(
            factoryClass: Class<out F>,
            createFunction: (F) -> T
        ): InjectedInstance<T> {
            return InjectedInstance { injector -> createFunction(injector.getInstance(factoryClass)) }
        }
    }
}