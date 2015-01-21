package com.robinfinch.journal.app.ui;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.robinfinch.journal.app.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Base class of list fragments.
 *
 * @author Mark Hoogenboom
 */
public abstract class ListFragment extends Fragment {

    @InjectView(R.id.list_header)
    protected TextView listHeader;

    @InjectView(R.id.list)
    protected ListView list;

    @InjectView(R.id.list_add)
    protected ImageButton listAddButton;

    protected CursorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        ButterKnife.inject(this, view);

        listHeader.setText(getHeaderResId());

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) adapter.getItem(position);
                if (cursor != null) {
                    int i = cursor.getColumnIndexOrThrow(BaseColumns._ID);
                    select(cursor.getLong(i));
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

    protected abstract void select(long id);

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }
}
