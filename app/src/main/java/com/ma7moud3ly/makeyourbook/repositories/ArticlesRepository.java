package com.ma7moud3ly.makeyourbook.repositories;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.data.Article;
import com.ma7moud3ly.makeyourbook.data.MyPager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.ARTICLES_DIR;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.ARTICLES_IMGS_DIR;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.AUTHORS_IMGS_DIR;

public class ArticlesRepository {
    @Inject
    ArticlesRepository() {
    }

    public MutableLiveData<List<Article>> data = new MutableLiveData<>();
    public MutableLiveData<Long> items_count = new MutableLiveData<>();

    private Article snapshotToArticle(DataSnapshot snapshot) {
        try {
            Article article = new Article();
            article.author_id = "8992";
            article.id = snapshot.child("id").getValue().toString();
            article.title = snapshot.child("title").getValue().toString();
            article.img = ARTICLES_IMGS_DIR + "/" + article.id + ".jpg";
            article.publish = snapshot.child("publish").getValue().toString();
            article.author = "د.أحمد خالد توفيق";
            article.author_img = AUTHORS_IMGS_DIR + "/" + "8992.jpg";
            return article;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void count() {
        Query query = FirebaseDatabase.getInstance().getReference(ARTICLES_DIR);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items_count.setValue(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                App.l(databaseError.getMessage());
            }

        });
    }

    public void read(MyPager pager) {
        try {
            List<Article> list = new ArrayList<>();
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(ARTICLES_DIR);
            myRef.keepSynced(true);
            myRef.keepSynced(true);

            Query query = myRef.orderByKey().limitToFirst(pager.page_size);
            if (!pager.last_key.isEmpty()) query = query.startAt(pager.last_key);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshots) {
                    for (DataSnapshot snapshot : snapshots.getChildren()) {
                        Article article = snapshotToArticle(snapshot);
                        list.add(article);
                    }
                    data.setValue(list);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    App.l(databaseError.getDetails());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            App.l(e.getMessage());
        }

    }

}
