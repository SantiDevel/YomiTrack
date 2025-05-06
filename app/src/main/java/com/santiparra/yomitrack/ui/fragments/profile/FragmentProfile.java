package com.santiparra.yomitrack.ui.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.UserStats;
import com.santiparra.yomitrack.utils.StatsHelper;

import java.util.List;

public class FragmentProfile extends Fragment {

    public FragmentProfile() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView usernameText = view.findViewById(R.id.usernameText);
        TextView descriptionText = view.findViewById(R.id.descriptionText);
        LinearLayout statsContainer = view.findViewById(R.id.animeStatsContainer);

        usernameText.setText("BtwIsSanti");
        descriptionText.setText("Eiko is my waifu right now\nMai and Mikasa is my second wife");

        // Cargar estadísticas simuladas
        List<UserStats> statsList = StatsHelper.getAnimeStats();

        for (UserStats stat : statsList) {
            View statView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_stat_bar, statsContainer, false);

            TextView label = statView.findViewById(R.id.statLabelFull);
            ProgressBar bar = statView.findViewById(R.id.statProgressBar);

            label.setText(stat.getCategory() + " • " + stat.getCount());
            bar.setProgress(stat.getPercentage());

            // Aplicar color distinto por categoría
            int colorRes = R.color.primary; // default
            switch (stat.getCategory()) {
                case "Watching":
                    colorRes = R.color.statWatching;
                    break;
                case "Completed":
                    colorRes = R.color.statCompleted;
                    break;
                case "On Hold":
                    colorRes = R.color.statOnHold;
                    break;
                case "Dropped":
                    colorRes = R.color.statDropped;
                    break;
                case "Plan to Watch":
                    colorRes = R.color.statPlanToWatch;
                    break;
            }

            bar.setProgressTintList(ContextCompat.getColorStateList(requireContext(), colorRes));
            statsContainer.addView(statView);
        }

        // Agregar tarjetas de actividad (opcional)
        LinearLayout activityContainer = view.findViewById(R.id.activityContainer);

        if (activityContainer != null) {
            // Lista simulada de updates
            String[] activities = {
                    "Watched episode 5 of Kakushite! Makina-san!!",
                    "Watched episode 5 of Chotto dake Ai ga Omo...",
                    "Watched episode 4 of Go-Toubun no Hanayome",
                    "Watched episode 3 of Kanojo, Okarishimasu",
                    "Watched episode 12 of Jujutsu Kaisen",
                    "Watched episode 8 of Bleach: TYBW",
                    "Watched episode 1 of Chainsaw Man",
                    "Watched episode 10 of Dr. Stone",
                    "Watched episode 9 of Mushoku Tensei",
                    "Watched episode 11 of Ousama Ranking",
                    "Watched episode 7 of HUNTER×HUNTER" // Se ignora si hay más de 10
            };

            int limit = Math.min(10, activities.length);

            for (int i = 0; i < limit; i++) {
                View card = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_activity, activityContainer, false);

                TextView text = card.findViewById(R.id.activityText);
                TextView time = card.findViewById(R.id.activityTime);

                text.setText(activities[i]);
                time.setText("16 hours ago");

                activityContainer.addView(card);
            }
        }

        // Estadísticas de manga
        LinearLayout mangaStatsContainer = view.findViewById(R.id.mangaStatsContainer);
        List<UserStats> mangaStats = StatsHelper.getMangaStats();

        for (UserStats stat : mangaStats) {
            View statView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_stat_bar, mangaStatsContainer, false);

            TextView label = statView.findViewById(R.id.statLabelFull);
            ProgressBar bar = statView.findViewById(R.id.statProgressBar);

            label.setText(stat.getCategory() + " • " + stat.getCount());
            bar.setProgress(stat.getPercentage());

            int colorRes = R.color.primary;
            switch (stat.getCategory()) {
                case "Reading": colorRes = R.color.statWatching; break;
                case "Completed": colorRes = R.color.statCompleted; break;
                case "On Hold": colorRes = R.color.statOnHold; break;
                case "Dropped": colorRes = R.color.statDropped; break;
                case "Plan to Read": colorRes = R.color.statPlanToWatch; break;
            }

            bar.setProgressTintList(ContextCompat.getColorStateList(requireContext(), colorRes));
            mangaStatsContainer.addView(statView);
        }

    }
}
