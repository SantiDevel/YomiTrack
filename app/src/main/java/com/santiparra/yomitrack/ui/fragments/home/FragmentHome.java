package com.santiparra.yomitrack.ui.fragments.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        RecyclerView mainRecyclerView = view.findViewById(R.id.mainRecyclerView);

        // Crear las secciones
        List<String> sectionTitles = Arrays.asList(
                "Airing", "Anime in Progress", "Manga in Progress"
        );

        // Crear el contenido de cada sección como List<ItemModel>
        Map<String, List<ItemModel>> sectionItems = new HashMap<>();

        List<ItemModel> airingItems = new ArrayList<>();
        airingItems.add(new ItemModel("Naruto", "5/220", "https://i.imgur.com/N5uCbDu.jpg", ItemModel.ContentType.ANIME));
        airingItems.add(new ItemModel("One Piece", "900/1100", "https://i.imgur.com/VgVfG6K.jpg", ItemModel.ContentType.ANIME));
        airingItems.add(new ItemModel("Bleach", "100/366", "https://i.imgur.com/I0d1HyA.jpg", ItemModel.ContentType.ANIME));

        List<ItemModel> animeInProgressItems = new ArrayList<>();
        animeInProgressItems.add(new ItemModel("Attack on Titan", "16/25", "https://i.imgur.com/z4d4kWk.jpg", ItemModel.ContentType.ANIME));
        animeInProgressItems.add(new ItemModel("Jujutsu Kaisen", "10/24", "https://i.imgur.com/lWhD6Zc.jpg", ItemModel.ContentType.ANIME));

        List<ItemModel> mangaInProgressItems = new ArrayList<>();
        mangaInProgressItems.add(new ItemModel("Chainsaw Man", "45/100", "https://i.imgur.com/7tZ0h8R.jpg", ItemModel.ContentType.MANGA));
        mangaInProgressItems.add(new ItemModel("Berserk", "370/380", "https://i.imgur.com/8FJYYHo.jpg", ItemModel.ContentType.MANGA));

        // Asignar los items a las secciones
        sectionItems.put("Airing", airingItems);
        sectionItems.put("Anime in Progress", animeInProgressItems);
        sectionItems.put("Manga in Progress", mangaInProgressItems);

        // Configurar RecyclerView
        SectionAdapter adapter = new SectionAdapter(sectionTitles, sectionItems);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainRecyclerView.setAdapter(adapter);
    }
}
