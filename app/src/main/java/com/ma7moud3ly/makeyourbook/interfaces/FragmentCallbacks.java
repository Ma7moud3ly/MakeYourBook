
package com.ma7moud3ly.makeyourbook.interfaces;

public interface FragmentCallbacks {
    public void onSearch(String query);

    public void read();

    public void onSearchCleared();

    public void onItemSelected(int index);

    public void onScrollToTop();

    public void onScrollToBottom();

    public void onNetworkRetry();

}

