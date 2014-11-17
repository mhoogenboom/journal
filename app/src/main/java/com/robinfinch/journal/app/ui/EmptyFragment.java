package com.robinfinch.journal.app.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robinfinch.journal.app.R;

import butterknife.ButterKnife;

/**
 * Empty fragment, used in two pane layout when no list item is selected.
 *
 * @author Mark Hoogenboom
 */
public class EmptyFragment extends Fragment {

    public static EmptyFragment newInstance() {
        EmptyFragment fragment = new EmptyFragment();
        return fragment;
    }

    private Parent listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (Parent) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empty_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    public interface Parent {

    }
}
