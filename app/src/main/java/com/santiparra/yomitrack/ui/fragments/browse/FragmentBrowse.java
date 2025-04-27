package com.santiparra.yomitrack.ui.fragments.browse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.santiparra.yomitrack.R;

public class FragmentBrowse extends Fragment {
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle saveInstanceState){
        return layoutInflater.inflate(R.layout.fragment_browse,container,false);
    }
}
