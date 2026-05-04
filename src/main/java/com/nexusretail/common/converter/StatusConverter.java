package com.nexusretail.common.converter;

import com.nexusretail.common.constant.Status;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter extends BaseEnumConverter<Status, Integer>{

     public StatusConverter() {
        super(Status.class);
    }
}