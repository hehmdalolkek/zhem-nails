package ru.zhem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.zhem.common.constraint.CheckPhoneNumber;
import ru.zhem.common.constraint.NullOrNotBlank;

import java.util.Set;

@NamedEntityGraph(
        name = "zhem-user_entity-graph",
        attributeNodes = {
                @NamedAttributeNode("roles")
        }
)

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "zhem", name = "users")
public class ZhemUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @CheckPhoneNumber
    @Column(name = "phone", unique = true)
    private String phone;

    @NotBlank
    @Size(max = 256)
    @Column(name = "password")
    private String password;

    @Email
    @Size(max = 256)
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank
    @Size(min = 2, max = 32)
    @Column(name = "first_name")
    private String firstName;

    @Size(min = 2, max = 32)
    @NullOrNotBlank
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @ManyToMany
    @JoinTable(
            schema = "zhem",
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "[PROTECTED]";
    }

}

