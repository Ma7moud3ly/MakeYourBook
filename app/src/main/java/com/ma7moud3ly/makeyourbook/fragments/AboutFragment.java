package com.ma7moud3ly.makeyourbook.fragments;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ma7moud3ly.makeyourbook.databinding.FragmentAboutBinding;
import com.ma7moud3ly.makeyourbook.models.AboutViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AboutFragment extends BaseFragment {
    private FragmentAboutBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AboutViewModel about = new AboutViewModel();
        about.appVersion(getActivity());
        binding.setAbout(about);
    }

    @Override
    public void onResume() {
        super.onResume();
        uiState.showFooter.set(false);
        uiState.showHeader.set(false);
    }

}