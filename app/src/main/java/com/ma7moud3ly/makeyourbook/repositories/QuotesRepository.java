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
import com.ma7moud3ly.makeyourbook.data.MyPager;
import com.ma7moud3ly.makeyourbook.data.Quote;
import com.ma7moud3ly.makeyourbook.util.SearchHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.AUTHORS_IMGS_DIR;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.QUOTES_DIR;

public class QuotesRepository {
    @Inject
    QuotesRepository() {
    }

    public MutableLiveData<List<Quote>> data = new MutableLiveData<>();
    public MutableLiveData<Long> items_count = new MutableLiveData<>();

    private Quote snapshotToQuote(DataSnapshot snapshot, String author_id) {
        Quote quote = new Quote();
        quote.id = snapshot.getKey();
        quote.author_id = author_id;
        quote.author = snapshot.child("author").getValue().toString();
        quote.source = snapshot.child("source").getValue().toString();
        quote.text = snapshot.child("text").getValue().toString();
        quote.author_img = AUTHORS_IMGS_DIR + "/" + quote.author_id + ".jpg";
        return quote;
    }

    public void count(String ref) {
        Query query = FirebaseDatabase.getInstance().getReference(QUOTES_DIR).child(ref);
        if (App.newVersion) query.keepSynced(true);
        else query.keepSynced(false);
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

    public void read(MyPager pager, String ref) {
        try {
            List<Quote> list = new ArrayList<>();
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(QUOTES_DIR).child(ref);
            Query query = myRef.orderByKey().limitToFirst(pager.page_size);
            if (!pager.last_key.isEmpty()) query = query.startAt(pager.last_key);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshots) {
                    for (DataSnapshot snapshot : snapshots.getChildren()) {
                        Quote quote = snapshotToQuote(snapshot, ref);
                        list.add(quote);
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

    public void search(String ref, String query) {
        try {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(QUOTES_DIR).child(ref);
            if(App.newVersion)myRef.keepSynced(true);
            else  myRef.keepSynced(false);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshots) {
                    List<Quote> matched = new ArrayList<>();
                    for (DataSnapshot snapshot : snapshots.getChildren()) {
                        Quote quote = snapshotToQuote(snapshot, ref);
                        if (SearchHelper.in(quote.text, query) || SearchHelper.in(quote.source, query))
                            matched.add(quote);
                    }
                    data.setValue(matched);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    data.setValue(null);
                    App.l(databaseError.getDetails());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            App.l(e.getMessage());
        }

    }

}
