package io.twogether.nbe_5_7_2_02team.post.dto.request;

import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentStatus;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostUpdateRequest {

    public PostUpdateRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public RecruitmentStatus getRecruitmentStatus() {
        return recruitmentStatus;
    }

    public void setRecruitmentStatus(RecruitmentStatus recruitmentStatus) {
        this.recruitmentStatus = recruitmentStatus;
    }

    public LocalDate getRecruitmentDeadline() {
        return recruitmentDeadline;
    }

    public void setRecruitmentDeadline(LocalDate recruitmentDeadline) {
        this.recruitmentDeadline = recruitmentDeadline;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<RecruitmentFieldRequest> getRecruitmentFields() {
        return recruitmentFields;
    }

    public void setRecruitmentFields(List<RecruitmentFieldRequest> recruitmentFields) {
        this.recruitmentFields = recruitmentFields;
    }

    private String title;
    private String content;
    private RecruitmentStatus recruitmentStatus;
    private LocalDate recruitmentDeadline;
    private List<MultipartFile> images;
    private List<String> tags;
    private List<RecruitmentFieldRequest> recruitmentFields = new ArrayList<>();
}
