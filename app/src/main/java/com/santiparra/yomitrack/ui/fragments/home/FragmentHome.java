package com.santiparra.yomitrack.ui.fragments.home;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.ItemModel;
import com.santiparra.yomitrack.model.adapters.sectionadapter.SectionAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentHome extends Fragment {

    public FragmentHome() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecyclerView principal
        RecyclerView mainRecyclerView = view.findViewById(R.id.mainRecyclerView);

        // Crear las secciones
        List<String> sectionTitles = Arrays.asList(
                "Airing", "Anime in Progress", "Manga in Progress"
        );

        Map<String, List<ItemModel>> sectionItems = new HashMap<>();

        List<ItemModel> airingItems = new ArrayList<>();
        airingItems.add(new ItemModel("Naruto", "5/220", "https://th.bing.com/th/id/OIP.aypxRH6Qq7yXLFCXiYhaKAHaLo?rs=1&pid=ImgDetMain", ItemModel.ContentType.ANIME));
        airingItems.add(new ItemModel("One Piece", "900/1100", "https://i.imgur.com/VgVfG6K.jpg", ItemModel.ContentType.ANIME));
        airingItems.add(new ItemModel("Bleach", "100/366", "https://i.imgur.com/I0d1HyA.jpg", ItemModel.ContentType.ANIME));

        List<ItemModel> animeInProgressItems = new ArrayList<>();
        animeInProgressItems.add(new ItemModel("Attack on Titan", "16/25", "https://i.imgur.com/z4d4kWk.jpg", ItemModel.ContentType.ANIME));
        animeInProgressItems.add(new ItemModel("Jujutsu Kaisen", "10/24", "https://i.imgur.com/lWhD6Zc.jpg", ItemModel.ContentType.ANIME));

        List<ItemModel> mangaInProgressItems = new ArrayList<>();
        mangaInProgressItems.add(new ItemModel("Chainsaw Man", "45/100", "https://i.imgur.com/7tZ0h8R.jpg", ItemModel.ContentType.MANGA));
        mangaInProgressItems.add(new ItemModel("Berserk", "370/380", "https://i.imgur.com/8FJYYHo.jpg", ItemModel.ContentType.MANGA));

        sectionItems.put("Airing", airingItems);
        sectionItems.put("Anime in Progress", animeInProgressItems);
        sectionItems.put("Manga in Progress", mangaInProgressItems);

        SectionAdapter adapter = new SectionAdapter(sectionTitles, sectionItems);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainRecyclerView.setAdapter(adapter);

        // Sección inferior: campo de estado + nueva card
        EditText editStatus = view.findViewById(R.id.editStatus);

        View activityCard = view.findViewById(R.id.activityCard);
        ImageView activityImage = activityCard.findViewById(R.id.activityImage);
        TextView activityUser = activityCard.findViewById(R.id.activityUser);
        TextView activityAction = activityCard.findViewById(R.id.activityAction);
        TextView activityTitle = activityCard.findViewById(R.id.activityTitle);
        TextView activityTime = activityCard.findViewById(R.id.activityTime);

        // Contenido inicial
        activityUser.setText("Midca");
        activityAction.setText("Read chapters 1 - 60 of");
        activityTitle.setText("Choujun! Choujou Senpai");
        activityTime.setText("4 minutes ago");

        Glide.with(requireContext())
                .load("https://i.imgur.com/7tZ0h8R.jpg")
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(activityImage);

        // Actualización con el texto del usuario
        editStatus.setOnEditorActionListener((v, actionId, event) -> {
            String status = editStatus.getText().toString().trim();
            if (!status.isEmpty()) {
                activityAction.setText(status);
            }
            return true;
        });
    }
}
