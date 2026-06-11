package com.nexusretail.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(
        name = "system_menu",
        indexes = {
                @Index(name = "index_system_menu_name", columnList = "name")
        }
)
public class SystemMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String route;

    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private SystemMenu parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    private List<SystemMenu> children = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "menu_level", nullable = false)
    private MenuLevel menuLevel;

    @Column(name = "display_order")
    private int displayOrder;

    public enum MenuLevel { MENU, SUBMENU, CHILD }

    @Column(nullable = false)
    private boolean is_active;

}
