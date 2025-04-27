package com.santiparra.yomitrack.ui.fragments.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.adapters.homeadapter.HomeAdapter;
import com.santiparra.yomitrack.model.adapters.sectionadapter.SectionAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentHome extends Fragment {

    private RecyclerView horizontalRecyclerView;

    public FragmentHome() {
        super(R.layout.fragment_home); // Estás usando ViewBinding/Inflado automático por constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView mainRecyclerView = view.findViewById(R.id.mainRecyclerView);

        List<String> sectionTitles = Arrays.asList(
                "Airing", "Anime in Progress", "Manga in Progress"
        );

        Map<String, List<Integer>> sectionImages = new HashMap<>();
        sectionImages.put("Airing", Arrays.asList(R.drawable.imagen1, R.drawable.imagen2, R.drawable.imagen3));
        sectionImages.put("Anime in Progress", Arrays.asList(R.drawable.imagen4, R.drawable.imagen5));
        sectionImages.put("Manga in Progress", Arrays.asList(R.drawable.imagen6, R.drawable.imagen7, R.drawable.imagen8));

        SectionAdapter adapter = new SectionAdapter(sectionTitles, sectionImages);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainRecyclerView.setAdapter(adapter);
    }

}
