package com.ma7moud3ly.makeyourbook.adapters;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ma7moud3ly.makeyourbook.data.Article;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.databinding.ItemArticleBinding;
import com.ma7moud3ly.makeyourbook.observables.UiState;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleRecyclerAdapter extends RecyclerView.Adapter<ArticleRecyclerAdapter.MyViewHolder> {
    private List<Article> list;
    private int divider = 1;


    public ArticleRecyclerAdapter(List<Article> list, int divider) {
        this.list = list;
        this.divider = divider;
    }

    public class MyViewHolder extends BaseViewHolder {
        private final ItemArticleBinding binding;

        public MyViewHolder(ItemArticleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setUi(uiState);
            setHeight(binding.getRoot(), divider);
        }
    }

    @NonNull
    @Override
    public ArticleRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArticleBinding binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ArticleRecyclerAdapter.MyViewHolder holder, int position) {
        if (list != null && position < list.size()) {
            Article article = list.get(position);
            holder.binding.setArticle(article);
        }
    }

    @NonNull
    @Override
    public int getItemCount() {
        return list.size();
    }


}
