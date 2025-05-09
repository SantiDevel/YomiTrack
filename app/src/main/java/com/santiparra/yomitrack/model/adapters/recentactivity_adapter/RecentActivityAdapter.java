package com.santiparra.yomitrack.model.adapters.recentactivity_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.RecentActivityModel;

import java.util.List;

public class RecentActivityAdapter extends RecyclerView.Adapter<RecentActivityAdapter.ActivityViewHolder> {
    private final List<RecentActivityModel> activityList;

    public RecentActivityAdapter(List<RecentActivityModel> activityList) {
        this.activityList = activityList;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_card, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        RecentActivityModel activity = activityList.get(position);
        holder.user.setText(activity.user);
        holder.action.setText(activity.action);
        holder.title.setText(activity.title);
        holder.time.setText(activity.time);

        Glide.with(holder.image.getContext())
                .load(activity.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView user, action, title, time;
        ImageView image;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.activityUser);
            action = itemView.findViewById(R.id.activityAction);
            title = itemView.findViewById(R.id.activityTitle);
            time = itemView.findViewById(R.id.activityTime);
            image = itemView.findViewById(R.id.activityCover);
        }
    }
}
