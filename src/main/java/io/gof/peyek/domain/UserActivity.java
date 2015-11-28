package io.gof.peyek.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import io.gof.peyek.domain.enumeration.Type;

/**
 * A UserActivity.
 */
@Entity
@Table(name = "user_activity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserActivity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @ManyToOne
    private User source;

    @ManyToOne
    private Contractor target;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public User getSource() {
        return source;
    }

    public void setSource(User user) {
        this.source = user;
    }

    public Contractor getTarget() {
        return target;
    }

    public void setTarget(Contractor contractor) {
        this.target = contractor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserActivity userActivity = (UserActivity) o;

        if ( ! Objects.equals(id, userActivity.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserActivity{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
