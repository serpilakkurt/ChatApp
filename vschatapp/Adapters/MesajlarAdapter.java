package com.example.vschatapp.Adapters;


import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vschatapp.Activity.ChatActivity;
import com.example.vschatapp.Fragments.MesajlarFragment;
import com.example.vschatapp.Models.MessageModel;
import com.example.vschatapp.R;
import com.example.vschatapp.Utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MesajlarAdapter extends RecyclerView.Adapter<MesajlarAdapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String userId;
    int tut=0 , tut2=1;
    Boolean seen =false;

    public MesajlarAdapter(List<String> userKeysList, Activity activity, Context context) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance("https://chat-e38ba-default-rtdb.firebaseio.com/");
        reference = firebaseDatabase.getReference();
        auth=FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userId = firebaseUser.getUid();

    }

    //layout tanımlaması yapılacak
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view;

        if (tut==2)
        {
            view =LayoutInflater.from(context).inflate(R.layout.message_sent_layout,parent,false);
            return new ViewHolder(view);
        }else
        {
            view =LayoutInflater.from(context).inflate(R.layout.message_layout,parent,false);
            return new ViewHolder(view);

        }
    }



   //viewlere setlemeler yapılacak
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {

        //holder.usernameTextView.setText(userKeysList.get(position).toString());

        reference.child("Kullanicilar").child(userKeysList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final @NonNull DataSnapshot dataSnapshot) {

                String userName = dataSnapshot.child("isim").getValue().toString();
                String userImage = dataSnapshot.child("resim").getValue().toString();
                Boolean stateUser = Boolean.parseBoolean(dataSnapshot.child("state").getValue().toString());
                if (stateUser==true)
                {
                    holder.message_state_image.setImageResource(R.drawable.online_icon);
                }else
                {
                    holder.message_state_image.setImageResource(R.drawable.offline_icon);

                }
                    Picasso.get().load(userImage).into(holder.message_image);
                    holder.message_text.setText(userName);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //adapteri oluşturulacak olan listenin size
    @Override
    public int getItemCount() {

        return userKeysList.size();
    }

    //viewleri tanımlama işlemiyapılacak
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView message_text,message_text2;
        CircleImageView message_state_image,message_image;

           ViewHolder(View itemView)
           {
               super(itemView);
               message_text = (TextView)itemView.findViewById(R.id.message_text);
               message_state_image = (CircleImageView)itemView.findViewById(R.id.message_state_image);
               message_image = (CircleImageView)itemView.findViewById(R.id.message_image);


           }


    }


}


