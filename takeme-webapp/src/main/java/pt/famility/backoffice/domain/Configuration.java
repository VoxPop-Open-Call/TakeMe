package pt.famility.backoffice.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * A Configuration.
 */
@Entity
@Table(name = "configuration")
public class Configuration {

    @Id
    @NotBlank
    @Column(name = "jhi_key", nullable = false)
    private String key;

    @Column(name = "jhi_value")
    private String value;

    @Column(name = "description")
    private String description;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
