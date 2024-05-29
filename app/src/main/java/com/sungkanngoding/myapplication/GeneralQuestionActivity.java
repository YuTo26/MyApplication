package com.sungkanngoding.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GeneralQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_general_question);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set dynamic text for answers
        TextView answer1 = findViewById(R.id.answer1);
        TextView answer2 = findViewById(R.id.answer2);
        TextView answer3 = findViewById(R.id.answer3);
        TextView answer4 = findViewById(R.id.answer4);
        TextView answer5 = findViewById(R.id.answer5);
        TextView answer6 = findViewById(R.id.answer6);
        TextView answer7 = findViewById(R.id.answer7);

        answer1.setText("NabungSampah adalah aplikasi jual beli sampah online (Recycling platform). NabungSampah menghubungkan penghasil sampah (rumah tangga, bisnis, dan kantor) dengan kolektor lokal (pengepul) terdekat, sehingga lebih mudah dalam menjual sampah. NabungSampah berfokus pada pola daur ulang sampah di sektor hulu, bukan layanan pengangkutan sampah atau kerajinan tangan daur ulang.");
        answer2.setText("Layanan NabungSampah gratis digunakan oleh siapa saja. Kami bukan perusahaan layanan penjemputan sampah, kami mencoba memberikan nilai yang lebih dari itu, yaitu sampah yang dijemput oleh mitra kami, seluruhnya akan didaur ulang dan tidak dibuang ke TPA. Baik layanan menjual sampah atau program daur ulang, semuanya akan didaur ulang kembali.");
        answer3.setText("Menjual sampah melalui NabungSampah sangatlah mudah, pastikan kamu sudah mendownload aplikasi NabungSampah di Play Store atau iOS. Selanjutnya, buka aplikasi NabungSampah, pilih fitur “jual sampah”, masukkan informasi sampah, alamat, dan waktu penjemputan, submit, dan tunggu sampai pengepul menjemput dan membayar sampahmu. Selengkapnya lihat panduan menjual sampah.");
        answer4.setText("Jika kamu pemilik bisnis atau penyelenggara event, kamu dapat terlibat dalam menciptakan dunia yang lebih hijau. Buka aplikasi NabungSampah, pilih fitur “program daur ulang”, selanjutnya masukkan informasi bisnis/event-mu. Setelah berhasil mendaftar, kamu akan mendapatkan email panduan untuk mempelajari lebih lanjut. Selengkapnya lihat panduan “program daur ulang”.");
        answer5.setText("Sampah yang dikelola oleh NabungSampah adalah jenis sampah kering seperti:\n\n1. Sampah berbahan plastik\n2. Sampah berbahan kertas\n3. Sampah aluminium & kaleng\n4. Sampah besi & logam\n5. Sampah elektronik\n6. Sampah botol kaca\n7. Barang donasi seperti pakaian dan mainan\n8. Beberapa sampah khusus seperti minyak bekas, pecahan beling, dll.");
        answer6.setText("NabungSampah adalah Perusahaan Teknologi yang berbasis sosial. Mengapa perusahaan? Kami ingin NabungSampah dapat berjalan mandiri, sustainable, dan berdampak luas, tidak bergantung pada donasi/sumbangan dan pihak-pihak lain. Dengan demikian, NabungSampah dapat sustainable dan berdampak luas bagi lingkungan dan sosial.");
        answer7.setText("Jika Anda adalah pelaku usaha di bidang daur ulang sampah seperti pengepul, Bank Sampah, pemulung, atau produsen produk daur ulang, Anda dapat bergabung menjadi mitra NabungSampah.");
    }
}
