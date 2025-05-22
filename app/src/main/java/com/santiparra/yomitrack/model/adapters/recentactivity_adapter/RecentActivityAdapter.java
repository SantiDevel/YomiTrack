package com.santiparra.yomitrack.model.adapters.recentactivity_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.CommentDialog;
import com.santiparra.yomitrack.model.CommentModel;
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

        holder.commentContainer.removeAllViews();

        if (holder.commentContainer != null) {
            holder.commentContainer.removeAllViews();

            for (CommentModel comment : activity.comments) {
                View commentView = LayoutInflater.from(holder.itemView.getContext())
                        .inflate(R.layout.item_comment, holder.commentContainer, false);

                TextView commentText = commentView.findViewById(R.id.commentText);
                ImageButton commentLike = commentView.findViewById(R.id.commentLikeButton);

                commentText.setText(comment.text);
                commentLike.setImageResource(comment.liked ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
                commentLike.setColorFilter(comment.liked
                        ? holder.itemView.getContext().getColor(R.color.pink)
                        : holder.itemView.getContext().getColor(R.color.textPrimary));

                commentLike.setOnClickListener(v -> {
                    comment.liked = !comment.liked;
                    commentLike.setImageResource(comment.liked ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
                    commentLike.setColorFilter(comment.liked
                            ? holder.itemView.getContext().getColor(R.color.pink)
                            : holder.itemView.getContext().getColor(R.color.textPrimary));
                });

                holder.commentContainer.addView(commentView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView user, action, title, time;
        ImageView image;
        ImageButton likeButton, commentButton;
        LinearLayout commentContainer;
        boolean isLiked = false;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.activityUser);
            action = itemView.findViewById(R.id.activityAction);
            title = itemView.findViewById(R.id.activityTitle);
            time = itemView.findViewById(R.id.activityTime);
            image = itemView.findViewById(R.id.activityCover);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            commentContainer = itemView.findViewById(R.id.commentsContainer); // <- protección aplicada

            likeButton.setOnClickListener(v -> {
                isLiked = !isLiked;
                likeButton.setImageResource(isLiked ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
                likeButton.setColorFilter(isLiked
                        ? itemView.getContext().getColor(R.color.pink)
                        : itemView.getContext().getColor(R.color.textPrimary));
            });

            commentButton.setOnClickListener(v -> {
                RecentActivityModel activity = activityList.get(getAdapterPosition());
                int activityId = activity.getId();
                int userId = this.user.getId();

                CommentDialog dialog = new CommentDialog(itemView.getContext(), userId, activityId, () -> {});
                dialog.show();
            });
        }
    }
}
