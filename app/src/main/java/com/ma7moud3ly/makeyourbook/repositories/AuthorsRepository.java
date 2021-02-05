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
import com.ma7moud3ly.makeyourbook.data.Author;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.data.MyPager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.AUTHORS_DIR;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.AUTHORS_IMGS_DIR;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.BOOKS_IMGS_DIR;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.PDF_BOOKS_DIR;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.TXT_BOOKS_DIR;

public class AuthorsRepository {
    @Inject
    AuthorsRepository() {
    }

    public MutableLiveData<List<Author>> data = new MutableLiveData<>();
    public MutableLiveData<Author> author = new MutableLiveData<>();
    public MutableLiveData<Long> items_count = new MutableLiveData<>();

    private Author snapshotToAuthor(DataSnapshot snapshot) {
        try {
            Author author = new Author();
            author.id = snapshot.getKey();
            author.name = snapshot.child("author_name").getValue().toString();
            author.img = AUTHORS_IMGS_DIR + "/" + author.id + ".jpg";
            List<Book> pdf_books = new ArrayList<>();
            if (snapshot.hasChild("pdf_books")) {
                for (DataSnapshot snapshot1 : snapshot.child("pdf_books").getChildren()) {
                    Book book = new Book();
                    book.id = snapshot1.child("id").getValue().toString();
                    book.img = BOOKS_IMGS_DIR + "/" + book.id + ".jpg";
                    book.name = snapshot1.child("name").getValue().toString();
                    book.author = author.name;
                    book.author_id = author.id;
                    book.author_img = author.img;
                    pdf_books.add(book);
                }
            }
            List<Book> txt_books = new ArrayList<>();
            if (snapshot.hasChild("txt_books")) {
                for (DataSnapshot snapshot1 : snapshot.child("txt_books").getChildren()) {
                    Book book = new Book();
                    book.id = snapshot1.child("id").getValue().toString();
                    book.img = BOOKS_IMGS_DIR + "/" + book.id + ".jpg";
                    book.name = snapshot1.child("name").getValue().toString();
                    book.author = author.name;
                    book.author_id = author.id;
                    book.author_img = author.img;
                    book.is_text = true;
                    txt_books.add(book);
                }
            }
            author.pdf_books = pdf_books;
            author.txt_books = txt_books;
            return author;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void count() {
        Query query = FirebaseDatabase.getInstance().getReference(AUTHORS_DIR);
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
            List<Author> list = new ArrayList<>();
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(AUTHORS_DIR);
            Query query = myRef.orderByKey().limitToFirst(pager.page_size);
            if (!pager.last_key.isEmpty()) query = query.startAt(pager.last_key);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshots) {
                    for (DataSnapshot snapshot : snapshots.getChildren()) {
                        if (pager.last_key.equals(snapshots.getKey())) continue;
                        Author author = snapshotToAuthor(snapshot);
                        if (author != null)
                            list.add(author);
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

    public void read(String id) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(AUTHORS_DIR + "/" + id);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Author a = snapshotToAuthor(snapshot);
                    if (a != null) author.setValue(a);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    App.l(databaseError.getDetails());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
