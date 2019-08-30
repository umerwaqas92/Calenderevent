package com.vovoapps.calenderevent.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vovoapps.calenderevent.Adopters.Adopter_UserList;
import com.vovoapps.calenderevent.Database_Handler;
import com.vovoapps.calenderevent.Fragments.Add_new_order;
import com.vovoapps.calenderevent.Interfaces.Edit_Clicked_Listiner;
import com.vovoapps.calenderevent.R;
import com.vovoapps.calenderevent.Services.Service_AI;
import com.vovoapps.calenderevent.Utills.MyActivity;
import com.vovoapps.calenderevent.landscape_calender.Landscap_Calender;
import com.vovoapps.calenderevent.models.User;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class Users_List extends MyActivity {


    ArrayList<User> users=new ArrayList<>();
    Database_Handler db;
    private Context context;
    private BottomSheetBehavior add_new_order_bottom;
    private BottomSheetBehavior delete_bottom_behaviour;
    private LinearLayout bottom_sheet;
    private EditText edt_name,edt_email;

    private  ImageButton imageButton;
    private int RESULT_LOAD_IMAGE=7;
    private Adopter_UserList adopterUserList;
    Bitmap bitmap_user;
     Button done_btn;
     EditText usr_edt_srch;
     String query="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users__list);
        context=this;
        startService(new Intent(context, Service_AI.class));
//        bottom_sheet_parent=(CoordinatorLayout) findViewById(R.id.bottome_ad_newUser_parent);

        bottom_sheet=(LinearLayout) findViewById(R.id.bottome_ad_newUser);
        usr_edt_srch=(EditText) findViewById(R.id.edt_search_txt);

        add_new_order_bottom=BottomSheetBehavior.from(bottom_sheet);
        delete_bottom_behaviour=BottomSheetBehavior.from((ConstraintLayout)findViewById(R.id.bottome_delete_user));

        delete_bottom_behaviour.setHideable(true);
        delete_bottom_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
        add_new_order_bottom.setHideable(true);
        add_new_order_bottom.setState(BottomSheetBehavior.STATE_HIDDEN);

        imageButton=bottom_sheet.findViewById(R.id.user_profile_img);
        edt_name=bottom_sheet.findViewById(R.id.user_profile_name);
        edt_email=bottom_sheet.findViewById(R.id.user_profile_email);
         done_btn =bottom_sheet.findViewById(R.id.ad_new_user_done);
        adopterUserList=new Adopter_UserList(users,context,db);
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.user_list_reclrView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));

        recyclerView.setLayoutManager(linearLayoutManager);

        ///
        startActivity(new Intent(this, Landscap_Calender.class));
        recyclerView.setAdapter(adopterUserList);

        ((ImageButton)findViewById(R.id.imageButton_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.INVISIBLE);
                usr_edt_srch.setVisibility(View.VISIBLE);
                usr_edt_srch.requestFocus();
                InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);;

                KeyboardVisibilityEvent.setEventListener(Users_List.this, new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(!isOpen){
                            ((ImageButton)findViewById(R.id.imageButton_search)).setVisibility(View.VISIBLE);
                            usr_edt_srch.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });

        adopterUserList.setClicked_listiner(new Edit_Clicked_Listiner() {
            @Override
            public void edit(final User user, int pos) {
                toast("edit user"+user.getUsr_name());
                add_new_order_bottom.setState(BottomSheetBehavior.STATE_EXPANDED);
                bitmap_user=db.getuser_pic(user);
                imageButton.setImageBitmap(bitmap_user);
                edt_email.setText(user.getUsr_email());
                edt_name.setText(user.getUsr_name());
                done_btn.setText("Update user info");
                delete_bottom_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);






                done_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setUsr_email(edt_email.getText().toString());
                        user.setUsr_name(edt_name.getText().toString());

                        if(db.update_user(user,bitmap_user)){
                            add_new_order_bottom.setState(BottomSheetBehavior.STATE_HIDDEN);
                            database_laod();
                            snake_bar_make("User is updated");

                        }else {
                            snake_bar_make("try again");

//                            toast("somthign went wrong");
                        }

                    }
                });

            }

            @Override
            public void delete(final User user, int pos) {
                        add_new_order_bottom.setState(BottomSheetBehavior.STATE_HIDDEN);
                        delete_bottom_behaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                ((Button)findViewById(R.id.delete_yes)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ///delete the user
//                        toast("deleted");
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if(db.delete_user(user)){
                                    delete_bottom_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                                    database_laod();
                                    snake_bar_make("User is deleted");

                                }
                            }
                        });
                    }
                });

                ((Button)findViewById(R.id.delete_no)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ///delete the user
                        delete_bottom_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);


