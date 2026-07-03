package com.nexusretail.features.organization.office;

import com.nexusretail.common.exception.OfficeNotFoundException;
import com.nexusretail.data.models.Office;
import com.nexusretail.data.repositories.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfficeRepositoryWrapper {

    private final OfficeRepository repository;

    public Office save(final Office office) {
        return repository.save(office);
    }

    public Office saveAndFlush(final Office office) {
        return repository.saveAndFlush(office);
    }

    public Office findOneWithNotFoundDetection(final Long id) {
        return repository.findById(id).orElseThrow(() -> new OfficeNotFoundException(id));
    }

    /**
     * Equivalent to Fineract's findOfficeHierarchy — loads an office with
     * its full subtree of children eagerly, for privilege checks.
     */
    public Office findOfficeHierarchy(final Long id) {
        return repository.findByIdWithChildren(id).orElseThrow(() -> new OfficeNotFoundException(id));
    }
}