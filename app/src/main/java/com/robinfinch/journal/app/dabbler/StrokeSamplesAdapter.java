package com.robinfinch.journal.app.dabbler;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.robinfinch.journal.app.R;
import com.robinfinch.journal.app.util.Parser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Adapter for a list of {@link com.robinfinch.journal.app.dabbler.StrokeSample samples}.
 *
 * @author Mark Hoogenboom
 */
public class StrokeSamplesAdapter extends RecyclerView.Adapter<StrokeSamplesAdapter.ViewHolder> {
    private final List<StrokeSample> samples;

    public StrokeSamplesAdapter(List<StrokeSample> strokes) {
        this.samples = strokes;
    }

    @Override
    public int getItemCount() {
        return samples.size();
    }

    public List<StrokeSample> getSamples() {
        return new ArrayList<>(samples);
    }

    @Override
    public StrokeSamplesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stroke_sample_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final StrokeSample sample = samples.get(position);

        holder.countView.setText(Integer.toString(sample.getCount()));
        holder.countView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        int count = Parser.parseNumber(holder.countView.getText());
                        sample.setCount(count);
                    }
                    catch (NumberFormatException e) {
                    }
                }
            }
        });

        holder.sampleView.setImageDrawable(holder.res.getDrawable(sample.getSampleResId()));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        Resources res;

        @InjectView(R.id.dabble_count)
        EditText countView;

        @InjectView(R.id.dabble_sample)
        ImageView sampleView;

        public ViewHolder(View view) {
            super(view);
            res = view.getContext().getResources();
            ButterKnife.inject(this, view);
        }
    }
}