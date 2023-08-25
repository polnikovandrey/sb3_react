package com.mcfly.template.domain.user_role;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"username"}),
                @UniqueConstraint(columnNames = {"email"})
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        @Size(max = 20)
        private String username;

        @NotBlank
        @Size(max = 40)
        @Email
        private String email;

        @NotBlank
        @Size(max = 100)
        private String password;

        @NotBlank
        @Size(max = 20)
        private String firstName;

        @NotBlank
        @Size(max = 20)
        private String lastName;

        @NotNull
        @Size(max = 20)
        private String middleName;

        @NotNull
        private boolean emailConfirmed;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id",
                nullable = false)
        )
        private Set<Role> roles = new HashSet<>();

        @CreatedDate
        @Column(nullable = false, updatable = false)
        private Instant createdAt;

        @LastModifiedDate
        @Column(nullable = false)
        private Instant updatedAt;

        @Version
        @Column(nullable = false)
        private Long version;

        @Override
        public String toString() {
                return "User{" +
                       "id=" + id +
                       ", username='" + username + '\'' +
                       ", email='" + email + '\'' +
                       ", password='" + password + '\'' +
                       ", firstName='" + firstName + '\'' +
                       ", lastName='" + lastName + '\'' +
                       ", middleName='" + middleName + '\'' +
                       ", emailConfirmed=" + emailConfirmed +
                       ", roles=" + roles +
                       ", createdAt=" + createdAt +
                       ", updatedAt=" + updatedAt +
                       '}';
        }
}
