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
import com.ma7moud3ly.makeyourbook.databinding.ItemLibraryBinding;
import com.ma7moud3ly.makeyourbook.observables.UiState;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LibraryRecyclerAdapter extends RecyclerView.Adapter<LibraryRecyclerAdapter.MyViewHolder> {
    private List<Book> list;
    private int divider = 1;



    public LibraryRecyclerAdapter(List<Book> list, int divider) {
        this.list = list;
        this.divider = divider;
    }

    public class MyViewHolder extends BaseViewHolder {
        private final ItemLibraryBinding binding;

        public MyViewHolder(ItemLibraryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setUi(uiState);
            setHeight(binding.getRoot(), divider);
        }
    }

    @NonNull
    @Override
    public LibraryRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLibraryBinding binding = ItemLibraryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull LibraryRecyclerAdapter.MyViewHolder holder, int position) {
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
