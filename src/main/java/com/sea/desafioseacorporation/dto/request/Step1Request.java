package com.sea.desafioseacorporation.dto.request;

import com.sea.desafioseacorporation.entity.Priority;
import com.sea.desafioseacorporation.entity.ServiceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Step1Request {
    private ServiceType serviceType;

    private String title;

    private String description;

    private Priority priority;

}
