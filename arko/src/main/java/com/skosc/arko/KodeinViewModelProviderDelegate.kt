package com.skosc.arko

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import org.kodein.di.*
import org.kodein.di.generic.instance
import kotlin.reflect.KProperty

class KodeinViewModelProviderDelegate<VM : ViewModel>(
    private val factoryProvider: (Kodein) -> KodeinProperty<KodeinViewModelProviderFactory<VM>>,
    private val cls: Class<VM>
) {
    var instance: VM? = null

    operator fun getValue(thisRef: KodeinAware, property: KProperty<*>): VM {
        if (instance == null) {
            instance = createViewModel(thisRef)
        }
        return instance!! // Usually accessed on main thread, so no race condition
    }

    private fun createViewModel(thisRef: KodeinAware): VM {
        val factory by factoryProvider.invoke(thisRef.kodein)
        val provider = when (thisRef) {
            is ViewModelStoreOwner -> ViewModelProvider(thisRef, factory)
            is ViewModelStore -> ViewModelProvider(thisRef, factory)
            else -> throw IllegalArgumentException("This delegate only supports ViewModelStoreOwners and ViewModelStores")
        }

        return provider.get(cls)
    }


}

inline fun <reified T : ViewModel> viewModel(): KodeinViewModelProviderDelegate<T> {
    val provider = { kodein: Kodein -> kodein.instance<KodeinViewModelProviderFactory<T>>() }
    return KodeinViewModelProviderDelegate(provider, T::class.java)
}
