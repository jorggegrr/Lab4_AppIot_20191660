package com.example.contactosysensores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.contactosysensores.Contact;
import java.util.List;
import com.bumptech.glide.Glide;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private List<Contact> contactList;


    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private TextView tvName, tvGender, tvCity, tvCountry, tvEmail, tvPhone;
        private ImageButton deleteButton;

        public ContactViewHolder(@NonNull View itemView) {

            super(itemView);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            tvName = itemView.findViewById(R.id.tvName);
            tvGender = itemView.findViewById(R.id.tvGender);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
        public void bind(Contact contact) {
            // Configurar los elementos de la vista con los datos del contacto
            Glide.with(context).load(contact.getPhotoUrl()).into(ivPhoto);
            tvName.setText(contact.getName());
            tvGender.setText(contact.getGender());
            tvCity.setText(contact.getCity());
            tvCountry.setText(contact.getCountry());
            tvEmail.setText(contact.getEmail());
            tvPhone.setText(contact.getPhone());

            // Manejar la acción del botón de eliminación
            deleteButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Eliminar el contacto de la lista y notificar al adaptador
                    contactList.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }


    }
}

