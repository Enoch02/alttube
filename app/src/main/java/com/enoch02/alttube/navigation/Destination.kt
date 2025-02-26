package com.enoch02.alttube.navigation

sealed class Destination(val route: String) {
    data object MainScaffold: Destination("video_feed")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}