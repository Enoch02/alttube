package com.enoch02.alttube.navigation

data class MainDestinationInfo(
    val name: String,
    val icon: Int,
)

enum class MainDestination {
    FEED,
    UPLOAD,
    FAVORITE,
    PROFILE
}
