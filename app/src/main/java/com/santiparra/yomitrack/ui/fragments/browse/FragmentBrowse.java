package com.santiparra.yomitrack.ui.fragments.browse;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.ItemModel;
import com.santiparra.yomitrack.model.adapters.browser_section_adapter.BrowseGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentBrowse extends Fragment {

    private Spinner spinnerType;
    private EditText editTextSearch;
    private LinearLayout sectionContainer;

    private final List<ItemModel> animeList = new ArrayList<>();
    private final List<ItemModel> mangaList = new ArrayList<>();

    public FragmentBrowse() {
        super(R.layout.fragment_browse);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spinnerType = view.findViewById(R.id.spinnerType);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        sectionContainer = view.findViewById(R.id.sectionContainer);

        View statusBarSpacer = view.findViewById(R.id.statusBarSpacer);
        int statusBarHeight = getStatusBarHeight();
        ViewGroup.LayoutParams params = statusBarSpacer.getLayoutParams();
        params.height = statusBarHeight;
        statusBarSpacer.setLayoutParams(params);

        Spinner spinnerType = view.findViewById(R.id.spinnerType);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.media_types, R.layout.item_spinner);
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner);
        spinnerType.setAdapter(spinnerAdapter);


        loadSampleData();
        showSections(animeList);

        spinnerType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selected = spinnerType.getSelectedItem().toString();
                if (selected.equals("Anime")) {
                    showSections(animeList);
                } else {
                    showSections(mangaList);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String selected = spinnerType.getSelectedItem().toString();
                if (selected.equals("Anime")) {
                    showSections(animeList, s.toString());
                } else {
                    showSections(mangaList, s.toString());
                }
            }
        });
    }

    private void showSections(List<ItemModel> source) {
        showSections(source, "");
    }

    private void showSections(List<ItemModel> source, String query) {
        sectionContainer.removeAllViews();

        List<ItemModel> trending = new ArrayList<>();
        List<ItemModel> popular = new ArrayList<>();

        for (int i = 0; i < source.size(); i++) {
            ItemModel item = source.get(i);
            if (!query.isEmpty() && !item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                continue;
            }
            if (i % 2 == 0) trending.add(item);
            else popular.add(item);
        }

        if (!trending.isEmpty()) {
            addSection("Trending Now", trending);
        }

        if (!popular.isEmpty()) {
            addSection("Popular This Season", popular);
        }
    }

    private void addSection(String title, List<ItemModel> items) {
        Context context = requireContext();

        LinearLayout sectionLayout = new LinearLayout(context);
        sectionLayout.setOrientation(LinearLayout.VERTICAL);
        sectionLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sectionLayout.setPadding(0, 0, 0, 24);

        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextSize(18);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(ContextCompat.getColor(context, R.color.textPrimary));
        titleView.setPadding(0, 0, 0, 8);
        sectionLayout.addView(titleView);

        RecyclerView recyclerView = new RecyclerView(context);

        // 🔥 Establecer altura fija para 2 filas (170dp + 12sp de texto aprox + márgenes)
        int itemHeightPx = (int) (170 * context.getResources().getDisplayMetrics().density);
        int textHeightPx = (int) (40 * context.getResources().getDisplayMetrics().density);
        int totalHeight = (itemHeightPx + textHeightPx + 45) * 2;


        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, totalHeight)); // <- altura calculada

        GridLayoutManager layoutManager = new GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new BrowseGridAdapter(items));
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setClipToPadding(false);

        sectionLayout.addView(recyclerView);
        sectionContainer.addView(sectionLayout);
    }

    private void loadSampleData() {
        animeList.clear();
        mangaList.clear();

        for (int i = 1; i <= 20; i++) {
            animeList.add(new ItemModel("Anime " + i, i + "/24", "https://example.com/anime" + i + ".jpg", ItemModel.ContentType.ANIME));
            mangaList.add(new ItemModel("Manga " + i, i + "/120", "https://example.com/manga" + i + ".jpg", ItemModel.ContentType.MANGA));
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
