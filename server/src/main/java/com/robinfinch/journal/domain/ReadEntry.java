package com.robinfinch.journal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import static com.robinfinch.journal.server.util.Utils.appendIfNotEmpty;
import static com.robinfinch.journal.server.util.Utils.isEmpty;

/**
 * Journal entry describing reading a {@link com.robinfinch.journal.domain.Title title}.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class ReadEntry extends JournalEntry {

    @Transient
    private Long titleId;

    @ManyToOne
    private Title title;

    private String part;

    public Long getTitleId() {
        return titleId;
    }

    public void setTitleId(Long titleId) {
        this.titleId = titleId;
    }

    @JsonIgnore
    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    @Override
    public String toPrettyString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Read");
        if (title != null) {
            appendIfNotEmpty(sb, " ", title.getAuthorName());
            if (!isEmpty(title.getAuthorName()) && !isEmpty(title.getTitle())) {
                sb.append(":");
            }
            appendIfNotEmpty(sb, " ", title.getTitle());
            appendIfNotEmpty(sb, " (", title.getYear(), ")");
        }
        appendIfNotEmpty(sb, ", ", part);
        sb.append(".");
        return sb.toString();
    }

    @Override
    public void prepareBeforeSend() {
        super.prepareBeforeSend();

        if (title == null) {
            titleId = 0L;
        } else {
            titleId = title.getId();
        }
    }

    @Override
    public void prepareAfterReceive(EntityManager em, JournalOwner owner) {
        super.prepareAfterReceive(em, owner);

        if (titleId == 0L) {
            title = null;
        } else {
            title = em.find(Title.class, titleId);
        }
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.ReadEntry[id=" + getId()
                + ";owner=" + getOwner()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";titleId=" + getTitleId()
                + ";title=" + getTitle()
                + ";part=" + getPart()
                + "]";
    }
}
