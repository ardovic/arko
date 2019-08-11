package com.skosc.arko

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val MainModule = Kodein.Module("name", false, "prefix") {
    bind<Sumator>() with provider { Sumator() }
    bindViewModel<MainViewModel>() with viewModelProvider { MainViewModel(instance()) }
}
