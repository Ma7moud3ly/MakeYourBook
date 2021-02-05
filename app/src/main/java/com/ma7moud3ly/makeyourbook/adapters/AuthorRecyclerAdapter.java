package com.ma7moud3ly.makeyourbook.adapters;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.ma7moud3ly.makeyourbook.data.Author;
import com.ma7moud3ly.makeyourbook.databinding.ItemAuthorBinding;
import com.ma7moud3ly.makeyourbook.observables.UiState;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AuthorRecyclerAdapter extends RecyclerView.Adapter<AuthorRecyclerAdapter.MyViewHolder> {
    private final List<Author> list;
    private int divider = -1;

    public AuthorRecyclerAdapter(List<Author> list, int divider) {
        this.list = list;
        this.divider = divider;
    }

    public AuthorRecyclerAdapter(List<Author> list) {
        this.list = list;
    }


    public class MyViewHolder extends BaseViewHolder {
        private final ItemAuthorBinding binding;

        public MyViewHolder(ItemAuthorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setUi(uiState);
            if (divider != -1)
                setWidth(binding.getRoot(), divider);
        }
    }

    @NonNull
    @Override
    public AuthorRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAuthorBinding binding = ItemAuthorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull AuthorRecyclerAdapter.MyViewHolder holder, int position) {
        if (list != null && position < list.size()) {
            Author author = list.get(position);
            holder.binding.setAuthor(author);
        }
    }

    @NonNull
    @Override
    public int getItemCount() {
        return list.size();
    }


}
