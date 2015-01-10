package com.robinfinch.journal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import static com.robinfinch.journal.server.util.Utils.appendIfNotEmpty;
import static com.robinfinch.journal.server.util.Utils.compare;
import static com.robinfinch.journal.server.util.Utils.isEmpty;

/**
 * A written work.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class Title extends SyncableObject implements Comparable<Title> {

    private String title;

    @Transient
    private Long authorId;

    @ManyToOne
    private Author author;

    private String year;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    @JsonIgnore
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getAuthorName() {
        return (author == null) ? null : author.getName();
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public void prepareBeforeSend() {
        super.prepareBeforeSend();

        if (author == null) {
            authorId = 0L;
        } else {
            authorId = author.getId();
        }
    }

    @Override
    public void prepareAfterReceive(EntityManager em, JournalOwner owner) {
        super.prepareAfterReceive(em, owner);

        if (authorId == 0L) {
            author = null;
        } else {
            author = em.find(Author.class, authorId);
        }
    }

    @Override
    public int compareTo(Title that) {
        int c = compare(this.author, that.author);
        if (c == 0) {
            c = compare(this.year, that.year);
        }
        if (c == 0) {
            c = (int) (this.getId() - that.getId());
        }
        return c;
    }

    public CharSequence toPrettyString() {
        StringBuilder sb = new StringBuilder();
        appendIfNotEmpty(sb, " ", getAuthorName());
        if (!isEmpty(getAuthorName()) && !isEmpty(title)) {
            sb.append(":");
        }
        appendIfNotEmpty(sb, " ", title);
        appendIfNotEmpty(sb, " (", year, ")");
        return sb;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Title[id=" + getId()
                + ";owner=" + getOwner()
                + ";title=" + getTitle()
                + ";authorId=" + getAuthorId()
                + ";author=" + getAuthor()
                + ";year=" + getYear()
                + "]";
    }
}
