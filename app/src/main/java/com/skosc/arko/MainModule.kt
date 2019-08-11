package com.skosc.arko

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val MainModule = Kodein.Module("name", false, "prefix") {
    bindViewModel<MainViewModel>() with viewModelFactory { sum: Sumator -> MainViewModel(sum) }
}
