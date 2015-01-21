package com.robinfinch.journal.app.ui;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.robinfinch.journal.app.R;
import com.robinfinch.journal.app.ui.adapter.JournalEntryListAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Base class of expandable list fragments.
 *
 * @author Mark Hoogenboom
 */
public abstract class ExpandableListFragment extends Fragment {

    @InjectView(R.id.list_header)
    protected TextView listHeader;

    @InjectView(R.id.list)
    protected ExpandableListView list;

    @InjectView(R.id.list_add)
    protected ImageButton listAddButton;

    protected JournalEntryListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expandable_list_fragment, container, false);
        ButterKnife.inject(this, view);

        listHeader.setText(getHeaderResId());

        list.setAdapter(adapter);

        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Cursor cursor = (Cursor) adapter.getChild(groupPosition, childPosition);
                if (cursor == null) {
                    return false;
                } else {
                    select(cursor);
                    return true;
                }
            }
        });

        return view;
    }

    protected abstract int getHeaderResId();

    // TODO: remove
    protected abstract int getAddButtonResId();

    @OnClick(R.id.list_add)
    protected abstract void add();

    protected void select(Cursor cursor) {
        int i = cursor.getColumnIndexOrThrow(BaseColumns._ID);
        select(cursor.getLong(i));
    }

    protected abstract void select(long id);

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }
}
