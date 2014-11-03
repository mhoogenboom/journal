package com.robinfinch.journal.domain;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static com.robinfinch.journal.server.util.Utils.LOG_TAG;

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

    private String deletedEntityClass;

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

    public String getDeletedEntityClass() {
        return deletedEntityClass;
    }

    public void setDeletedEntityClass(String deletedEntityClass) {
        this.deletedEntityClass = deletedEntityClass;
    }

    public Long getDeletedEntityId() {
        return deletedEntityId;
    }

    public void setDeletedEntityId(Long deletedEntityId) {
        this.deletedEntityId = deletedEntityId;
    }

    public SyncableObject getDeletedEntity() {
        SyncableObject object = null;
        try {
            object = (SyncableObject) Class.forName(deletedEntityClass).newInstance();
            object.setId(deletedEntityId);
        } catch (ClassNotFoundException| IllegalAccessException | InstantiationException | ClassCastException e) {
            Logger.getLogger(LOG_TAG).log(Level.WARNING, "Can't instantiate " + deletedEntityClass, e);
        }
        return object;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.SyncLog[id=" + getId()
                + ";modifier=" + getModifier()
                + ";changedEntity=" + getChangedEntity()
                + ";deletedEntityClass=" + getDeletedEntityClass()
                + ";deletedEntityId=" + getDeletedEntityId()
                + "]";
    }
}
