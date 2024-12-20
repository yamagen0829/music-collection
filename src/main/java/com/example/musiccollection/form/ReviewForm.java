package com.example.musiccollection.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewForm {
    @NotNull(message = "１〜５段階で評価をお願いします。")
    @Min(value = 1, message = "スコアは最低１点です。")
    @Max(value = 5, message = "スコアは最高５点です。")
    private Integer score;
    
    @NotBlank(message = "コメントをお願いします。")
    private String content;
}
