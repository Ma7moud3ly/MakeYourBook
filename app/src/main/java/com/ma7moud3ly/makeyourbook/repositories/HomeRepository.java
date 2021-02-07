package com.ma7moud3ly.makeyourbook.repositories;
/**
 * اصنع كتابك Make your Book
 *
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.data.Author;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.data.Home;
import com.ma7moud3ly.makeyourbook.data.Collection;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;

import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.AUTHORS_IMGS_DIR;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.BOOKS_IMGS_DIR;

public class HomeRepository {
    private final Context context;

    @Inject
    HomeRepository(Context context) {
        this.context = context;
    }

    public MutableLiveData<Home> data = new MutableLiveData<>();

    private List<Author> authorsSnapshot(Iterable<DataSnapshot> snapshots) {
        List<Author> list = new ArrayList<>();
        for (DataSnapshot snapshot : snapshots) {
            String id = snapshot.child("id").getValue().toString();
            String name = snapshot.child("name").getValue().toString();
            String img = AUTHORS_IMGS_DIR + "/" + id + ".jpg";
            list.add(new Author(id, name, img));
        }
        return list;
    }

    private List<Book> booksSnapshot(Iterable<DataSnapshot> snapshots) {
        List<Book> list = new ArrayList<>();
        for (DataSnapshot snapshot : snapshots) {
            Book book = new Book();
            book.id = snapshot.getKey();
            book.name = snapshot.child("name").getValue().toString();
            book.author = snapshot.child("author").getValue().toString();
            book.author_id = snapshot.child("author_id").getValue().toString();
            book.img = BOOKS_IMGS_DIR + "/" + book.id + ".jpg";
            book.author_img = AUTHORS_IMGS_DIR + "/" + book.author_id + ".jpg";
            if (snapshot.hasChild("download_link")) {
                book.is_text = false;
                book.category = snapshot.child("category").getValue().toString();
                book.download_link = snapshot.child("download_link").getValue().toString();
                book.format = snapshot.child("format").getValue().toString();
                book.size = snapshot.child("size").getValue().toString();
            } else book.is_text = true;

            list.add(book);
        }
        return list;
    }

    private List<Collection> homeCollectionSnapshot(Iterable<DataSnapshot> snapshots) {
        List<Collection> list = new ArrayList<>();
        for (DataSnapshot snapshot : snapshots) {
            Collection collection = new Collection();
            collection.id = snapshot.child("id").getValue().toString();
            collection.title = snapshot.child("title").getValue().toString();
            collection.books = booksSnapshot(snapshot.child("books").getChildren());

            list.add(collection);
        }
        return list;
    }


    public void read() {
        try {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(CONSTANTS.HOME_DIR);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshots) {
                    Home home = new Home();
                    try {
                        CONSTANTS.DOWNLOAD_BASE = snapshots.child("constants").child("download_base").getValue().toString();
                    } catch (Exception e) {
                        data.setValue(null);
                        FirebaseDatabase.getInstance().getReference(CONSTANTS.HOME_DIR).keepSynced(false);
                        return;
                    }
                    home.authors = authorsSnapshot(snapshots.child("authors").getChildren());
                    home.quotes = authorsSnapshot(snapshots.child("quotes").getChildren());
                    home.collections = homeCollectionSnapshot(snapshots.child("collections").getChildren());
                    Collection mostRead = new Collection("", context.getString(R.string.most_read),
                            booksSnapshot(snapshots.child("most_read").getChildren()));
                    home.collections.add(0, mostRead);
                    data.setValue(home);
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
