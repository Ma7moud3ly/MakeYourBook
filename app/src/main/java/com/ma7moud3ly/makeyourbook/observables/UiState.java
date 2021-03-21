package com.ma7moud3ly.makeyourbook.observables;
/**
 * اصنع كتابك Make your Book
 *
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.activities.BaseActivity;
import com.ma7moud3ly.makeyourbook.activities.BookActivity;
import com.ma7moud3ly.makeyourbook.activities.MainActivity;
import com.ma7moud3ly.makeyourbook.activities.QuoteActivity;
import com.ma7moud3ly.makeyourbook.activities.ReaderActivity;
import com.ma7moud3ly.makeyourbook.data.Article;
import com.ma7moud3ly.makeyourbook.data.Author;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.data.Quote;
import com.ma7moud3ly.makeyourbook.data.Story;
import com.ma7moud3ly.makeyourbook.fragments.AboutFragment;
import com.ma7moud3ly.makeyourbook.fragments.HomeFragment;
import com.ma7moud3ly.makeyourbook.fragments.NotificationsFragment;
import com.ma7moud3ly.makeyourbook.fragments.authors.AboutAuthorFragment;
import com.ma7moud3ly.makeyourbook.fragments.authors.AuthorsFragment;
import com.ma7moud3ly.makeyourbook.fragments.library.ArticlesFragment;
import com.ma7moud3ly.makeyourbook.fragments.library.FavBooksFragment;
import com.ma7moud3ly.makeyourbook.fragments.library.LibraryEbookFragment;
import com.ma7moud3ly.makeyourbook.fragments.library.LibraryPdfFragment;
import com.ma7moud3ly.makeyourbook.fragments.library.LibraryTextFragment;
import com.ma7moud3ly.makeyourbook.fragments.quotes.QuotesFragment;
import com.ma7moud3ly.makeyourbook.fragments.quotes.CreateQuoteFragment;
import com.ma7moud3ly.makeyourbook.fragments.book.UserQuotesFragment;
import com.ma7moud3ly.makeyourbook.fragments.quotes.QuotesSearchFragment;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;

import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.E_BOOKS;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.PDF_BOOKS;
import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.TXT_BOOKS;

@Singleton
public class UiState {
    public ObservableField<Boolean> showMenu = new ObservableField<>();
    public ObservableField<Boolean> showHeader = new ObservableField<>();
    public ObservableField<Boolean> showFooter = new ObservableField<>();
    public ObservableField<Boolean> isHome = new ObservableField<>();
    public ObservableField<Boolean> showSearch = new ObservableField<>();
    public ObservableField<Integer> state = new ObservableField<>();
    public ObservableField<Integer> library = new ObservableField<>();
    public final ObservableField<Integer> searchResults = new ObservableField<>();

    @Inject
    public UiState() {
        showMenu.set(false);
        showHeader.set(true);
        showFooter.set(true);
        isHome.set(true);
        showSearch.set(true);
        searchResults.set(-1);
        library.set(CONSTANTS.LIB_MOST_READ);
    }

    public void toggleMenu(View v) {
        showMenu.set(!showMenu.get());
    }

    public void quoteSearch(Context c, String author_id) {
        Bundle bundle = new Bundle();
        bundle.putString("author_id", author_id);
        ((QuoteActivity) c).navigateTo(new QuotesSearchFragment(), bundle, true);
    }

    public void home(View view) {
        ((MainActivity) view.getContext()).navigateTo(new HomeFragment(), null, true);
    }

    public void fav(View view) {
        ((MainActivity) view.getContext()).navigateTo(new FavBooksFragment(), null, true);
    }

    public void quotes(View view) {
        Context context = view.getContext();
        this.library.set(CONSTANTS.LIB_QUOTES);
        context.startActivity(new Intent(context, QuoteActivity.class));
    }

    public void library(View view) {
        ((MainActivity) view.getContext()).navigateTo(new LibraryEbookFragment(), null, true);
    }


    public void about(View view) {
        ((MainActivity) view.getContext()).navigateTo(new AboutFragment(), null, true);
    }

    public void eBooks(View view) {
        this.library.set(CONSTANTS.LIB_E_BOOKS);
        ((MainActivity) view.getContext()).navigateTo(new LibraryEbookFragment(), null, true);
    }

    public void textBooks(View view) {
        this.library.set(CONSTANTS.LIB_TXT_BOOKS);
        ((MainActivity) view.getContext()).navigateTo(new LibraryTextFragment(), null, true);
    }

    public void pdfBooks(View view) {
        this.library.set(CONSTANTS.LIB_PDF_BOOKS);
        ((MainActivity) view.getContext()).navigateTo(new LibraryPdfFragment(), null, true);
    }

    public void openArticles(View view) {
        this.library.set(CONSTANTS.LIB_ARTICLES);
        ((MainActivity) view.getContext()).navigateTo(new ArticlesFragment(), null, true);
    }

    public void authors(View view) {
        this.library.set(CONSTANTS.LIB_AUTHORS);
        ((MainActivity) view.getContext()).navigateTo(new AuthorsFragment(), null, true);
    }


    public void openBook(View v, Book book) {
        Intent intent = new Intent(v.getContext(), ReaderActivity.class);
        if (book.type == E_BOOKS)
            intent.putExtra("book", book.toString());
        else if (book.type == TXT_BOOKS) {
            Story story = new Story(book);
            story.ref = CONSTANTS.BOOKS_DIR;
            intent.putExtra("story", story.toString());
        }
        v.getContext().startActivity(intent);
    }

    public void openArticle(View v, Article article) {
        Intent intent = new Intent(v.getContext(), ReaderActivity.class);
        Story story = new Story(article);
        story.ref = CONSTANTS.ARTICLES_DIR;
        intent.putExtra("story", story.toString());
        v.getContext().startActivity(intent);
    }

    public void openAuthor(View v, Author author) {
        Bundle bundle = new Bundle();
        bundle.putString("author", author.toString());
        ((MainActivity) v.getContext()).navigateTo(new AboutAuthorFragment(), bundle, true);
    }

    public void openAuthor(View v, String author_id) {
        Bundle bundle = new Bundle();
        bundle.putString("author_id", author_id);
        ((BaseActivity) v.getContext()).navigateTo(new AboutAuthorFragment(), bundle, true);
    }

    public void openNotifications(View v) {
        ((MainActivity) v.getContext()).navigateTo(new NotificationsFragment(), null, true);
    }

    public void bookDetails(View v, Book book) {
        Intent intent = new Intent(v.getContext(), BookActivity.class);
        if (((book.type == TXT_BOOKS || book.type == E_BOOKS) && book.author_id != null) || book.download_link != null)
            intent.putExtra("book", book.toString());
        else {
            intent.putExtra("book_id", book.id);
            String ref = "";
            if (book.type == E_BOOKS) ref = CONSTANTS.E_BOOKS_DIR;
            else if (book.type == TXT_BOOKS) ref = CONSTANTS.TXT_BOOKS_DIR;
            else if (book.type == PDF_BOOKS) ref = CONSTANTS.PDF_BOOKS_DIR;
            intent.putExtra("ref", ref);
        }
        v.getContext().startActivity(intent);
    }

    public void shareBook(View v, Book book) {
        Context context = v.getContext();
        String url;
        String type = "";
        if (book.type == E_BOOKS) type = "e-book/";
        else if (book.type == TXT_BOOKS) type = "text-book/";
        else if (book.type == PDF_BOOKS) type = "pdf-book/";
        url = "https://makeubook.web.app/" + type + book.id;
        String content = book.name + " - " + book.author + "\n" + url;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getString(R.string.app_name)));
    }

    public void shareAuthor(View v, Author author) {
        Context context = v.getContext();
        String url = "https://makeubook.web.app/author/" + author.id;
        String content = author.name + "\n" + url;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getString(R.string.app_name)));
    }

    public void copyLinkBook(View v, Book book) {
        String type = "";
        if (book.type == E_BOOKS) type = "e-book/";
        else if (book.type == TXT_BOOKS) type = "text-book/";
        else if (book.type == PDF_BOOKS) type = "pdf-book/";
        String url = "https://makeubook.web.app/" + type + book.id;
        String data = book.name + " - " + book.author + "\n" + url;
        ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copied", data);
        clipboard.setPrimaryClip(clip);
        App.toast(v.getContext().getString(R.string.book_link_copied));
    }

    public void copyLinkBook(View v, Author author) {
        String url = "https://makeubook.web.app/author/" + author.id;
        String data = author.name + "\n" + url;
        ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copied", data);
        clipboard.setPrimaryClip(clip);
        App.toast(v.getContext().getString(R.string.author_link_copied));
    }

    public void userQuotes(View v, Book book) {
        Bundle bundle = new Bundle();
        bundle.putString("book", book.toString());
        ((BaseActivity) v.getContext()).navigateTo(new UserQuotesFragment(), bundle, true);
    }

    public void snackMsg(View v, String s) {
        Snackbar snackbar = Snackbar.make(v, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void openQuotes(View v, String id) {
        Bundle bundle = new Bundle();
        bundle.putString("author_id", id);
        ((QuoteActivity) v.getContext()).navigateTo(new QuotesFragment(), bundle, true);
    }

    public void copyQuote(View v, Quote quote) {
        ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copied", quote.text);
        clipboard.setPrimaryClip(clip);
        App.toast("copied..");
    }


    public void createQuotes(View v) {
        ((BaseActivity) v.getContext()).navigateTo(new CreateQuoteFragment(), null, true);
    }

    public void createQuotes(View v, Quote entry) {
        Bundle bundle = new Bundle();
        bundle.putString("quote", entry.toString());
        ((BaseActivity) v.getContext()).navigateTo(new CreateQuoteFragment(), bundle, true);
    }


    @BindingAdapter("src")
    public static void src(ImageView view, long type) {
        Resources r = view.getContext().getResources();
        int drawable;
        if (type == E_BOOKS) drawable = R.drawable.ebook;
        else if (type == TXT_BOOKS) drawable = R.drawable.text;
        else drawable = R.drawable.pdf;
        view.setImageResource(drawable);
    }

}
