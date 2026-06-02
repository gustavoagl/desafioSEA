package com.sea.desafioseacorporation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AnalystCoverageResponse {

    private Long analystId;
    private List<String> states;
}