package com.example.tapos.fullapplicationdevelopment.adapters;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tapos.fullapplicationdevelopment.R;
import com.example.tapos.fullapplicationdevelopment.fragments.HomeFragment;
import com.example.tapos.fullapplicationdevelopment.model.Datum;
import com.example.tapos.fullapplicationdevelopment.model.Movie;
import com.example.tapos.fullapplicationdevelopment.model.PostList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tapos on 9/27/17.
 */

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";

    private List<Datum> postResult;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        postResult = new ArrayList<>();
    }

    public List<Datum> getMovies() {
        return postResult;
    }

    public void setMovies(List<Datum> postResult) {
        this.postResult = postResult;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_list, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Datum result = postResult.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                /*




            blog_image = (ImageView) itemView.findViewById(R.id.blog_image);
            circleView = (ImageView) itemView.findViewById(R.id.circleView);

                 */
                movieVH.postTitle.setText(result.getTitle());
                movieVH.postDescription.setText(result.getDetails());
                movieVH.publisher_name.setText(result.getCreatorInfo().getName());
                movieVH.comment_number.setText(result.getCommentCount());


                Glide
                        .with(context)
                        .load(result.getCoverPhoto())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                // handle failure
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(movieVH.blog_image);




                Glide
                        .with(context)
                        .load(result.getCreatorInfo().getCoverPhoto())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                // handle failure
                              //  movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now
                              //  movieVH.mProgress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(movieVH.circleView);




                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return postResult == null ? 0 : postResult.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == postResult.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Datum r) {
        postResult.add(r);
        notifyItemInserted(postResult.size() - 1);
    }

    public void addAll(List<Datum> moveResults) {
        for (Datum result : moveResults) {
            add(result);
        }
    }

    public void remove(Datum r) {
        int position = postResult.indexOf(r);
        if (position > -1) {
            postResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Datum());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = postResult.size() - 1;
        Datum result = getItem(position);

        if (result != null) {
            postResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Datum getItem(int position) {
        return postResult.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView postTitle;
        private TextView postDescription;
        private TextView publisher_name;
        private TextView comment_number;
        private ImageView blog_image;
        private ImageView circleView;
        private ProgressBar mProgress;


        public MovieVH(View itemView) {
            super(itemView);

            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            postDescription = (TextView) itemView.findViewById(R.id.blog_content);
            publisher_name = (TextView) itemView.findViewById(R.id.publisher_name);
            comment_number = (TextView) itemView.findViewById(R.id.comment_number);
            blog_image = (ImageView) itemView.findViewById(R.id.blog_image);
            circleView = (ImageView) itemView.findViewById(R.id.circleView);
            mProgress = (ProgressBar) itemView.findViewById(R.id.movie_progress);

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}