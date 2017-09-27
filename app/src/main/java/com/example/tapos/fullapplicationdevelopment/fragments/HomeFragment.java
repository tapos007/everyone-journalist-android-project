package com.example.tapos.fullapplicationdevelopment.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.tapos.fullapplicationdevelopment.R;
import com.example.tapos.fullapplicationdevelopment.adapters.PaginationAdapter;
import com.example.tapos.fullapplicationdevelopment.api.APIService;
import com.example.tapos.fullapplicationdevelopment.api.APIUrl;
import com.example.tapos.fullapplicationdevelopment.model.Datum;
import com.example.tapos.fullapplicationdevelopment.model.LoginInformation;
import com.example.tapos.fullapplicationdevelopment.model.Movie;
import com.example.tapos.fullapplicationdevelopment.model.PostList;
import com.example.tapos.fullapplicationdevelopment.utils.PaginationScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tapos on 9/27/17.
 */

public class HomeFragment extends Fragment {
    private static final String TAG = "FragmentHome";
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    private APIService apiService;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        rv = (RecyclerView) view.findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);

        adapter = new PaginationAdapter(getContext());

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });



        loadFirstPage();

        return view;
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //init service and load data
        APIService service = retrofit.create(APIService.class);
        Call<PostList> call = service.GetAllPostData(this.currentPage);

        call.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                // Got data. Send it to adapter

                List<Datum> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });

    }


    private List<Datum> fetchResults(Response<PostList> response) {
        PostList allPost = response.body();
        TOTAL_PAGES = allPost.getMeta().getPagination().getTotalPages();

        return allPost.getData();
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //init service and load data
        APIService service = retrofit.create(APIService.class);
        Call<PostList> call = service.GetAllPostData(this.currentPage);

        call.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<Datum> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });
    }




}
