package com.santiparra.yomitrack.ui.fragments.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.ItemModel;
import com.santiparra.yomitrack.model.RecentActivityModel;
import com.santiparra.yomitrack.model.adapters.recentactivity_adapter.RecentActivityAdapter;
import com.santiparra.yomitrack.model.adapters.sectionadapter.SectionAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentHome extends Fragment {

    private final List<RecentActivityModel> fullRecentActivity = new ArrayList<>();
    private final List<RecentActivityModel> visibleRecentActivity = new ArrayList<>();
    private RecentActivityAdapter activityAdapter;

    public FragmentHome() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Secciones principales
        RecyclerView mainRecyclerView = view.findViewById(R.id.mainRecyclerView);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<String> sectionTitles = Arrays.asList("Airing", "Anime in Progress", "Manga in Progress");
        Map<String, List<ItemModel>> sectionItems = new HashMap<>();

        sectionItems.put("Airing", Arrays.asList(
                new ItemModel("Naruto", "5/220", "https://i.imgur.com/qzWZbL2.jpg", ItemModel.ContentType.ANIME),
                new ItemModel("Bleach", "100/366", "https://i.imgur.com/I0d1HyA.jpg", ItemModel.ContentType.ANIME),
                new ItemModel("One Piece", "900/1100", "https://i.imgur.com/VgVfG6K.jpg", ItemModel.ContentType.ANIME),
                new ItemModel("Boruto", "10/100", "https://i.imgur.com/lWhD6Zc.jpg", ItemModel.ContentType.ANIME),
                new ItemModel("Dragon Ball", "80/150", "https://i.imgur.com/z4d4kWk.jpg", ItemModel.ContentType.ANIME),
                new ItemModel("Another", "2/12", "https://i.imgur.com/z4d4kWk.jpg", ItemModel.ContentType.ANIME)
        ));

        sectionItems.put("Anime in Progress", Arrays.asList(
                new ItemModel("Attack on Titan", "16/25", "https://i.imgur.com/z4d4kWk.jpg", ItemModel.ContentType.ANIME),
                new ItemModel("Jujutsu Kaisen", "10/24", "https://i.imgur.com/lWhD6Zc.jpg", ItemModel.ContentType.ANIME),
                new ItemModel("One Piece", "900/1100", "https://i.imgur.com/VgVfG6K.jpg", ItemModel.ContentType.ANIME),
                new ItemModel("Boruto", "10/100", "https://i.imgur.com/lWhD6Zc.jpg", ItemModel.ContentType.ANIME),
                new ItemModel("Dragon Ball", "80/150", "https://i.imgur.com/z4d4kWk.jpg", ItemModel.ContentType.ANIME),
                new ItemModel("Another", "2/12", "https://i.imgur.com/z4d4kWk.jpg", ItemModel.ContentType.ANIME)
        ));

        sectionItems.put("Manga in Progress", Arrays.asList(
                new ItemModel("Chainsaw Man", "45/100", "https://i.imgur.com/7tZ0h8R.jpg", ItemModel.ContentType.MANGA),
                new ItemModel("Berserk", "370/380", "https://i.imgur.com/8FJYYHo.jpg", ItemModel.ContentType.MANGA),
                new ItemModel("One Piece", "900/1100", "https://i.imgur.com/VgVfG6K.jpg", ItemModel.ContentType.ANIME),
                new ItemModel("Boruto", "10/100", "https://i.imgur.com/lWhD6Zc.jpg", ItemModel.ContentType.ANIME),
                new ItemModel("Dragon Ball", "80/150", "https://i.imgur.com/z4d4kWk.jpg", ItemModel.ContentType.ANIME),
                new ItemModel("Another", "2/12", "https://i.imgur.com/z4d4kWk.jpg", ItemModel.ContentType.ANIME)
        ));

        SectionAdapter sectionAdapter = new SectionAdapter(sectionTitles, sectionItems);
        mainRecyclerView.setAdapter(sectionAdapter);

        // Actividades recientes
        RecyclerView activityRecyclerView = view.findViewById(R.id.activityRecyclerView);
        activityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fullRecentActivity.addAll(Arrays.asList(
                new RecentActivityModel("Midca", "Read chapters 1 - 60 of", "Choujun! Choujou Senpai", "4 minutes ago", "https://i.imgur.com/7tZ0h8R.jpg"),
                new RecentActivityModel("prtrncyon", "Scored 9/10 on", "Chainsaw Man", "12 hours ago", "https://cdn.myanimelist.net/images/manga/2/253146.jpg"),
                new RecentActivityModel("BtwIsSanti", "Watched episode 7 of", "Fate/Zero", "1 day ago", "https://cdn.myanimelist.net/images/anime/5/73245.jpg"),
                new RecentActivityModel("Taku", "Watched episode 3 of", "Mob Psycho 100", "2 days ago", "https://i.imgur.com/I0d1HyA.jpg"),
                new RecentActivityModel("Maki", "Read chapter 14 of", "Blue Period", "4 days ago", "https://i.imgur.com/z4d4kWk.jpg"),
                new RecentActivityModel("Yato", "Scored 10/10 on", "Noragami", "5 days ago", "https://i.imgur.com/8FJYYHo.jpg"),
                new RecentActivityModel("Eri", "Completed", "My Dress-Up Darling", "6 days ago", "https://i.imgur.com/qzWZbL2.jpg"),
                new RecentActivityModel("Yuji", "Watched episode 6 of", "Jujutsu Kaisen", "7 days ago", "https://i.imgur.com/lWhD6Zc.jpg"),
                new RecentActivityModel("Light", "Scored 10/10 on", "Death Note", "8 days ago", "https://i.imgur.com/I0d1HyA.jpg"),
                new RecentActivityModel("Luffy", "Watched episode 1000 of", "One Piece", "9 days ago", "https://i.imgur.com/VgVfG6K.jpg"),
                new RecentActivityModel("Gon", "Started watching", "Hunter x Hunter", "10 days ago", "https://i.imgur.com/z4d4kWk.jpg")
        ));

        visibleRecentActivity.addAll(fullRecentActivity.subList(0, Math.min(10, fullRecentActivity.size())));
        activityAdapter = new RecentActivityAdapter(visibleRecentActivity);
        activityRecyclerView.setAdapter(activityAdapter);

        Button buttonShowMore = view.findViewById(R.id.buttonShowMoreActivity);
        Button buttonShowLess = view.findViewById(R.id.buttonShowLessActivity);

        if (fullRecentActivity.size() > 10) {
            buttonShowMore.setVisibility(View.VISIBLE);
            buttonShowLess.setVisibility(View.GONE);

            buttonShowMore.setOnClickListener(v -> {
                visibleRecentActivity.clear();
                visibleRecentActivity.addAll(fullRecentActivity);
                activityAdapter.notifyDataSetChanged();
                buttonShowMore.setVisibility(View.GONE);
                buttonShowLess.setVisibility(View.VISIBLE);
            });

            buttonShowLess.setOnClickListener(v -> {
                visibleRecentActivity.clear();
                visibleRecentActivity.addAll(fullRecentActivity.subList(0, 10));
                activityAdapter.notifyDataSetChanged();
                buttonShowMore.setVisibility(View.VISIBLE);
                buttonShowLess.setVisibility(View.GONE);
            });
        } else {
            buttonShowMore.setVisibility(View.GONE);
            buttonShowLess.setVisibility(View.GONE);
        }
    }
}
