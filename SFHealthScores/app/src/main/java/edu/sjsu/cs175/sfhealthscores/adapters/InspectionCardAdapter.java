package edu.sjsu.cs175.sfhealthscores.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.sjsu.cs175.sfhealthscores.R;
import edu.sjsu.cs175.sfhealthscores.helpers.Globals;
import edu.sjsu.cs175.sfhealthscores.models.InspectionHistory;

/**
 * Inspection history card view adapter.
 */
public class InspectionCardAdapter extends RecyclerView.Adapter<InspectionCardAdapter.ViewHolder> {

    private List<InspectionHistory> dataSet;
    private int layoutId = R.layout.card_inspection;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public TextView scoreTextView;
        public TextView dateTextView;
        public TextView typeTextView;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_inspection_view);
            scoreTextView = (TextView) v.findViewById(R.id.card_inspection_score);
            dateTextView = (TextView) v.findViewById(R.id.card_inspection_date);
            typeTextView = (TextView) v.findViewById(R.id.card_inspection_type);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public InspectionCardAdapter(List<InspectionHistory> myDataset) {
        dataSet = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public InspectionCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        InspectionHistory item = dataSet.get(position);
        String score = item.inspectionScore;
        String date = item.inspectionDate;
        String type = item.inspectionType;
        // set card color and score
        if (score != null && !score.equals("")) {
            int scoreColor = Globals.getScoreColor(Integer.parseInt(score));
            holder.cardView.setCardBackgroundColor(scoreColor);
            holder.scoreTextView.setText(score);
        }
        // set and format date
        String formatDate = Globals.timestampToDate(date);
        holder.dateTextView.setText(formatDate);
        // set type
        holder.typeTextView.setText(type);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}
