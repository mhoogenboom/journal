package com.robinfinch.journal.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.domain.NamedObject;

/**
 * View displaying a named object.
 *
 * @author Mark Hoogenboom
 */
public class NamedObjectView<T extends NamedObject> extends EditText {

    private T obj;

    public NamedObjectView(Context context) {
        super(context);
        setFocusable(false);
    }

    public NamedObjectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
    }

    public void setObject(T obj) {
        this.obj = obj;
        setText(Formatter.formatNamedObject(obj));
    }

    public T getObject() {
        return obj;
    }

    public long getObjectId() {
        return (obj == null) ? 0L : obj.getId();
    }
}
