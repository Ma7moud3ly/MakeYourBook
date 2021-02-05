package com.ma7moud3ly.makeyourbook.adapters;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.activities.BaseActivity;
import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.data.Quote;
import com.ma7moud3ly.makeyourbook.databinding.ItemQuoteBinding;
import com.ma7moud3ly.makeyourbook.models.UserQuotesViewModel;
import com.ma7moud3ly.makeyourbook.util.CapAndShare;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QuotesRecyclerAdapter extends RecyclerView.Adapter<QuotesRecyclerAdapter.MyViewHolder> {
    private List<Quote> list;
    private UserQuotesViewModel userQuotesViewModel = null;

    public QuotesRecyclerAdapter(List<Quote> list) {
        this.list = list;
    }

    public QuotesRecyclerAdapter(List<Quote> list, UserQuotesViewModel userQuotesViewModel) {
        this.list = list;
        this.userQuotesViewModel = userQuotesViewModel;
    }


    public class MyViewHolder extends BaseViewHolder {
        private final ItemQuoteBinding binding;

        public MyViewHolder(ItemQuoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setUi(uiState);
        }
    }

    @NonNull
    @Override
    public QuotesRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuoteBinding binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        Context c = parent.getContext();

        binding.shareQuote.setOnClickListener(view -> {
            binding.logo.setVisibility(View.VISIBLE);
            CapAndShare capAndShare = new CapAndShare(c, binding.quoteDesignLayout);
            if (((BaseActivity) c).checkStoragePermission())
                capAndShare.share(c.getResources().getString(R.string.share_subject), c.getResources().getString(R.string.share_text));
            binding.logo.setVisibility(View.INVISIBLE);
        });

        binding.saveQuote.setOnClickListener(view -> {
            binding.logo.setVisibility(View.VISIBLE);
            CapAndShare capAndShare = new CapAndShare(c, binding.quoteDesignLayout);
            if (((BaseActivity) c).checkStoragePermission() && capAndShare.save()) {
                App.toast(c.getString(R.string.quote_saved_in_storage));
            }
            binding.logo.setVisibility(View.INVISIBLE);
        });

        if (userQuotesViewModel != null) binding.setModel(userQuotesViewModel);
        return new MyViewHolder(binding);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull QuotesRecyclerAdapter.MyViewHolder holder, int position) {
        if (list != null && position < list.size()) {
            Quote quote = list.get(position);
            holder.binding.setQuote(quote);
        }
    }

    @NonNull
    @Override
    public int getItemCount() {
        return list.size();
    }


}
