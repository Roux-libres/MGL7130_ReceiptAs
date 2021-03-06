package com.example.receiptas.ui.scan_receipt;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.receiptas.R;

import java.util.ArrayList;
import java.util.List;

//import com.bumptech.glide.glide;

public class ProcessedImageAdapter extends GalleryAdapter {

    private Context context;
    private List<Bitmap> images;
    private ArrayList<ViewHolder> holders;
    private PhotoListener photoListener;

    public ProcessedImageAdapter(Context context, List<Bitmap> images, PhotoListener photoListener) {
        super(context);
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
        final Bitmap image = images.get(position);

        Glide.with(context).asBitmap().load(image).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoListener.onPhotoClick(holder, image);
            }
        });

        holder.image.setBackgroundResource(R.drawable.gallery_border_selected);
        holders.add(holder);
    }

    @Override
    public int getItemCount() {
        if(images != null){
            return images.size();
        }

        return 0;
    }

    public interface PhotoListener {
        void onPhotoClick(ViewHolder holder, Bitmap imageBitmap);
    }
}
