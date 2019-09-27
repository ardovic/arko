package com.skosc.arko

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import org.kodein.di.*
import org.kodein.di.generic.instance
import kotlin.reflect.KProperty

class KodeinViewModelProviderDelegate<VM : ViewModel>(
    private val factoryProvider: (Kodein) -> KodeinProperty<IKodeinViewModelProviderFactory<VM>>,
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

        val owner = when(thisRef) {
            is Fragment -> thisRef.requireActivity()
            is AppCompatActivity -> thisRef
            else -> thisRef as ViewModelStoreOwner
        }

        val provider = ViewModelProvider(owner, factory)
        return provider.get(cls)
    }
}

inline fun <reified T : ViewModel> viewModel(): KodeinViewModelProviderDelegate<T> {
    val provider = { kodein: Kodein -> kodein.instance<IKodeinViewModelProviderFactory<T>>() }
    return KodeinViewModelProviderDelegate(provider, T::class.java)
}

inline fun <reified A : Any, reified T : ViewModel> viewModel(crossinline arg: () -> A): KodeinViewModelProviderDelegate<T> {
    val provider = { kodein: Kodein ->
        kodein.instance<A, IKodeinViewModelProviderFactory<T>>(
            arg = arg.invoke()
        )
    }
    return KodeinViewModelProviderDelegate(provider, T::class.java)
}
