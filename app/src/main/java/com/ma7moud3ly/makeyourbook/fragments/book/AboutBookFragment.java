package com.ma7moud3ly.makeyourbook.fragments.book;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.activities.BaseActivity;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.databinding.FragmentAboutBookBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.models.BookViewModel;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;
import com.ma7moud3ly.makeyourbook.util.CheckInternet;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

public class AboutBookFragment extends BaseFragment {
    private FragmentAboutBookBinding binding;
    private BookViewModel model;
    private HashMap<Long, String> currentDownloads = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentAboutBookBinding.inflate(inflater, container, false);
        binding.setUi(uiState);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(getActivity(), viewModelFactory).get(BookViewModel.class);

        model.book.observe(this, book -> {
            if (book != null) initBook(book);
            networkState(CONSTANTS.LOADED);
        });

        model.isFaved.observe(this, exists -> {
            if (exists) binding.bookToggleFav.setImageResource(R.drawable.fav_checked);
            else binding.bookToggleFav.setImageResource(R.drawable.fav);
        });

        binding.bookToggleFav.setOnClickListener((v) -> {
            if (model.book.getValue() != null) {
                if (model.isFaved.getValue() == true) {
                    model.delFav(model.book.getValue());
                } else {
                    model.addFav(model.book.getValue());
                    Toast.makeText(getContext(), getResources().getString(R.string.book_fav_added), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.download.setOnClickListener(v -> downloadBook());

    }

    private void initBook(Book book) {
        model.isExist(book);
        binding.setBook(book);
        if (!book.is_text) {
            binding.download.setText("تحميل : " + book.size + " - " + book.format);
            requireActivity().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (currentDownloads.containsKey(id)) {
                currentDownloads.remove(id);
                App.toast(getString(R.string.download_done));
            }
        }
    };

    private void downloadBook() {
        if (!((BaseActivity) getActivity()).checkStoragePermission()) return;
        if (!CheckInternet.isConnected(getContext())) {
            App.toast(getContext().getString(R.string.no_internet));
            return;
        }
        Book book = model.book.getValue();
        if (currentDownloads.containsValue(book.id)) {
            App.toast(getContext().getString(R.string.download_running));
            return;
        }
        String url = CONSTANTS.DOWNLOAD_BASE + book.download_link;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription(book.name + " : " + book.author);
        request.setTitle(book.name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, book.name + "." + book.format);
        request.addRequestHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:21.0) Gecko/20100101 Firefox/21.0");
        DownloadManager manager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Long download_id = manager.enqueue(request);
        currentDownloads.put(download_id, book.id);
        App.toast(getContext().getString(R.string.download_start));
    }

    @Override
    public void onResume() {
        super.onResume();
        uiState.showHeader.set(false);
        uiState.showFooter.set(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!model.book.getValue().is_text)
            getActivity().unregisterReceiver(onDownloadComplete);
    }
}