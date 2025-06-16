package io.twogether.nbe_5_7_2_02team.member.dto.request

import org.springframework.web.multipart.MultipartFile

// update인데 nickname과 image를 안바꿀수도 있기때문에
data class UpdateProfileRequest(

    val image: MultipartFile?,
    val nickname: String?

)
