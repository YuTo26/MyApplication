package com.sungkanngoding.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private List<String> userList;
    private Context context;
    private OnItemClickListener listener;
    private Map<String, Map<String, Object>> usersMap;
    private DatabaseReference mDatabase;

    public interface OnItemClickListener {
        void onReadClick(String username, Map<String, Object> userData);
        void onEditClick(String username, Map<String, Object> userData);
        void onDeleteClick(String username, Map<String, Object> userData);
    }

    public UserListAdapter(List<String> userList, Context context, OnItemClickListener listener, Map<String, Map<String, Object>> usersMap) {
        this.userList = userList;
        this.context = context;
        this.listener = listener;
        this.usersMap = usersMap;
        this.mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_users, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        String username = userList.get(position);
        Map<String, Object> userData = usersMap.get(username);
        holder.bind(username, userData, listener);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUsername,textViewRole;
        private Button buttonRead;
        private Button buttonEdit;
        private Button buttonDelete;
        private ImageView imageViewProfile;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            buttonRead = itemView.findViewById(R.id.buttonRead);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            imageViewProfile = itemView.findViewById(R.id.imageViewUser);
        }

        public void bind(final String username, final Map<String, Object> userData, final OnItemClickListener listener) {
            textViewUsername.setText(username);
            // Tampilkan role jika tersedia

            if (userData != null && userData.containsKey("role")) {
                String role = userData.get("role").toString();
                textViewRole.setText(role);
            } else {
                textViewRole.setText("Role not specified");
            }


            // Tampilkan foto profil jika tersedia
            if (userData != null && userData.containsKey("profile_picture_url") && !userData.get("profile_picture_url").toString().isEmpty()) {
                String profilePictureUrl = userData.get("profile_picture_url").toString();
                Picasso.get().load(profilePictureUrl).centerCrop().fit().into(imageViewProfile);
            } else {
                // Jika tidak ada URL gambar profil, tampilkan gambar dari Firebase Storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("photo_user/anonymous_profile.jpg");
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Picasso.get().load(uri).centerCrop().fit().into(imageViewProfile);
                }).addOnFailureListener(e -> {
                    // Handle any errors
                    imageViewProfile.setImageResource(R.drawable.icon_profilenopic);
                });
            }

            // Handle buttonRead click
            buttonRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userData != null) {
                        listener.onReadClick(username, userData);
                    } else {
                        Toast.makeText(itemView.getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Handle buttonEdit click
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userData != null) {
                        listener.onEditClick(username, userData);
                    } else {
                        Toast.makeText(itemView.getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Handle buttonDelete click
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ambil email_address pengguna dari userData (jika memungkinkan)
                    String email = userData.get("email_address").toString(); // Ubah ini sesuai dengan key yang benar di userData

                    // Query untuk memeriksa apakah email terdaftar di Firebase Database
                    Query query = mDatabase.orderByChild("email_address").equalTo(email);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean userFound = false;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Cek apakah email_address cocok
                                String userEmail = snapshot.child("email_address").getValue(String.class);
                                if (userEmail != null && userEmail.equals(email)) {
                                    // Email cocok, dapatkan UID pengguna
                                    String uid = snapshot.getKey();

                                    // Hapus pengguna dari Firebase Database
                                    mDatabase.child(uid).removeValue()
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(itemView.getContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();

                                                // Hapus item dari userList dan usersMap
                                                userList.remove(username);
                                                usersMap.remove(username);

                                                // Notifikasi adapter bahwa data telah berubah
                                                notifyDataSetChanged();

                                                // Panggil listener untuk memberitahu bahwa item dihapus
                                                listener.onDeleteClick(username, userData);
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(itemView.getContext(), "Failed to delete user", Toast.LENGTH_SHORT).show();
                                            });

                                    userFound = true;
                                    break;
                                }
                            }

                            if (!userFound) {
                                // Jika pengguna tidak ditemukan
                                Toast.makeText(itemView.getContext(), "User not found in database", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Penanganan batal jika diperlukan
                            Toast.makeText(itemView.getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
