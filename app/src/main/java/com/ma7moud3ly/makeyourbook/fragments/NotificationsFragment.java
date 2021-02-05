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
import android.widget.CheckBox;

import com.ma7moud3ly.makeyourbook.activities.BaseActivity;
import com.ma7moud3ly.makeyourbook.databinding.FragmentNotificationsBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotificationsFragment extends BaseFragment {
    private FragmentNotificationsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.notifyBooks.setChecked(pref.get("notify-books", true));
        binding.notifyAuthors.setChecked(pref.get("notify-authors", true));
        binding.notifyQuotes.setChecked(pref.get("notify-quotes", true));

        binding.notifyBooks.setOnClickListener(v -> subNotifications((CheckBox) v));
        binding.notifyAuthors.setOnClickListener(v -> subNotifications((CheckBox) v));
        binding.notifyQuotes.setOnClickListener(v -> subNotifications((CheckBox) v));
    }

    public void subNotifications(CheckBox v) {
        String topic = v.getTag().toString();
        boolean is_checked = v.isChecked();
        if (is_checked) ((BaseActivity) getActivity()).subscribeToTopics(topic);
        else ((BaseActivity) getActivity()).unsubscribeToTopics(topic);
    }

    @Override
    public void onResume() {
        super.onResume();
        showFooter(false);
        showSearch(true);
    }


}