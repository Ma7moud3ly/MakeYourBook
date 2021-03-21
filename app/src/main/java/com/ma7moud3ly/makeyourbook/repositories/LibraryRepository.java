package com.ma7moud3ly.makeyourbook.repositories;
/**
 * اصنع كتابك Make your Book
 *
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */

import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.data.MyPager;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;
import com.ma7moud3ly.makeyourbook.util.SearchHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.AUTHORS_IMGS_DIR;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.BOOKS_IMGS_DIR;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.E_BOOKS_DIR;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.PDF_BOOKS_DIR;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.TXT_BOOKS_DIR;

public class LibraryRepository {
    @Inject
    LibraryRepository() {
    }

    public MutableLiveData<Book> book = new MutableLiveData<>();
    public MutableLiveData<List<Book>> data = new MutableLiveData<>();
    public MutableLiveData<Long> items_count = new MutableLiveData<>();

    private Book pdfSnapshotToBook(DataSnapshot snapshot) {
        try {
            Book book = new Book();
            book.id = snapshot.getKey();
            book.name = snapshot.child("name").getValue().toString();
            book.img = BOOKS_IMGS_DIR + "/" + book.id + ".jpg";
            book.author = snapshot.child("author").getValue().toString();
            book.author_id = snapshot.child("author_id").getValue().toString();
            book.author_img = AUTHORS_IMGS_DIR + "/" + book.author_id + ".jpg";
            book.type = 12;
            book.category = snapshot.child("category").getValue().toString();
            book.download_link = snapshot.child("download_link").getValue().toString();
            book.format = snapshot.child("format").getValue().toString();
            book.size = snapshot.child("size").getValue().toString();
            return book;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Book txtSnapshotToBook(DataSnapshot c) {
        try {
            Book book = new Book();
            book.id = c.getKey();
            book.name = c.child("name").getValue().toString();
            book.img = BOOKS_IMGS_DIR + "/" + book.id + ".jpg";
            book.author = c.child("author").getValue().toString();
            book.author_id = c.child("author_id").getValue().toString();
            book.author_img = AUTHORS_IMGS_DIR + "/" + book.author_id + ".jpg";
            book.type = 11;
            return book;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Book ebookSnapshotToBook(DataSnapshot c) {
        try {
            Book book = new Book();
            book.id = c.getKey();
            book.name = c.child("name").getValue().toString();
            book.img = BOOKS_IMGS_DIR + "/" + book.id + ".jpg";
            book.author = c.child("author").getValue().toString();
            book.author_id = c.child("author_id").getValue().toString();
            book.author_img = AUTHORS_IMGS_DIR + "/" + book.author_id + ".jpg";
            book.type = 10;
            return book;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Book> searchBooksSnapshot(Iterable<DataSnapshot> snapshots, String query) {
        List<Book> list = new ArrayList<>();
        for (DataSnapshot snapshot : snapshots) {
            Book book = new Book();
            book.name = snapshot.child("name").getValue().toString();
            book.author = snapshot.child("author").getValue().toString();
            if (SearchHelper.in(book.author, query) || SearchHelper.in(book.name, query)) {
                book.author_id = snapshot.child("author_id").getValue().toString();
                book.id = snapshot.child("id").getValue().toString();
                book.type = (long) snapshot.child("type").getValue();
                book.img = BOOKS_IMGS_DIR + "/" + book.id + ".jpg";
                book.author_img = AUTHORS_IMGS_DIR + "/" + book.author_id + ".jpg";
                list.add(book);
            }
        }
        return list;
    }

    public void count(String ref) {
        Query query = FirebaseDatabase.getInstance().getReference(ref);
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
            List<Book> list = new ArrayList<>();
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(ref);
            if (App.newVersion) myRef.keepSynced(true);
            else myRef.keepSynced(false);
            Query query = myRef.orderByKey().limitToFirst(pager.page_size);
            if (!pager.last_key.isEmpty()) query = query.startAt(pager.last_key);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshots) {
                    if (ref.equals(E_BOOKS_DIR))
                        for (DataSnapshot snapshot : snapshots.getChildren()) {
                            if (pager.last_key.equals(snapshots.getKey())) continue;
                            Book book = ebookSnapshotToBook(snapshot);
                            if (book != null) list.add(book);
                        }
                    else if (ref.equals(TXT_BOOKS_DIR))
                        for (DataSnapshot snapshot : snapshots.getChildren()) {
                            if (pager.last_key.equals(snapshots.getKey())) continue;
                            Book book = txtSnapshotToBook(snapshot);
                            if (book != null) list.add(book);
                        }
                    else if (ref.equals(PDF_BOOKS_DIR))
                        for (DataSnapshot snapshot : snapshots.getChildren()) {
                            if (pager.last_key.equals(snapshots.getKey())) continue;
                            Book book = pdfSnapshotToBook(snapshot);
                            if (book != null) list.add(book);
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

    public void read(String id, String ref) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(ref + "/" + id);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (ref.equals(E_BOOKS_DIR)) {
                        Book b = ebookSnapshotToBook(snapshot);
                        if (b != null) book.setValue(b);
                    } else if (ref.equals(TXT_BOOKS_DIR)) {
                        Book b = txtSnapshotToBook(snapshot);
                        if (b != null) book.setValue(b);
                    } else if (ref.equals(PDF_BOOKS_DIR)) {
                        Book b = pdfSnapshotToBook(snapshot);
                        if (b != null) book.setValue(b);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    book.setValue(null);
                    App.l(databaseError.getDetails());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search(String query) {
        try {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(CONSTANTS.BOOKS_SEARCH_DIR);
            if (App.newVersion) myRef.keepSynced(true);
            else myRef.keepSynced(false);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshots) {
                    List<Book> matched = searchBooksSnapshot(snapshots.getChildren(), query);
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
