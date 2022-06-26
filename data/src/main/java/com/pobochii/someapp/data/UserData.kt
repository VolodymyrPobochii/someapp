package com.pobochii.someapp.data

import com.google.gson.annotations.SerializedName
import com.pobochii.someapp.domain.users.User
import java.util.*

class UserData {
    @SerializedName("badge_counts")
    val badgeCounts: BadgeCounts = BadgeCounts()

    @SerializedName("account_id")
    val accountId: Int = 0

    @SerializedName("is_employee")
    val isEmployee: Boolean = false

    @SerializedName("last_modified_date")
    val lastModified: Date? = null

    @SerializedName("last_access_date")
    val lastAccess: Date? = null

    @SerializedName("creation_date")
    val created: Date? = null

    @SerializedName("reputation_change_year")
    val reputationChangeYear: Int = 0

    @SerializedName("reputation_change_quarter")
    val reputationChangeQuarter: Int = 0

    @SerializedName("reputation_change_month")
    val reputationChangeMonth: Int = 0

    @SerializedName("reputation_change_week")
    val reputationChangeWeek: Int = 0

    @SerializedName("reputation_change_day")
    val reputationChangeDay: Int = 0

    @SerializedName("reputation")
    val reputation: Int = 0

    @SerializedName("user_type")
    val userType: String = ""

    @SerializedName("user_id")
    val userId: Int = 0

    @SerializedName("accept_rate")
    val acceptRate: Int = 0

    @SerializedName("location")
    val location: String = ""

    @SerializedName("website_url")
    val websiteUrl: String = ""

    @SerializedName("link")
    val link: String = ""

    @SerializedName("profile_image")
    val profileImageUrl: String = ""

    @SerializedName("display_name")
    val displayName: String = ""

    class BadgeCounts {
        @SerializedName("bronze")
        val bronze: Int = 0

        @SerializedName("silver")
        val silver: Int = 0

        @SerializedName("gold")
        val gold: Int = 0
    }
}

/**
 * [UserData] to domain [User] mapper
 */
fun UserData.asUser(topTags: List<User.Tag> = emptyList()) = User(
    userId,
    displayName,
    reputation,
    profileImageUrl,
    topTags,
    location,
    created ?: Date(0),
    badgeCounts.asUserBadges()
)

fun UserData.BadgeCounts.asUserBadges() = User.Badges(bronze, silver, gold)
