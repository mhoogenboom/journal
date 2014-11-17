package com.robinfinch.journal.app.ui;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.robinfinch.journal.app.R;
import com.robinfinch.journal.app.ui.adapter.JournalEntryListAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Base class of expandable list fragments.
 *
 * @author Mark Hogenboom
 */
public abstract class ExpandableListFragment extends Fragment {

    @InjectView(R.id.list)
    protected ExpandableListView list;

    protected JournalEntryListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        ButterKnife.inject(this, view);

        list.setAdapter(adapter);

        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Cursor cursor = (Cursor) adapter.getChild(groupPosition, childPosition);
                if (cursor == null) {
                    return false;
                } else {
                    int i = cursor.getColumnIndexOrThrow(BaseColumns._ID);
                    select(cursor.getLong(i));
                    return true;
                }
            }
        });


        return view;
    }

    protected abstract int getLayoutResId();

    @OnClick(R.id.entity_add)
    protected abstract void add();

    protected abstract void select(long id);

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }
}
