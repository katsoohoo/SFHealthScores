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
import edu.sjsu.cs175.sfhealthscores.models.BusinessListing;

/**
 * Business card view adapter.
 */
public class BusinessCardAdapter extends RecyclerView.Adapter<BusinessCardAdapter.ViewHolder> {

    private List<BusinessListing> dataSet;
    private int layoutId = R.layout.card_business;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public TextView nameTextView;
        public TextView addressTextView;
        public TextView scoreTextView;
        public TextView extraTextView;


        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_business_view);
            nameTextView = (TextView) v.findViewById(R.id.card_business_name);
            addressTextView = (TextView) v.findViewById(R.id.card_business_address);
            scoreTextView = (TextView) v.findViewById(R.id.card_business_score);
            extraTextView = (TextView) v.findViewById(R.id.card_business_extra);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BusinessCardAdapter(List<BusinessListing> myDataset) {
        dataSet = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BusinessCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
        BusinessListing item = dataSet.get(position);
        String name = item.businessName;
        String address = item.businessAddress;
        String score = item.inspectionScore;
        String distance = item.distance;
        String date = item.inspectionDate;
        // set name and address
        holder.nameTextView.setText(name);
        holder.addressTextView.setText(address);
        // set card color and score
        if (score != null && !score.equals("")) {
            int scoreColor = Globals.getScoreColor(Integer.parseInt(score));
            holder.cardView.setCardBackgroundColor(scoreColor);
            holder.scoreTextView.setText(score);
        }
        // set extra content: distance, date
        if (distance != null) {
            double milesDist = Globals.metersToMiles(Double.parseDouble(distance));
            holder.extraTextView.setText(milesDist + " mi");
        } else if (date != null) {
            String formatDate = Globals.timestampToDate(date);
            holder.extraTextView.setText(formatDate);
        } else {
            holder.extraTextView.setVisibility(View.GONE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}
