package com.robinfinch.journal.app.util;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Directory type of uniform resource identifier of content.
 *
 * @author Mark Hoogenboom
 */
public class DirUriType extends UriType {

    public DirUriType(String entityName, String joinedEntities) {
        super(entityName, joinedEntities);
    }

	@Override
    public String match() {
        return getEntityName();
    }

    public Uri uri() {
        return new Uri.Builder()
                .scheme("content")
                .authority(AUTHORITY)
                .appendPath(getEntityName())
                .build();
    }

    public ItemUriType toItemType() {
        return new ItemUriType(getEntityName(), getJoinedEntities());
    }

    @Override
    public String toString() {
        return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + getEntityName();
    }
}
