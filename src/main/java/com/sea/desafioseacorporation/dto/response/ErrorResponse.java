package com.sea.desafioseacorporation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {

    private String message;

    private Integer status;

    private LocalDateTime timestamp;
}