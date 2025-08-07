# Pelecard Payment App

This is an Android payment app built with Kotlin and Jetpack Compose.  
It lets users enter payment details, toggle UI options, capture a signature, and convert currencies using a free API.


### Features
- Enter payment amount and number of payments
- Toggle UI sections: payments, currency, signature
- Switch between ILS and USD
- Draw and save signature
- View digital clock
- Currency conversion using FreeCurrencyAPI
- Clean Architecture with modular structure
- Light and dark theme support


### Tech Stack
- Kotlin
- Jetpack Compose
- Hilt
- Retrofit + Gson
- Coroutines and StateFlow
- MVVM architecture
- Clean Architecture (domain, data, app)

To use it:
1. Go to [freecurrencyapi](https://freecurrencyapi.com/) and sign up for a free account.
2. Copy your API key.
3. Open Constants.kt and replace the value of FREE_CURRENCY_API_KEY with your own key.


