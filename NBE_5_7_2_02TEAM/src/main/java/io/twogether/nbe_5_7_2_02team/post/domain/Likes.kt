package io.twogether.nbe_5_7_2_02team.post.domain

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["member_id", "post_id"])])
class Likes (
    @field:ManyToOne
    @field:JoinColumn(name = "member_id")
    val member: Member,

    @field:ManyToOne
    @field:JoinColumn(name = "post_id")
    val post: Post
){
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null


}
