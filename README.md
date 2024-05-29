# Aplikasi Bank Sampah

Aplikasi Bank Sampah adalah aplikasi yang dirancang untuk mempermudah pengelolaan dan pencatatan sampah yang dapat didaur ulang. Aplikasi ini memiliki berbagai fitur seperti pendaftaran pengguna, pencatatan transaksi, dan pemantauan saldo pengguna.

## Fitur

1. **Splash Screen**
   - Tampilan awal saat aplikasi dibuka.
   - Mengarahkan pengguna ke halaman login atau pendaftaran.

2. **Login/Sign Up**
   - Pengguna dapat mendaftar atau masuk ke akun mereka.

3. **Dashboard Utama**
   - Menggunakan Bottom Navigation Bar untuk navigasi antara berbagai fragment:
     - HomeFragment
     - SalesFragment
     - BalanceFragment
     - ProfileFragment

4. **HomeFragment**
   - Tampilan utama berisi informasi umum dan menu utama.

5. **SalesFragment**
   - Mencatat dan menampilkan transaksi penjualan sampah.

6. **BalanceFragment**
   - Menampilkan saldo pengguna.

7. **ProfileFragment**
   - Menampilkan dan mengedit informasi profil pengguna.

## Struktur Proyek

- `MainActivity.java`
  - Activity utama yang mengelola navigasi antara fragment menggunakan Bottom Navigation Bar.

- `SplashActivity.java`
  - Activity yang ditampilkan pertama kali saat aplikasi dibuka, menampilkan logo dan slogan aplikasi.

- `GetStartedActivity.java`
  - Activity yang mengarahkan pengguna untuk login atau mendaftar.

- `HomeFragment.java`
  - Fragment untuk tampilan utama.

- `SalesFragment.java`
  - Fragment untuk melihat transaksi penjualan sampah.

- `BalanceFragment.java`
  - Fragment untuk melihat dan mengelola saldo pengguna.

- `ProfileFragment.java`
  - Fragment untuk melihat dan mengedit profil pengguna.

## Instalasi

1. Clone repositori ini:

   ```bash
   git clone (https://github.com/YuTo26/MyApplication.git)
