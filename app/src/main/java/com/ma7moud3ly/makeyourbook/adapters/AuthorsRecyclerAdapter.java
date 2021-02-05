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
import com.ma7moud3ly.makeyourbook.databinding.ItemAllAuthorsBinding;
import com.ma7moud3ly.makeyourbook.observables.UiState;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AuthorsRecyclerAdapter extends RecyclerView.Adapter<AuthorsRecyclerAdapter.MyViewHolder> {
    private final List<Author> list;
    private final int divider;

    public AuthorsRecyclerAdapter(List<Author> list, int divider) {
        this.list = list;
        this.divider = divider;
    }

    public class MyViewHolder extends BaseViewHolder {
        private final ItemAllAuthorsBinding binding;

        public MyViewHolder(ItemAllAuthorsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setUi(uiState);
            setWidth(binding.getRoot(), divider);
        }
    }

    @NonNull
    @Override
    public AuthorsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAllAuthorsBinding binding = ItemAllAuthorsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull AuthorsRecyclerAdapter.MyViewHolder holder, int position) {
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
