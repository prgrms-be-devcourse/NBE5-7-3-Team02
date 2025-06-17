package io.twogether.nbe_5_7_2_02team.post.util

import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Component
class ImageUploader {
    @Value("\${file.upload-dir}")
    private val baseUploadDir: String? = null

    companion object {
        private const val MAX_IMAGES_COUNT = 10
    }

    fun saveImages(
        images: List<MultipartFile>,
        postId: Long,
    ): List<String> {
        if (images.size > MAX_IMAGES_COUNT) {
            throw ErrorException(ErrorCode.IMAGE_UPLOAD_LIMIT_EXCEEDED)
        }

        return images.map { image: MultipartFile -> saveImage(image, postId) }
    }

    private fun saveImage(
        file: MultipartFile,
        postId: Long,
    ): String {
        try {
            val uploadRootPath = Paths.get(baseUploadDir!!).toAbsolutePath().normalize()
            val postDir = uploadRootPath.resolve("post").resolve(postId.toString())

            Files.createDirectories(postDir)

            val fileName = UUID.randomUUID().toString() + "_" + file.originalFilename
            val filePath = postDir.resolve(fileName)
            file.transferTo(filePath.toFile())

            return "/uploads/post/$postId/$fileName"
        } catch (_: IOException) {
            throw ErrorException(ErrorCode.IMAGE_UPLOAD_FAILED)
        }
    }

    fun deletePostImageByFolder(postId: Long) {
        val folderPath =
            Paths
                .get(baseUploadDir!!)
                .toAbsolutePath()
                .normalize()
                .resolve("post")
                .resolve(postId.toString())

        try {
            if (Files.exists(folderPath)) {
                Files
                    .walk(folderPath)
                    .sorted(Comparator.reverseOrder())
                    .map { obj: Path -> obj.toFile() }
                    .forEach { obj: File -> obj.delete() }
            }
        } catch (_: IOException) {
            throw ErrorException(ErrorCode.IMAGE_DELETE_FAILED)
        }
    }
}
