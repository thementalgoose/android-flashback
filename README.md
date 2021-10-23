# Flashback - Android 

[![main](https://github.com/thementalgoose/android-flashback/workflows/Main/badge.svg)](https://github.com/thementalgoose/android-flashback/actions)

[![Google Play](https://i.imgur.com/gSfLc4N.png)](https://play.google.com/store/apps/details?id=tmg.flashback)

![Flashback](res/feature.png)

Flashback is a Formula 1 statistics app!

### Style guide

![styleguide](res/styleguide.png)

### Architecture

![architecture](res/architecture.png)

### Configuring notifications

- [Dashboard](https://console.firebase.google.com/project/f1stats-live/notification)
- In app messaging says it's "30 minutes before the race"
- Channels:
    - `race`
    - `qualifying`
    - `seasonInfo`

### Libraries

- MVVM
- Koin
- Firebase
- Kotlin Coroutines
- LiveData
