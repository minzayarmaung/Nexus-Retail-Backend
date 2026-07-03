package com.nexusretail.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "office", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }, name = "name_org") })
@Getter
@Setter
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Office> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Office parent;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "hierarchy", length = 50)
    private String hierarchy;

    @Column(name = "opening_date", nullable = false)
    private LocalDate openingDate;

    protected Office() {
        // JPA
    }

    public static Office createNew(final Office parent, final String name, final LocalDate openingDate) {
        final Office office = new Office();
        office.parent = parent;
        office.name = name;
        office.openingDate = openingDate;
        return office;
    }

    /**
     * Mirrors Fineract's Office.generateHierarchy() — must be called AFTER
     * the first save so office.getId() is populated.
     */
    public void generateHierarchy() {
        if (this.parent != null) {
            this.hierarchy = this.parent.hierarchyOf(this.id);
        } else {
            this.hierarchy = "." + this.id + ".";
        }
    }

    private String hierarchyOf(final Long id) {
        return this.hierarchy + id + ".";
    }

    public boolean identifiedBy(final Long officeId) {
        return this.id.equals(officeId);
    }

    /**
     * True if the given officeId is NOT this office or one of its descendants
     * in the hierarchy string. Mirrors Fineract's doesNotHaveAnOfficeInHierarchyWithId.
     */
    public boolean doesNotHaveAnOfficeInHierarchyWithId(final Long officeId) {
        return !hasAnOfficeInHierarchyWithId(officeId);
    }

    private boolean hasAnOfficeInHierarchyWithId(final Long officeId) {
        if (officeId == null) {
            return true; // no target office specified -> treat as "own office", same as Fineract default
        }
        if (identifiedBy(officeId)) {
            return true;
        }
        for (final Office child : this.children) {
            if (child.hasAnOfficeInHierarchyWithId(officeId)) {
                return true;
            }
        }
        return false;
    }
}