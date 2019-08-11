package com.skosc.arko

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.*
import org.kodein.di.bindings.BindingKodein
import org.kodein.di.bindings.Factory
import org.kodein.di.bindings.Provider
import org.kodein.di.generic.instance

interface IKodeinViewModelProviderFactory<T : ViewModel> : ViewModelProvider.Factory

open class KodeinViewModelProviderFactory<T : ViewModel>(
    private val kodein: DKodein,
    private val factory: DKodein.() -> T
) : IKodeinViewModelProviderFactory<T> {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return factory(kodein) as T
    }
}

class KodeinViewModelProviderFactoryArg<C, A : Any, T : ViewModel>(
    private val kodein: BindingKodein<C>,
    private val factory: BindingKodein<C>.(A) -> T,
    private val arg: A
) : IKodeinViewModelProviderFactory<T> {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return factory(kodein, arg) as T
    }
}

inline fun <reified T : ViewModel> Kodein.Builder.bindViewModel(tag: Any? = null, overrides: Boolean? = null)
        : Kodein.Builder.TypeBinder<IKodeinViewModelProviderFactory<T>> {
    val typeToken = TT((object : TypeReference<IKodeinViewModelProviderFactory<T>>() {}))
    return Bind(typeToken, tag, overrides)
}

inline fun <C, reified T : ViewModel> Kodein.BindBuilder.WithContext<C>.viewModelProvider(
    noinline creator: DKodein.() -> T
): Provider<C, KodeinViewModelProviderFactory<T>> = Provider(contextType, generic()) {
    KodeinViewModelProviderFactory(kodein.direct, creator)
}

inline fun <C, reified A : Any, reified T : ViewModel> Kodein.BindBuilder.WithContext<C>.viewModelFactory(
    noinline creator: BindingKodein<C>.(A) -> T
): Factory<C, A, KodeinViewModelProviderFactoryArg<C, A, T>> = Factory(contextType, generic(), generic()) { arg: A ->
    KodeinViewModelProviderFactoryArg(this, creator, arg)
}
