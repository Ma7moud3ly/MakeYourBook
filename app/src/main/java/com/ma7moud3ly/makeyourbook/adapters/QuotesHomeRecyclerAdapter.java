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
import com.ma7moud3ly.makeyourbook.databinding.ItemQuoteHomeBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QuotesHomeRecyclerAdapter extends RecyclerView.Adapter<QuotesHomeRecyclerAdapter.MyViewHolder> {
    private List<Author> list;

    public QuotesHomeRecyclerAdapter(List<Author> list) {
        this.list = list;
    }

    public class MyViewHolder extends BaseViewHolder {
        private final ItemQuoteHomeBinding binding;

        public MyViewHolder(ItemQuoteHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setUi(uiState);
        }
    }

    @NonNull
    @Override
    public QuotesHomeRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuoteHomeBinding binding = ItemQuoteHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull QuotesHomeRecyclerAdapter.MyViewHolder holder, int position) {
        if (list != null && position < list.size()) {
            Author entry = list.get(position);
            holder.binding.setAuthor(entry);
        }
    }

    @NonNull
    @Override
    public int getItemCount() {
        return list.size();
    }


}
