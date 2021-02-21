package com.example.vschatapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vschatapp.Adapters.FriendAdapter;
import com.example.vschatapp.Adapters.MesajlarAdapter;
import com.example.vschatapp.Adapters.MessageAdapter;
import com.example.vschatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MesajlarFragment extends Fragment {

    View view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    RecyclerView friend_recy;
    MesajlarAdapter mesajlarAdapter;
    List<String> keyList;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mesajlar, container, false);
        tanimla();
        getMesajList();
        return  view;
    }
    public void tanimla()
    {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        keyList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance("https://chat-e38ba-default-rtdb.firebaseio.com/");
        reference = firebaseDatabase.getReference().child("Mesajlar");
        friend_recy = (RecyclerView) view.findViewById(R.id.friend_recy);
        RecyclerView.LayoutManager layoutManager= new GridLayoutManager(getContext(),1);
        friend_recy.setLayoutManager(layoutManager);
        mesajlarAdapter = new MesajlarAdapter(keyList,getActivity(),getContext());
        friend_recy.setAdapter(mesajlarAdapter);
    }
    public  void  getMesajList()
    {
        reference.child(firebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                 if (keyList.indexOf(dataSnapshot.getKey())==-1)
                 {
                     keyList.add(dataSnapshot.getKey());

                 }
                mesajlarAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}