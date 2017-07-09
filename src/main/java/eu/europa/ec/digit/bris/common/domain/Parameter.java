package eu.europa.ec.digit.bris.common.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Parameter.
 */
@Entity
@Table(name = "parameter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "parameter")
public class Parameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain")
    private String domain;

    @Column(name = "parameter")
    private String parameter;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "jhi_value")
    private String value;

    @Column(name = "creation_user")
    private String creationUser;

    @Column(name = "creation_date_time")
    private ZonedDateTime creationDateTime;

    @Column(name = "last_modified_user")
    private String lastModifiedUser;

    @Column(name = "last_modified_date_time")
    private ZonedDateTime lastModifiedDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public Parameter domain(String domain) {
        this.domain = domain;
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getParameter() {
        return parameter;
    }

    public Parameter parameter(String parameter) {
        this.parameter = parameter;
        return this;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getName() {
        return name;
    }

    public Parameter name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Parameter description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public Parameter value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreationUser() {
        return creationUser;
    }

    public Parameter creationUser(String creationUser) {
        this.creationUser = creationUser;
        return this;
    }

    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    public ZonedDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public Parameter creationDateTime(ZonedDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
        return this;
    }

    public void setCreationDateTime(ZonedDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getLastModifiedUser() {
        return lastModifiedUser;
    }

    public Parameter lastModifiedUser(String lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
        return this;
    }

    public void setLastModifiedUser(String lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
    }

    public ZonedDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public Parameter lastModifiedDateTime(ZonedDateTime lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
        return this;
    }

    public void setLastModifiedDateTime(ZonedDateTime lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Parameter parameter = (Parameter) o;
        if (parameter.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), parameter.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Parameter{" +
            "id=" + getId() +
            ", domain='" + getDomain() + "'" +
            ", parameter='" + getParameter() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", value='" + getValue() + "'" +
            ", creationUser='" + getCreationUser() + "'" +
            ", creationDateTime='" + getCreationDateTime() + "'" +
            ", lastModifiedUser='" + getLastModifiedUser() + "'" +
            ", lastModifiedDateTime='" + getLastModifiedDateTime() + "'" +
            "}";
    }
}
