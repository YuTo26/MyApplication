package com.sungkanngoding.myapplication;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder> {

    private List<Integer> imageList;
    private Context context;
    private int currentPosition = 0;
    private Handler handler = new Handler();
    private Runnable imageChangeRunnable = new Runnable() {
        @Override
        public void run() {
            // Ganti gambar saat menjalankan runnable
            currentPosition = (currentPosition + 1) % imageList.size();
            notifyDataSetChanged();
            // Setel pengulangan penggantian gambar setelah 5 detik
            handler.postDelayed(this, 2000); // Ubah angka 5000 menjadi jangka waktu yang diinginkan dalam milidetik (5000 = 5 detik)
        }
    };

    public ImageSliderAdapter(Context context, List<Integer> imageList) {
        this.context = context;
        this.imageList = imageList;
        // Memulai pengulangan penggantian gambar saat adapter dibuat
        handler.postDelayed(imageChangeRunnable, 2000); // Ubah angka 5000 menjadi jangka waktu yang diinginkan dalam milidetik (5000 = 5 detik)
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // Mendapatkan gambar sesuai dengan posisi saat ini
        int imageResource = imageList.get(currentPosition);
        holder.imageView.setImageResource(imageResource);
    }

    @Override
    public int getItemCount() {
        return 1; // Hanya satu gambar yang ditampilkan pada satu waktu
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
