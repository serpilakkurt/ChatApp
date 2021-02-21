package com.example.vschatapp.Fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vschatapp.Activity.AnaActivity;
import com.example.vschatapp.Activity.ChatActivity;
import com.example.vschatapp.Models.Kullanicilar;
import com.example.vschatapp.R;
import com.example.vschatapp.Utils.ShowToastMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.getSystemServiceName;


public class OtherProfileFragment extends Fragment {


    View view;
    String otherId,userId;
    TextView userProfileNametext,userProfileEgitimtext,userProfileDogumtext,
            userProfileHakkimdatext,userProfileTakipText,userProfileArkadasText,userProfileNametext2;

    ImageView userProfileArkadasImage,userProfileMesajImage,userProfileTakipImage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, reference_Arkadaslik;
    CircleImageView userProfileProfileImage;
    FirebaseAuth auth;
    FirebaseUser user;
    String kontrol="",begeniKontrol="";
    ShowToastMessage showToastMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
       tanimla();
       action();
       getBegeniText();
       getArkadasText();
        return view;
    }

    public void tanimla() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://chat-e38ba-default-rtdb.firebaseio.com/");
        reference=firebaseDatabase.getReference();
        reference_Arkadaslik=firebaseDatabase.getReference().child("Arkadaslik_Istek");
         auth= FirebaseAuth.getInstance();
         user= auth.getCurrentUser();
         userId= user.getUid();

        otherId = getArguments().getString("userid");
        userProfileNametext =(TextView)view.findViewById(R.id.userProfileNametext);
        userProfileEgitimtext =(TextView)view.findViewById(R.id.userProfileEgitimtext);
        userProfileDogumtext =(TextView)view.findViewById(R.id.userProfileDogumtext);
        userProfileHakkimdatext =(TextView)view.findViewById(R.id.userProfileHakkimdatext);
        userProfileTakipText = (TextView)view.findViewById(R.id.userProfileTakipText);
        userProfileArkadasText = (TextView)view.findViewById(R.id.userProfileArkadasText);
        userProfileNametext2 = (TextView)view.findViewById(R.id.userProfileNametext2);

        userProfileArkadasImage = (ImageView)view.findViewById(R.id.userProfileArkadasImage);
        userProfileMesajImage = (ImageView)view.findViewById(R.id.userProfileMesajImage);
        userProfileTakipImage = (ImageView)view.findViewById(R.id.userProfileTakipImage);
        userProfileProfileImage =(CircleImageView)view.findViewById(R.id.userProfileProfileImage);

        reference_Arkadaslik.child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userId))
                {
                    kontrol = "istek";
                    userProfileArkadasImage.setImageResource(R.drawable.friend_remove2);
                }else
                {

                    userProfileArkadasImage.setImageResource(R.drawable.friend_add3);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        reference.child("Arkadaslar").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(otherId))
                {
                    kontrol = "arkadas";
                    userProfileArkadasImage.setImageResource(R.drawable.deleting_user);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

          reference.child("Begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if (dataSnapshot.hasChild(userId))
                  {
                      begeniKontrol = "begendi";
                      userProfileTakipImage.setImageResource(R.drawable.begen);

                  }else
                  {
                      userProfileTakipImage.setImageResource(R.drawable.begen_iptal);

                  }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });

          showToastMessage = new ShowToastMessage(getContext());

    }
    public  void action()
    {
      reference.child("Kullanicilar").child(otherId).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              Kullanicilar kl = dataSnapshot.getValue(Kullanicilar.class);
              userProfileNametext.setText("İsim: " +kl.getIsim());
              userProfileEgitimtext.setText("Eğitim: " +kl.getEgitim());
              userProfileDogumtext.setText("Doğum Tarihi: " +kl.getDogumtarih());
              userProfileHakkimdatext.setText("Hakkımda: " +kl.getHakkimda());
              userProfileNametext2.setText(kl.getIsim());
              if(!kl.getResim().equals("null")){
                  Picasso.get().load(kl.getResim()).into(userProfileProfileImage);
              }

          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });
        userProfileArkadasImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (kontrol.equals("istek"))
                 {
                     arkadasIptalEt(otherId,userId);
                 }else if (kontrol.equals("arkadas"))
                 {
                     arkadasTablosundanCikar(otherId,userId);
                 }
                 else
                 {
                     arkadaşEkle(otherId,userId);
                 }
            }
        });
        userProfileTakipImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (begeniKontrol.equals("begendi"))
                {
                    begeniIptal(userId,otherId);
                }else
                {
                    begen(userId,otherId);
                }
            }
        });

        userProfileMesajImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("userName",userProfileNametext2.getText().toString());
                intent.putExtra("id",otherId);
                startActivity(intent);

            }
        });
    }

    private void begeniIptal(String userId, String otherId) {
        reference.child("Begeniler").child(otherId).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userProfileTakipImage.setImageResource(R.drawable.begen_iptal);
                begeniKontrol ="";
                showToastMessage.showToast("Beğenme iptal edildi.");
                getBegeniText();
            }
        });
    }

    private void arkadasTablosundanCikar(final String otherId,final String userId) {
        reference.child("Arkadaslar").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child("Arkadaslar").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol="";
                        userProfileArkadasImage.setImageResource(R.drawable.friend_add3);
                        showToastMessage.showToast("Arkadaslıktan çıkarıldı");
                        getArkadasText();
                    }
                });
            }
        });
    }

    public void arkadaşEkle(final String otherId, final String userId)
    {
        reference_Arkadaslik.child(userId).child(otherId).child("tip").setValue("gonderdi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                  reference_Arkadaslik.child(otherId).child(userId).child("tip").setValue("aldi").addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful())
                          {
                              kontrol="istek";
                              showToastMessage.showToast("Arkadaşlık İsteği Başarıyla gönderildi.");
                              userProfileArkadasImage.setImageResource(R.drawable.friend_remove2);
                          }else
                          {
                              showToastMessage.showToast("Bir problem meydana geldi");

                          }
                      }
                  });
                }else
                {
                    showToastMessage.showToast("Bir problem meydana geldi");
                }
            }
        });
    }
    public void arkadasIptalEt(final String otherId, final String userId)
    {
        reference_Arkadaslik.child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference_Arkadaslik.child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol="";
                        userProfileArkadasImage.setImageResource(R.drawable.friend_add3);
                        showToastMessage.showToast("Arkadaşlık isteği iptal edildi");

                    }
                });
            }
        });
    }

    public  void begen(String userId, String otherId)
    {
          reference.child("Begeniler").child(otherId).child(userId).child("tip").setValue("begendi").addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                  if (task.isSuccessful())
                  {
                      showToastMessage.showToast("Profiili beğendiniz.");
                      userProfileTakipImage.setImageResource(R.drawable.begen);
                      begeniKontrol= "begendi";
                      getBegeniText();
                  }
              }
          });


    }
    public void getBegeniText()
    {
        //final List<String> begeniList = new ArrayList<>();
        //userProfileTakipText.setText( "0 Beğeni");
        reference.child("Begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfileTakipText.setText(dataSnapshot.getChildrenCount()+ "Beğeni");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getArkadasText()
    {
        //getChildrenCount icin addListenerForSingleValueEvent kullanılması önerilir.
       // final List<String> arkList = new ArrayList<>();
        //userProfileArkadasText.setText("0 Arkadaş");
        reference.child("Arkadaslar").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfileArkadasText.setText(dataSnapshot.getChildrenCount()+ "Arkadaş");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}