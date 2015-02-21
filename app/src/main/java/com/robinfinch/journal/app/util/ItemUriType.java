package com.robinfinch.journal.app.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.robinfinch.journal.app.R;

/**
 * Item type of uniform resource identifier of content.
 *
 * @author Mark Hoogenboom
 */
public class ItemUriType extends UriType {

    public ItemUriType(String entityName, String joinedEntities) {
        super(entityName, joinedEntities);
    }

	@Override
    public String match() {
        return getEntityName() + "/#";
    }

    public Uri uri(Context context, long id) {
        return new Uri.Builder()
                .scheme("content")
                .authority(context.getString(R.string.content_authority))
                .appendPath(getEntityName())
                .appendPath(Long.toString(id))
                .build();
    }

    public DirUriType toDirType() {
        return new DirUriType(getEntityName(), getJoinedEntities());
    }

    @Override
    public String toString() {
        return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + getEntityName();
    }
}
