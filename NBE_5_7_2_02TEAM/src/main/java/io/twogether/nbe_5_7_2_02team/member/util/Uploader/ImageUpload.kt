package io.twogether.nbe_5_7_2_02team.member.util.Uploader

import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Component
class ImageUpload(
    @Value("\${file.upload-dir}")
    private val baseUploadDir: String,
) {
    fun saveProfileImage(
        file: MultipartFile?,
        memberId: Long,
    ): String? {
        if (file == null || file.isEmpty) return null
        return runCatching {
            val uploadRoot: Path = Paths.get(baseUploadDir).toAbsolutePath().normalize()
            val memberDir = uploadRoot.resolve("member").resolve(memberId.toString())

            Files.createDirectories(memberDir)

            val fileName = "${UUID.randomUUID()}_${file.originalFilename}"
            val filePath = memberDir.resolve(fileName)

            file.transferTo(filePath) // MultipartFile 확장
            "/uploads/member/$memberId/$fileName" // URL 리턴
        }.getOrElse {
            throw ErrorException(ErrorCode.IMAGE_UPLOAD_FAILED)
        }
    }
}
