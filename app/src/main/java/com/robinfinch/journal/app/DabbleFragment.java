package com.robinfinch.journal.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robinfinch.journal.app.dabbler.AndroidPainter;
import com.robinfinch.journal.app.dabbler.DabblerModule;
import com.robinfinch.journal.app.dabbler.StrokeSample;
import com.robinfinch.journal.app.dabbler.StrokeSamplesAdapter;
import com.robinfinch.journal.dabbler.strokes.Stroke;
import com.robinfinch.journal.dabbler.strokes.StrokeFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import dagger.ObjectGraph;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Plan a drawing.
 *
 * @author Mark Hoogenboom
 */
public class DabbleFragment extends Fragment {

    public static DabbleFragment newInstance() {
        DabbleFragment fragment = new DabbleFragment();
        return fragment;
    }

    @InjectView(R.id.list)
    protected RecyclerView list;

    @InjectView(R.id.dabble_go)
    protected View goView;

    @InjectView(R.id.dabble_go_progress)
    protected View goProgressView;

    private StrokeSamplesAdapter adapter;

    @Inject
    AndroidPainter painter;

    private Parent listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (Parent) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraph.create(
                new ContextModule(getActivity().getApplicationContext()),
                new DabblerModule()
        ).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dabble_fragment, container, false);
        ButterKnife.inject(this, view);

        StrokeFactory factory = new StrokeFactory(System.currentTimeMillis());

        List<StrokeSample> samples = new ArrayList<>();
        samples.add(new StrokeSample(factory.createBasicStroke(), R.drawable.dabble_sample_basic_stroke));
        samples.add(new StrokeSample(factory.createBlackStroke(), R.drawable.dabble_sample_black_stroke));
        samples.add(new StrokeSample(factory.createLongThinDarkStroke(), R.drawable.dabble_sample_longthindark_stroke));
        samples.add(new StrokeSample(factory.createLongThinDarkRedStroke(), R.drawable.dabble_sample_longthindarkred_stroke));
        samples.add(new StrokeSample(factory.createLongThinDarkGreenStroke(), R.drawable.dabble_sample_longthindarkgreen_stroke));
        samples.add(new StrokeSample(factory.createLongThinDarkBlueStroke(), R.drawable.dabble_sample_longthindarkblue_stroke));
        samples.add(new StrokeSample(factory.createShortThickLightStroke(), R.drawable.dabble_sample_shortthicklight_stroke));
        samples.add(new StrokeSample(factory.createShortThickLightRedStroke(), R.drawable.dabble_sample_shortthicklightred_stroke));
        samples.add(new StrokeSample(factory.createShortThickLightGreenStroke(), R.drawable.dabble_sample_shortthicklightgreen_stroke));
        samples.add(new StrokeSample(factory.createShortThickLightBlueStroke(), R.drawable.dabble_sample_shortthicklightblue_stroke));
        samples.add(new StrokeSample(factory.createLimpStroke(), R.drawable.dabble_sample_limp_stroke));
        samples.add(new StrokeSample(factory.createStiffStroke(), R.drawable.dabble_sample_stiff_stroke));
        samples.add(new StrokeSample(factory.createStiffStroke(), R.drawable.dabble_sample_blocky_stroke));

        adapter = new StrokeSamplesAdapter(samples);

        list.setLayoutManager(new LinearLayoutManager(container.getContext()));
        list.setAdapter(adapter);
        list.setHasFixedSize(true);

        return view;
    }

    @OnClick(R.id.dabble_go)
    public void onDabble() {
        new PaintTask().execute();
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

    private class PaintTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            goProgressView.setVisibility(View.VISIBLE);
            goView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            List<Stroke> strokes = new ArrayList<>();
            for (StrokeSample sample: adapter.getSamples()) {
                for (int count = sample.getCount(); count > 0; count--) {
                    strokes.add(sample.getStroke());
                }
            }

            File destDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            File out = new File(destDir, "dabble_" + System.currentTimeMillis() + ".png");

            painter.paint(strokes, out);

            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{out.toString()},
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            if (uri != null) {
                                Log.d(LOG_TAG, "View " + uri);

                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        }
                    }
            );

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            goView.setVisibility(View.VISIBLE);
            goProgressView.setVisibility(View.INVISIBLE);
        }
    }

}
