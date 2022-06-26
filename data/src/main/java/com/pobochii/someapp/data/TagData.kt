package com.pobochii.someapp.data

import com.google.gson.annotations.SerializedName
import com.pobochii.someapp.domain.users.User

class TagData {

    @SerializedName("user_id")
    val userId: Int = 0

    @SerializedName("answer_count")
    val answerCount: Int = 0

    @SerializedName("answer_score")
    val answerScore: Int = 0

    @SerializedName("question_count")
    val questionCount: Int = 0

    @SerializedName("question_score")
    val questionScore: Int = 0

    @SerializedName("tag_name")
    val tagName: String = ""
}

/**
 * [TagData] to [User.Tag] mapper
 */
fun TagData.asTag() = User.Tag(
    tagName,
    User.Tag.Stats(answerCount, answerScore),
    User.Tag.Stats(questionCount, questionScore)
)
