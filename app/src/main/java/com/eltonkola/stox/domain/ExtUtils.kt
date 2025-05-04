package com.eltonkola.stox.domain

private const val SECOND_MILLIS = 1000L
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS
private const val WEEK_MILLIS = 7 * DAY_MILLIS
private const val MONTH_MILLIS = 30 * DAY_MILLIS
private const val YEAR_MILLIS = 365 * DAY_MILLIS

fun Long.timeAgo(currentTimeMillis: Long = System.currentTimeMillis()): String {

    val timeDifference = currentTimeMillis - this

    return when {
        timeDifference < 0 -> {
            "in the future"
        }

        timeDifference < 5 * SECOND_MILLIS -> "just now" // Less than 5 seconds
        timeDifference < MINUTE_MILLIS -> "${timeDifference / SECOND_MILLIS} seconds ago"
        timeDifference < HOUR_MILLIS -> {
            val minutes = (timeDifference / MINUTE_MILLIS).toInt()
            "$minutes ${if (minutes == 1) "minute" else "minutes"} ago"
        }
        timeDifference < DAY_MILLIS -> {
            val hours = (timeDifference / HOUR_MILLIS).toInt()
            "$hours ${if (hours == 1) "hour" else "hours"} ago"
        }
        timeDifference < WEEK_MILLIS -> { // Less than 7 days
            val days = (timeDifference / DAY_MILLIS).toInt()
            // Note: Doesn't handle "yesterday" specifically without calendar logic
            "$days ${if (days == 1) "day" else "days"} ago"
        }
        timeDifference < MONTH_MILLIS -> { // Use ~30 days as approx month threshold
            val weeks = (timeDifference / WEEK_MILLIS).toInt()
            "$weeks ${if (weeks == 1) "week" else "weeks"} ago"
        }
        timeDifference < YEAR_MILLIS -> { // Use ~365 days as approx year threshold
            val months = (timeDifference / MONTH_MILLIS).toInt()
            "$months ${if (months == 1) "month" else "months"} ago"
        }
        else -> {
            val years = (timeDifference / YEAR_MILLIS).toInt()
            "$years ${if (years == 1) "year" else "years"} ago"
        }
    }
}