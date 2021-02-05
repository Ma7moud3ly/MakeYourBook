package com.ma7moud3ly.makeyourbook.di.modules;


import com.ma7moud3ly.makeyourbook.di.ViewModelFactory;
import com.ma7moud3ly.makeyourbook.models.ArticlesViewModel;
import com.ma7moud3ly.makeyourbook.models.BookViewModel;
import com.ma7moud3ly.makeyourbook.models.AuthorsViewModel;
import com.ma7moud3ly.makeyourbook.models.FavBooksViewModel;
import com.ma7moud3ly.makeyourbook.models.HomeViewModel;
import com.ma7moud3ly.makeyourbook.models.LibrarySearchModel;
import com.ma7moud3ly.makeyourbook.models.LibraryViewModel;
import com.ma7moud3ly.makeyourbook.models.QuotesViewModel;
import com.ma7moud3ly.makeyourbook.models.TextReaderViewModel;
import com.ma7moud3ly.makeyourbook.models.UserQuotesViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(LibraryViewModel.class)
    abstract ViewModel provideLibraryViewModel(LibraryViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AuthorsViewModel.class)
    abstract ViewModel provideAuthorViewModel(AuthorsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FavBooksViewModel.class)
    abstract ViewModel provideFavBookViewModel(FavBooksViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BookViewModel.class)
    abstract ViewModel provideAboutBookViewModel(BookViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel provideHomeViewModel(HomeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ArticlesViewModel.class)
    abstract ViewModel provideArticlesViewModel(ArticlesViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TextReaderViewModel.class)
    abstract ViewModel provideReaderViewModel(TextReaderViewModel viewModel);
    @Binds
    @IntoMap
    @ViewModelKey(QuotesViewModel.class)
    abstract ViewModel provideQuotesViewModel(QuotesViewModel viewModel);
    @Binds
    @IntoMap
    @ViewModelKey(LibrarySearchModel.class)
    abstract ViewModel provideLibrarySearchModel(LibrarySearchModel viewModel);
    @Binds
    @IntoMap
    @ViewModelKey(UserQuotesViewModel.class)
    abstract ViewModel provideUserQuotesViewModel(UserQuotesViewModel viewModel);
}