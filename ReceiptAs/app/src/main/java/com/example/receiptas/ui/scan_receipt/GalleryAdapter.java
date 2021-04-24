package com.example.receiptas.ui.scan_receipt;

import android.appwidget.AppWidgetHost;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.receiptas.R;

//import com.bumptech.glide.glide;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private List<String> images;
    protected PhotoListener photoListener;
    private ArrayList<ViewHolder> holders;

    public GalleryAdapter(Context context){
        this.context = context;
    }

    public GalleryAdapter(Context context, List<String> images, PhotoListener photoListener) {
        this.context = context;
        this.images = images;
        this.photoListener = photoListener;
        this.holders = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position == 0) {
            Glide.with(context).load(context.getDrawable(R.drawable.baseline_photo_camera_24)).into(holder.image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoListener.onPhotoClick(holder, "");
                }
            });
        } else {
            final String image = images.get(position - 1);
            Glide.with(context).load(image).into(holder.image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoListener.onPhotoClick(holder, image);
                }
            });
        }

        holders.add(holder);
    }

    @Override
    public int getItemCount() {
        if(images != null){
            return images.size() + 1;
        }

        return 0;
    }

    public void resetImageViewBackground(){
        float scale = this.context.getResources().getDisplayMetrics().density;
        int paddingSize = (int) (1 * scale + 0.5f);

        for(ViewHolder holder : holders){
            holder.image.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
            holder.image.setBackgroundResource(R.drawable.gallery_border_unselected);
        }
    }

    public ViewHolder getHolder(int index){
        return this.holders.get(index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            image = itemView.findViewById(R.id.image);
        }
    }

    public interface PhotoListener {
        void onPhotoClick(ViewHolder holder, String path);
    }
}
