package com.skosc.arko

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.*
import org.kodein.di.bindings.Provider

class KodeinViewModelProviderFactory<T : ViewModel>(private val kodein: DKodein, private val factory: DKodein.() -> T) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return factory(kodein) as T
    }
}

inline fun <C, reified T : ViewModel> Kodein.BindBuilder.WithContext<C>.viewModelProvider(
    noinline creator: DKodein.() -> T
): Provider<C, KodeinViewModelProviderFactory<T>> = Provider(contextType, generic()) {
    KodeinViewModelProviderFactory(kodein.direct, creator)
}

inline fun <reified T : ViewModel> Kodein.Builder.bindViewModel(tag: Any? = null, overrides: Boolean? = null)
        : Kodein.Builder.TypeBinder<KodeinViewModelProviderFactory<T>> {
    val typeToken = TT((object : TypeReference<KodeinViewModelProviderFactory<T>>() {}))
    return Bind(typeToken, tag, overrides)
}
