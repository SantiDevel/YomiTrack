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

/**
 * Adaptador para mostrar la actividad reciente del usuario en forma de tarjetas.
 * Cada tarjeta puede incluir una acción, portada, comentarios, botón de like y comentar.
 */
public class RecentActivityAdapter extends RecyclerView.Adapter<RecentActivityAdapter.ActivityViewHolder> {

    /** Lista de actividades recientes. */
    private List<RecentActivityModel> activityList;

    /** ID del usuario actualmente logueado (para comentarios y likes). */
    private final int currentUserId;

    /**
     * Constructor del adaptador.
     *
     * @param activityList lista de actividades a mostrar.
     * @param currentUserId ID del usuario actual.
     */
    public RecentActivityAdapter(List<RecentActivityModel> activityList, int currentUserId) {
        this.activityList = activityList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity_card, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        RecentActivityModel activity = activityList.get(position);

        holder.user.setText(activity.getUser());
        holder.action.setText(activity.action);
        holder.title.setText(activity.title);
        holder.time.setText(activity.time);

        Glide.with(holder.image.getContext())
                .load(activity.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.image);

        // Limpiar contenedor de comentarios antes de agregar los nuevos
        holder.commentContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());

        // Mostrar comentarios
        for (CommentModel comment : activity.comments) {
            View commentView = inflater.inflate(R.layout.item_comment, holder.commentContainer, false);

            TextView usernameView = commentView.findViewById(R.id.commentUsername);
            TextView commentText = commentView.findViewById(R.id.commentText);
            TextView dateView = commentView.findViewById(R.id.commentDate);
            ImageView avatar = commentView.findViewById(R.id.commentAvatar);
            ImageButton likeButton = commentView.findViewById(R.id.commentLikeButton);
            ImageButton replyButton = commentView.findViewById(R.id.replyButton);

            usernameView.setText(comment.getUsername());
            commentText.setText(comment.getText());
            dateView.setText(comment.getCreatedAt());

            Glide.with(commentView.getContext())
                    .load(comment.getAvatarUrl())
                    .placeholder(R.drawable.rectangle_placeholder)
                    .error(R.drawable.error_image)
                    .into(avatar);

            likeButton.setImageResource(comment.isLiked() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
            likeButton.setColorFilter(commentView.getContext().getColor(comment.isLiked() ? R.color.pink : R.color.gray));

            // Manejo de likes en comentarios
            likeButton.setOnClickListener(v -> {
                boolean newLike = !comment.isLiked();
                comment.setLiked(newLike);
                likeButton.setImageResource(newLike ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
                likeButton.setColorFilter(commentView.getContext().getColor(newLike ? R.color.pink : R.color.gray));
            });

            // Manejo de respuesta a un comentario
            replyButton.setOnClickListener(v -> {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos == RecyclerView.NO_POSITION) return;
                RecentActivityModel activityItem = activityList.get(adapterPos);
                CommentDialog dialog = new CommentDialog(
                        holder.itemView.getContext(),
                        currentUserId,
                        activityItem.getId(),
                        () -> notifyItemChanged(adapterPos),
                        comment.getUsername()
                );
                dialog.show();
            });

            holder.commentContainer.addView(commentView);
        }

        // Botón para añadir nuevo comentario a la actividad
        holder.commentButton.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos == RecyclerView.NO_POSITION) return;
            RecentActivityModel activityItem = activityList.get(adapterPos);
            CommentDialog dialog = new CommentDialog(
                    holder.itemView.getContext(),
                    currentUserId,
                    activityItem.getId(),
                    () -> notifyItemChanged(adapterPos)
            );
            dialog.show();
        });

        // Botón de like para la actividad
        holder.likeButton.setOnClickListener(v -> {
            activity.liked = !activity.liked;
            holder.likeButton.setImageResource(activity.liked ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
            holder.likeButton.setColorFilter(holder.itemView.getContext().getColor(
                    activity.liked ? R.color.pink : R.color.textPrimary));
        });
    }

    /**
     * Devuelve la cantidad de actividades en la lista.
     *
     * @return tamaño de la lista.
     */
    @Override
    public int getItemCount() {
        return activityList.size();
    }

    /**
     * Reemplaza la lista actual por una nueva y actualiza la vista.
     *
     * @param newList nueva lista de actividades.
     */
    public void updateData(List<RecentActivityModel> newList) {
        this.activityList = newList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder que representa una tarjeta de actividad reciente.
     */
    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView user, action, title, time;
        ImageView image;
        ImageButton likeButton, commentButton;
        LinearLayout commentContainer;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView vista inflada del ítem.
         */
        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.activityUser);
            action = itemView.findViewById(R.id.activityAction);
            title = itemView.findViewById(R.id.activityTitle);
            time = itemView.findViewById(R.id.activityTime);
            image = itemView.findViewById(R.id.activityCover);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            commentContainer = itemView.findViewById(R.id.commentsContainer);
        }
    }
}
