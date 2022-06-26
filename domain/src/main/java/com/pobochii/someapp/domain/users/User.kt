package com.pobochii.someapp.domain.users

import java.util.*

/**
 * User model
 */
data class User(
    val id: Int = 0,
    val name: String = "",
    val reputation: Int = 0,
    val profileImage: String = "",
    val topTags: List<Tag> = emptyList(),
    val location: String = "",
    val creation: Date = Date(0),
    val badges: Badges = Badges.EMPTY
) {
    data class Badges(val bronze: Int = 0, val silver: Int = 0, val gold: Int = 0) {
        override fun toString() = "B:$bronze, S:$silver, G:$gold"

        companion object {
            val EMPTY = Badges()
        }
    }

    data class Tag(val name: String, val answer: Stats, val question: Stats) {
        override fun toString() =
            "$name (a:$answer, q:$question)"

        data class Stats(val count: Int = 0, val score: Int = 0) {
            override fun toString() = "c:$count, s:$score"
        }
    }
}
