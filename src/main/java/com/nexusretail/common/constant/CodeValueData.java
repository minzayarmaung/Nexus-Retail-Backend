package com.nexusretail.common.constant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CodeValueData {

    private Long id;
    private String name;
    private Integer position;
    private String description;
    private Boolean active;
    private Boolean mandatory;
}
