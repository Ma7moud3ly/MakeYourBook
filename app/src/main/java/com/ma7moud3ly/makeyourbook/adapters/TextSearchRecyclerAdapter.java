package com.ma7moud3ly.makeyourbook.adapters;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ma7moud3ly.makeyourbook.data.TextSearch;
import com.ma7moud3ly.makeyourbook.databinding.ItemStorySearchBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TextSearchRecyclerAdapter extends RecyclerView.Adapter<TextSearchRecyclerAdapter.MyViewHolder> {
    private final List<TextSearch> list;

    public TextSearchRecyclerAdapter(List<TextSearch> list) {
        this.list = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemStorySearchBinding binding;

        public MyViewHolder(ItemStorySearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public TextSearchRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStorySearchBinding binding = ItemStorySearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull TextSearchRecyclerAdapter.MyViewHolder holder, int position) {
        TextSearch entry = list.get(position);
        holder.binding.result.setText(Html.fromHtml(entry.text));
        holder.binding.page.setText("" + (entry.page + 1));

    }

    @NonNull
    @Override
    public int getItemCount() {
        return list.size();
    }


}
