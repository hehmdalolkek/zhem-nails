package ru.zhem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.zhem.entity.constraints.NullOrNotBlank;

import java.util.Set;

@NamedEntityGraph(
        name = "appointment_entity-graph",
        attributeNodes = {
                @NamedAttributeNode("user"),
                @NamedAttributeNode("interval"),
                @NamedAttributeNode("services")
        }
)

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "zhem", name = "appointments")
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ZhemUser user;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interval_id", unique = true)
    private Interval interval;

    @Size(min = 2, max = 256)
    @NullOrNotBlank
    @Column(name = "details")
    private String details;

    @NotNull
    @ManyToMany
    @JoinTable(
            schema = "zhem",
            name = "appointments_services",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<ZhemService> services;

}
