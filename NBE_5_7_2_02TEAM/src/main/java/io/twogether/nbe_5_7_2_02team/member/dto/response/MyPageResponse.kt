package io.twogether.nbe_5_7_2_02team.member.dto.response

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.post.domain.Post

data class MyPageResponse(

    val id: Long,
    val email: String,
    val name: String,
    val job: String,
    val course: String,
    val profileImage: String?,
    val posts: List<Post?>,

    val followerCount: Long?,
    val followingCount: Long?,

    val following: Boolean,
    val owner: Boolean

) {
    companion object {
        @JvmStatic
        fun of(
            member : Member,
            posts: List<Post?>,
            followerCount: Long?,
            followingCount: Long?,
            folioing: Boolean,
            owner: Boolean
        ): MyPageResponse =
            MyPageResponse(
                id = member.id!!,
                email = member.email,
                name = member.name,
                job = member.job,
                course = member.course,
                profileImage = member.profileImage,
                posts = posts,
                followerCount = followerCount,
                followingCount = followingCount,
                following = folioing,
                owner = owner
            )
    }
}





