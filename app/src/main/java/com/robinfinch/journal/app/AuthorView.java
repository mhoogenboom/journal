package com.robinfinch.journal.app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.domain.Author;

/**
 * View displaying a author.
 *
 * @author Mark Hoogenboom
 */
public class AuthorView extends EditText {

    private Author author;

    public AuthorView(Context context) {
        super(context);
        setFocusable(false);
    }

    public AuthorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
    }

    public void setAuthor(Author author) {
        this.author = author;
        setText(Formatter.formatAuthor(author));
    }

    public Author getAuthor() {
        return author;
    }

    public long getAuthorId() {
        return (author == null) ? 0L : author.getId();
    }
}
