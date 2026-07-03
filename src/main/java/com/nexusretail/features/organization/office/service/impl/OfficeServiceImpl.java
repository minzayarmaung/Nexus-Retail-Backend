package com.nexusretail.features.organization.office.service.impl;

import com.nexusretail.common.exception.NoAuthorizationException;
import com.nexusretail.data.models.Office;
import com.nexusretail.data.models.User;
import com.nexusretail.features.organization.office.OfficeRepositoryWrapper;
import com.nexusretail.features.organization.office.dto.request.OfficeRequest;
import com.nexusretail.features.organization.office.dto.response.OfficeResponse;
import com.nexusretail.features.organization.office.service.OfficeService;
import com.nexusretail.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepositoryWrapper officeRepositoryWrapper;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    @Override
    public OfficeResponse createOffice(final OfficeRequest officeRequest) {
        try {
            final User currentUser = currentUserProvider.getCurrentUser();

            final Office parent = validateUserPrivilegeOnOfficeAndRetrieve(currentUser, officeRequest.parentId());

            final Office office = Office.createNew(parent, officeRequest.name(), LocalDate.parse(officeRequest.openingDate()));

            // pre-save to generate an id for use in the hierarchy string
            officeRepositoryWrapper.saveAndFlush(office);
            office.generateHierarchy();
            officeRepositoryWrapper.save(office);

            return OfficeResponse.builder()
                    .officeId(office.getId())
                    .hierarchy(office.getHierarchy())
                    .build();

        } catch (final DataIntegrityViolationException dve) {
            handleOfficeDataIntegrityIssues(officeRequest, dve);
            throw dve;
        }
    }

    private Office validateUserPrivilegeOnOfficeAndRetrieve(final User currentUser, final Long officeId) {
        final Long userOfficeId = currentUser.getOffice().getId();
        final Office userOffice = officeRepositoryWrapper.findOfficeHierarchy(userOfficeId);

        if (userOffice.doesNotHaveAnOfficeInHierarchyWithId(officeId)) {
            throw new NoAuthorizationException("User does not have sufficient privileges to act on the provided office.");
        }

        Office officeToReturn = userOffice;
        if (officeId != null && !userOffice.identifiedBy(officeId)) {
            officeToReturn = officeRepositoryWrapper.findOfficeHierarchy(officeId);
        }
        return officeToReturn;
    }

    private void handleOfficeDataIntegrityIssues(final OfficeRequest request, final DataIntegrityViolationException dve) {
        final Throwable rootCause = dve.getMostSpecificCause();
        if (rootCause.getMessage() != null && rootCause.getMessage().contains("name_org")) {
            throw new IllegalStateException("An office with name '" + request.name() + "' already exists.", dve);
        }
        throw new IllegalStateException("Unknown data integrity issue creating office.", dve);
    }
}