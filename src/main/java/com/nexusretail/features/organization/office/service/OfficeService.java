package com.nexusretail.features.organization.office.service;

import com.nexusretail.features.organization.office.dto.request.OfficeRequest;
import com.nexusretail.features.organization.office.dto.response.OfficeResponse;

public interface OfficeService {
    OfficeResponse createOffice(OfficeRequest officeRequest);
}
