package com.robinfinch.journal.app.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

import com.robinfinch.journal.app.util.DirUriType;

/**
 *
 * @author Mark Hoogenboom
 */
public class AutoCompleteAdapter extends SimpleCursorAdapter {

    public AutoCompleteAdapter(final Context context, final DirUriType uriType, final String colName) {
        super(context, android.R.layout.simple_list_item_1, null,
                new String[]{colName},
                new int[]{android.R.id.text1},
                0);

        setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence searchString) {
                String[] projection = {
                        "DISTINCT 1 AS _id",
                        colName
                };
                String selection = colName + " LIKE ?";
                String[] selectionArgs = { "%" + searchString + "%" };
                return context.getContentResolver()
                        .query(uriType.uri(context), projection, selection, selectionArgs, null);
            }
        });

        setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public CharSequence convertToString(Cursor cursor) {
                int index = cursor.getColumnIndexOrThrow(colName);
                return cursor.getString(index);
            }
        });
    }
}
