package com.ma7moud3ly.makeyourbook.util;

import com.ma7moud3ly.makeyourbook.data.MyPager;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerPagination {

    private boolean can_scroll = true;
    private int max_size, page_size, item_index;
    public MutableLiveData<MyPager> observer = new MutableLiveData<>();

    public RecyclerPagination(RecyclerView recycler, int maxSize, int pageSize) {
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                    if (can_scroll) {
                        can_scroll = false;
                        item_index += page_size;
                        changePage();
                    }
                }
            }
        });
        this.max_size = maxSize;
        this.page_size = pageSize;
        this.item_index = 0;
        can_scroll = true;
        changePage();
    }

    private void changePage() {
        if (item_index >= max_size) return;
        int end = (item_index + page_size > max_size) ? max_size : item_index + page_size;
        if (item_index < max_size)
            onPageChanged(item_index, end);
        can_scroll = true;
    }

    private void onPageChanged(int start, int end) {
        MyPager pager = new MyPager();
        pager.current_page = start;
        pager.last_page = end;
        observer.setValue(pager);
    }

}
