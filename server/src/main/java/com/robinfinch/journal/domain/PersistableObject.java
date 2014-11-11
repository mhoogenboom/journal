package com.robinfinch.journal.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Base class for all classes that are stored in the db.
 *
 * @author Mark Hoogenboom
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PersistableObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        PersistableObject that = (PersistableObject) object;
        if (this.id == null) {
            return (that.id == null);
        } else {
            return (that.id != null) && this.id.equals(that.id);
        }
    }
}
