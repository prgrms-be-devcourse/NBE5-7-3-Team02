package io.twogether.nbe_5_7_2_02team.post.util

import io.twogether.nbe_5_7_2_02team.post.dao.PostRepository
import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentStatus
import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentStatus.RECRUITING
import org.slf4j.LoggerFactory.getLogger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class PostBatchScheduler(
    private val postRepository: PostRepository
) {
    private val log = getLogger(PostBatchScheduler::class.java)

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    fun updateExpiredRecruitmentPosts() {
        val today = LocalDate.now()
        val expiredPosts =
            postRepository.findByRecruitmentStatusAndRecruitmentDeadlineBefore(
                RECRUITING, today
            )

        if (expiredPosts.isEmpty()) {
            return
        }

        log.info("{}개의 모집글이 DONE으로 변경됩니다", expiredPosts.size)
        expiredPosts.forEach { post ->
            post.recruitmentStatus = RecruitmentStatus.DONE
            log.info("[{}] RECRUITING -> DONE", post.title)

        }
    }
}
