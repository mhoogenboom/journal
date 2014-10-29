package com.robinfinch.journal.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Keep track of changes to be synced.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class SyncLog extends PersistableObject {

    @ManyToOne
    private App modifier;

    @ManyToOne
    private SyncableObject changedEntity;

    private Long deletedEntityId;

    public App getModifier() {
        return modifier;
    }

    public void setModifier(App modifier) {
        this.modifier = modifier;
    }

    public SyncableObject getChangedEntity() {
        return changedEntity;
    }

    public void setChangedEntity(SyncableObject changedEntity) {
        this.changedEntity = changedEntity;
    }

    public Long getDeletedEntityId() {
        return deletedEntityId;
    }

    public void setDeletedEntityId(Long deletedEntityId) {
        this.deletedEntityId = deletedEntityId;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.SyncLog[id=" + getId()
                + ";modifier=" + getModifier()
                + ";changedEntity=" + getChangedEntity()
                + ";deletedEntityId=" + getDeletedEntityId()
                + "]";
    }
}
