# Arko

Integration library between https://github.com/Kodein-Framework/Kodein-DI and Android Architecture Components.

## Quick Start

### View Model Injection
```kotlin

// In your Kodein.Module

bindViewModel<MyViewModel> with viewModelProvider { viewModelProvider(instance()) }

// In your Activity/Fragment

private val vm: MyViewModel by viewModel()

```