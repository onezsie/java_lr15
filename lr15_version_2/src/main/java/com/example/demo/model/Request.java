package com.example.demo.model;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Request {
    @NotBlank
    @Size(max = 32)
    private String uid;

    @NotBlank
    @Size(max = 32)
    private String operationUid;

    private String systemName;
    @NotBlank
    private String systemTime;

    private String source;
    private int communicationId;
    private int templateId;

    @Max(7)
    private int productCode;

    @Max(5)
    private int smsCode;
}
