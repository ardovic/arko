package com.skosc.arko.app

import com.skosc.arko.bindViewModel
import com.skosc.arko.viewModelFactory
import org.kodein.di.Kodein

val MainModule = Kodein.Module("name", false, "prefix") {
    bindViewModel<MainViewModel>() with viewModelFactory { sum: Sumator ->
        MainViewModel(
            sum
        )
    }
}
