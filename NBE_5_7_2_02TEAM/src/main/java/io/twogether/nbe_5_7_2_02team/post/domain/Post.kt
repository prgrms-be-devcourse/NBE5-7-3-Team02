package io.twogether.nbe_5_7_2_02team.post.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.post.dto.request.PostUpdateRequest
import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Post(
    @field:Column(nullable = false)
    var title: String,
    @field:Column(nullable = false, columnDefinition = "TEXT")
    @field:Lob
    var content: String,
    @field:Enumerated(EnumType.STRING)
    var recruitmentStatus: RecruitmentStatus,
    @field:JsonIgnore
    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "member_id")
    val member: Member,
) : BaseEntity() {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @field:ElementCollection
    @field:CollectionTable(name = "post_image", joinColumns = [JoinColumn(name = "post_id")])
    @field:Column(name = "image_url")
    @field:OrderColumn(name = "image_order") // 이미지 순서
    var imageUrls: List<String> = listOf()

    var recruitmentDeadline: LocalDate? = null

    @field:OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val recruitmentFields: MutableList<RecruitmentField> = arrayListOf()

    @field:OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val postTags: MutableList<PostTag> = arrayListOf()

    fun update(request: PostUpdateRequest) {
        title = request.title
        content = request.content
        recruitmentStatus = request.recruitmentStatus
        recruitmentDeadline = request.recruitmentDeadline
    }
}
