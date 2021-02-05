package com.ma7moud3ly.makeyourbook.adapters;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.data.Collection;
import com.ma7moud3ly.makeyourbook.databinding.ItemCollectionBinding;
import com.ma7moud3ly.makeyourbook.observables.UiState;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CollectionsRecyclerAdapter extends RecyclerView.Adapter<CollectionsRecyclerAdapter.MyViewHolder> {
    final private List<Collection> list;

    public CollectionsRecyclerAdapter(List<Collection> list) {
        this.list = list;
    }

    public class MyViewHolder extends BaseViewHolder {
        private final ItemCollectionBinding binding;

        public MyViewHolder(ItemCollectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public CollectionsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCollectionBinding binding = ItemCollectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull CollectionsRecyclerAdapter.MyViewHolder holder, int position) {
        if (list != null && position < list.size()) {
            Collection collection = list.get(position);
            if (collection.id.equals(""))
                holder.binding.more.setOnClickListener(v -> holder.uiState.library(holder.binding.more));
            else
                holder.binding.more.setOnClickListener(v -> holder.uiState.openAuthor(holder.binding.more, collection.id));
            holder.binding.label.setText(collection.title);
            initRecycler(holder.binding.recycler,collection.books);
        }
    }

    @NonNull
    @Override
    public int getItemCount() {
        return list.size();
    }

    private void initRecycler(RecyclerView recyclerView,List<Book>books){
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        BookRecyclerAdapter adapter = new BookRecyclerAdapter(books,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

}
