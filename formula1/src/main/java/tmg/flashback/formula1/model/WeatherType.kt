package tmg.flashback.formula1.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.formula1.R
import tmg.flashback.strings.R.string

enum class WeatherType(
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int
) {
    CLEAR_SKY(
        label = string.weather_type_clear_sky,
        icon = R.drawable.weather_clear_sky
    ),
    CLOUDS_LIGHT(
        label = string.weather_type_clouds_light,
        icon = R.drawable.weather_clouds_light
    ),
    CLOUDS_SCATTERED(
        label = string.weather_type_clouds_scattered,
        icon = R.drawable.weather_clouds_scattered
    ),
    CLOUDS_BROKEN(
        label = string.weather_type_clouds_broken,
        icon = R.drawable.weather_clouds_broken
    ),
    CLOUDS_OVERCAST(
        label = string.weather_type_clouds_overcast,
        icon = R.drawable.weather_overcast
    ),
    RAIN_LIGHT(
        label = string.weather_type_rain_light,
        icon = R.drawable.weather_rain_light
    ),
    RAIN_MODERATE(
        label = string.weather_type_rain_moderate,
        icon = R.drawable.weather_rain_moderate
    ),
    RAIN_HEAVY(
        label = string.weather_type_rain_heavy,
        icon = R.drawable.weather_rain_heavy
    ),
    THUNDERSTORM(
        label = string.weather_type_thunderstorm,
        icon = R.drawable.weather_thunderstorms
    ),
    SNOW(
        label = string.weather_type_snow,
        icon = R.drawable.weather_snow
    ),
    SAND(
        label = string.weather_type_sand,
        icon = R.drawable.weather_sand
    ),
    FOG(
        label = string.weather_type_fog,
        icon = R.drawable.weather_fog
    ),
    MIST(
        label = string.weather_type_mist,
        icon = R.drawable.weather_mist
    );
}