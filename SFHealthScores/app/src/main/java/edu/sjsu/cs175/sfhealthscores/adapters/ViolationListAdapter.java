package edu.sjsu.cs175.sfhealthscores.adapters;

import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.sjsu.cs175.sfhealthscores.R;
import edu.sjsu.cs175.sfhealthscores.helpers.Globals;
import edu.sjsu.cs175.sfhealthscores.models.Violation;

/**
 * Created by kat on 12/12/16.
 */

public class ViolationListAdapter extends RecyclerView.Adapter<ViolationListAdapter.ViewHolder> {
    private List<Violation> dataSet;
    private int layoutId = R.layout.list_violation;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView iconImageView;
        public TextView descTextView;

        public ViewHolder(View v) {
            super(v);
            iconImageView = (ImageView) v.findViewById(R.id.violation_icon);
            descTextView = (TextView) v.findViewById(R.id.violation_description);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ViolationListAdapter(List<Violation> myDataset) {
        dataSet = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViolationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViolationListAdapter.ViewHolder vh = new ViolationListAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViolationListAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Violation item = dataSet.get(position);
        String category = item.riskCategory;
        String description = item.violationDescription;
        // set description
        holder.descTextView.setText(description);
        // set icon color
        int catColor = Globals.getViolationColor(category);
//        holder.iconImageView.setColorFilter(catColor, PorterDuff.Mode.MULTIPLY);
        holder.iconImageView.getDrawable().setColorFilter(catColor, PorterDuff.Mode.MULTIPLY);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}