//                        toast("deleted");
                    }
                });

            }
        });



        usr_edt_srch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){
                    query=charSequence.toString();
                    ArrayList<User> filter_usr=get_searched_usr(query,users);
                    users.clear();
                    users.addAll(filter_usr);
                    adopterUserList.notifyDataSetChanged();

                }else {
                    database_laod();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                // Sets the type as image/*. This ensures only components of type image are selected
                intent.setType("image/*");
                //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
//                // Launching the Intent
//                intent.putExtra("crop", "true");
//                intent.putExtra("aspectX", 0);
//                intent.putExtra("aspectY", 0);
//                intent.putExtra("return-data", true);

                startActivityForResult(intent,RESULT_LOAD_IMAGE);
            }
        });





        new Handler().post(new Runnable() {
            @Override
            public void run() {
                db=new Database_Handler(context);


                database_laod();

            }
        });





    }


    public ArrayList<User> get_searched_usr(String q,ArrayList<User> users){
        ArrayList<User> filter_user=new ArrayList<>();
        for (User user:users) {
            if(user.getUsr_name().toLowerCase().contains(q.toLowerCase())){
                filter_user.add(user);
            }
        }
        return filter_user;
    }

    public void show_bottom_clicked(View view){
//        view.setVisibility(View.GONE);
        add_new_order_bottom.setState(BottomSheetBehavior.STATE_EXPANDED);
        add_new_order_bottom.setSkipCollapsed(true);

        done_btn.setText("Add new user");
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_name.getText().toString().isEmpty()){
                    edt_name.setError("Enter user name");
                    return;
                }

                if(edt_email.getText().toString().isEmpty()){
                    edt_email.setError("Enter user email");
                    return;
                }


                String name=edt_name.getText().toString();
                String email=edt_email.getText().toString();
                String pic="ads.jpg";
//                if(db!=null){
                if(bitmap_user!=null)
                    if(db.put_user(new User(name,email,pic,0),bitmap_user)){
                        add_new_order_bottom.setState(BottomSheetBehavior.STATE_HIDDEN);
                        snake_bar_make("User is added");
                        database_laod();


//                        add_new_order_bottom.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }


//                }


//                toast("load image");
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                Picasso.get().load(resultUri).into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                      new Handler().post(new Runnable() {
                          @Override
                          public void run() {
                              bitmap_user=transform(bitmap);

                              imageButton.setImageBitmap(bitmap_user);
                              toast("data is updated");
                          }
                      });


                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null ) {
//            Picasso.get().load(data.getData()).noPlaceholder().centerCrop().fit()
//                    .into(imageButton);


            Uri selectedImage = data.getData();
            CropImage.activity(selectedImage)
                    .setAllowRotation(false)
                    .setAspectRatio(1,1)
                    .setRequestedSize(100,100)
                    .start(Users_List.this);


//


        }else {
            toast("data is null");
        }
    }

    public void update_vies(){

    }


    @Override
    public void onBackPressed() {
        if(delete_bottom_behaviour.getState()==BottomSheetBehavior.STATE_EXPANDED){
            delete_bottom_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
        }else if(add_new_order_bottom.getState()==BottomSheetBehavior.STATE_EXPANDED){
            add_new_order_bottom.setState(BottomSheetBehavior.STATE_HIDDEN);
        }else {
            super.onBackPressed();
        }

    }

    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    public void database_laod(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                   while (db==null);
                try {
                    users.clear();
                    adopterUserList.notifyDataSetChanged();
                    users.addAll(db.getAllUsers());
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                   toast("all users"+users.size());
            }
        });
    }
}
