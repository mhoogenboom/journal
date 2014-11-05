package com.robinfinch.journal.app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CursorAdapter;

import com.robinfinch.journal.app.R;
import com.robinfinch.journal.app.persistence.JournalEntryContract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Adapter for expandable list view showing journal entries.
 *
 * @author Mark Hoogenboom
 */
public class JournalEntryListAdapter extends BaseExpandableListAdapter {

    private final List<JournalEntryGroup> groups;

    private final CursorAdapter adapter;

    public JournalEntryListAdapter(CursorAdapter adapter) {
        this.groups = new ArrayList<>();
        this.adapter = adapter;
    }

    public void swapCursor(Cursor cursor) {
        groups.clear();

        if ((cursor != null) && cursor.moveToFirst()) {
            int year = -1;
            int offset = 0;
            int length = 0;

            do {
                int index = cursor.getColumnIndexOrThrow(JournalEntryContract.COL_DAY_OF_ENTRY);
                long day = cursor.getLong(index);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(day);

                if (calendar.get(Calendar.YEAR) != year) {
                    if (year != -1) {
                        groups.add(new JournalEntryGroup(year, offset, length));
                    }

                    year = calendar.get(Calendar.YEAR);
                    offset += length;
                    length = 0;
                }

                length++;
            } while (cursor.moveToNext());

            if (year != -1) {
                groups.add(new JournalEntryGroup(year, offset, length));
            }
        }

        adapter.swapCursor(cursor);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groups.get(groupPosition).year;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = newGroupView(parent.getContext(), parent);
        }
        JournalEntryGroup group = groups.get(groupPosition);
        bindGroupView(convertView, group);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).length;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        int pos = groups.get(groupPosition).offset + childPosition;
        return adapter.getItem(pos);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        int pos = groups.get(groupPosition).offset + childPosition;
        return adapter.getItemId(pos);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        int pos = groups.get(groupPosition).offset + childPosition;
        return adapter.getView(pos, convertView, parent);
    }

    @Override
    public boolean hasStableIds() {
        return adapter.hasStableIds();
    }

    @Override
    public void registerDataSetObserver (DataSetObserver observer) {
        adapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver (DataSetObserver observer) {
        adapter.unregisterDataSetObserver(observer);
    }

    private View newGroupView(Context context, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.journal_entry_group_list_item, parent, false);

        JournalEntryGroupViewHolder viewHolder = new JournalEntryGroupViewHolder(view);
        view.setTag(viewHolder);

        return view;    }

    private void bindGroupView(View view, JournalEntryGroup group) {
        JournalEntryGroupViewHolder viewHolder = (JournalEntryGroupViewHolder) view.getTag();
        viewHolder.bind(group);
    }
}
