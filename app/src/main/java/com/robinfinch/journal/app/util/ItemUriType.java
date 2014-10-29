package com.robinfinch.journal.app.util;

import android.content.ContentResolver;
import android.net.Uri;

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

    public Uri uri(long id) {
        return new Uri.Builder()
                .scheme("content")
                .authority(AUTHORITY)
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
