package net.mrwooly357.prism.client.util

class CacheableValue<T : Any> {

    var value: T? = null
        private set


    fun getOrSupply(supplier: () -> T): T {
        if (value == null)
            value = supplier()

        return value!!
    }
}
