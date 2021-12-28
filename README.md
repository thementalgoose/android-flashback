# Flashback - Android 

[![main](https://github.com/thementalgoose/android-flashback/workflows/Main/badge.svg)](https://github.com/thementalgoose/android-flashback/actions)
![latest-version](https://shields.io/badge/Latest%20Version-7.1.3-blue)
![pipeline](https://github.com/thementalgoose/android-flashback/actions/workflows/main/badge.svg)

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
