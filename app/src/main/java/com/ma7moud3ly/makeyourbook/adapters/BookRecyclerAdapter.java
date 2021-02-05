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
import com.ma7moud3ly.makeyourbook.databinding.ItemBookBinding;
import com.ma7moud3ly.makeyourbook.observables.UiState;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.MyViewHolder> {
    final private List<Book> list;
    private boolean is_vertical = true;

    public BookRecyclerAdapter(List<Book> list) {
        this.list = list;
    }

    public BookRecyclerAdapter(List<Book> list, boolean is_vertical) {
        this.list = list;
        this.is_vertical = is_vertical;
    }


    public class MyViewHolder extends BaseViewHolder {
        private final ItemBookBinding binding;

        public MyViewHolder(ItemBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setUi(uiState);
            if (is_vertical)
                setHeight(binding.getRoot(), 3.5);
            else {
                setWidth(binding.getRoot(), 2.8);
                setHeight(binding.getRoot(), 3.5);
            }
        }
    }

    @NonNull
    @Override
    public BookRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookBinding binding = ItemBookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull BookRecyclerAdapter.MyViewHolder holder, int position) {
        if (list != null && position < list.size()) {
            Book book = list.get(position);
            holder.binding.setBook(book);
        }
    }

    @NonNull
    @Override
    public int getItemCount() {
        return list.size();
    }


}
