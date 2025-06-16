package io.twogether.nbe_5_7_2_02team.post.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class PostApplyRequest {

    public PostApplyRequest() {}

    public PostApplyRequest(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    @NotBlank(message = "모집 분야는 필수입니다.")
    @JsonProperty("fieldName")
    private String fieldName;
}
