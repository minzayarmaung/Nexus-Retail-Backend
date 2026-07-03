package com.nexusretail.features.organization.office.service.impl;

import com.nexusretail.features.organization.office.dto.request.OfficeRequest;
import com.nexusretail.features.organization.office.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    @Transactional
    @Override
    public String createOffice(OfficeRequest officeRequest) {
        return "";
    }
}
