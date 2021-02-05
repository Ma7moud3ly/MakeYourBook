package com.ma7moud3ly.makeyourbook.models;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import com.ma7moud3ly.makeyourbook.data.Home;
import com.ma7moud3ly.makeyourbook.repositories.HomeRepository;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class HomeViewModel extends ViewModel {

    public MutableLiveData<Home> data = new MutableLiveData<>();
    private HomeRepository repo;

    @Inject
    public HomeViewModel(HomeRepository repo) {
        this.repo = repo;
        data = repo.data;
    }


    public void read() {
        repo.read();
    }

    public boolean nullOrEmpty() {
        return data.getValue() == null;
    }

}
