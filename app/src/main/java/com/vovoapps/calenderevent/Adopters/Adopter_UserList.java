package com.vovoapps.calenderevent.Adopters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vovoapps.calenderevent.Database_Handler;
import com.vovoapps.calenderevent.Interfaces.Edit_Clicked_Listiner;
import com.vovoapps.calenderevent.MainActivity;
import com.vovoapps.calenderevent.R;
import com.vovoapps.calenderevent.models.User;

import java.util.ArrayList;


public class Adopter_UserList extends RecyclerView.Adapter<Adopter_UserList.User_List_ViewHolder> {



    ArrayList<User> users=new ArrayList<>();
    private Context context;
    private Database_Handler databaseHandler;
    private Edit_Clicked_Listiner clicked_listiner;
//    ArrayList<event_name_count> event_counts=new ArrayList<>();




    public void setClicked_listiner(Edit_Clicked_Listiner clicked_listiner) {
        this.clicked_listiner = clicked_listiner;
    }

    public Adopter_UserList(ArrayList<User> users, Context context, Database_Handler databaseHandler) {
        this.users = users;
        this.context = context;
        this.databaseHandler = databaseHandler;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public User_List_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=null;
            if(view==null){
                view= LayoutInflater.from(context).inflate(R.layout.user_list_item,parent,false);
            }
        return new User_List_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final User_List_ViewHolder holder, final int position) {
           final User user=users.get(position);
            holder.textView.setText(user.getUsr_name());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                toast("open detail activity");
                Intent intent=new Intent(context, MainActivity.class);
                intent.putExtra("user_id",user.getUsr_id());
                intent.putExtra("user_name",user.getUsr_name());
                intent.putExtra("user_email",user.getUsr_email());
                context.startActivity(intent);



            }
        });

            new Handler().post(new Runnable() {
                @Override
                public void run() {
//                    try {

                        if(databaseHandler==null){
                            toast("db is null ");
                            databaseHandler=new Database_Handler(context);
//                            return;
                        }
                        holder.imageView.setImageBitmap(databaseHandler.getuser_pic(user));
                        holder.img_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {

                                holder.img_edit.setVisibility(View.VISIBLE);
                                holder.img_delete.setVisibility(View.VISIBLE);
                                holder.img_edit.animate()
                                        .translationX(-50)
                                        .alpha(1.0f)
                                        .setDuration(100);
                                holder.img_delete.animate()
                                        .translationX(-50)
                                        .alpha(1.0f)
                                        .setDuration(100).setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        view.setVisibility(View.GONE);

                                    }
                                });

                            }
                        });

                        holder.img_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                holder.img_edit.animate()
                                        .translationX(50)
                                        .alpha(0.0f)
                                        .setDuration(100);
                                holder.img_delete.animate()
                                        .translationX(50)
                                        .alpha(0.0f)
                                        .setDuration(100).setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        holder.img_edit.setVisibility(View.GONE);
                                holder.img_delete.setVisibility(View.GONE);
                                        holder.img_more.setVisibility(View.VISIBLE);
                                        if(clicked_listiner!=null){
                                            clicked_listiner.delete(user,position);
                                        }

                                    }
                                });
//
                            }
                        });

                    holder.img_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.img_edit.animate()
                                    .translationX(50)
                                    .alpha(0.0f)
                                    .setDuration(100);
                            holder.img_delete.animate()
                                    .translationX(50)
                                    .alpha(0.0f)
                                    .setDuration(100).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    holder.img_edit.setVisibility(View.GONE);
                                    holder.img_delete.setVisibility(View.GONE);
                                    holder.img_more.setVisibility(View.VISIBLE);
                                    if(clicked_listiner!=null){
                                        clicked_listiner.edit(user,position);
                                    }

                                }
                            });
//
                        }
                    });



//                    } catch (Exception e) {
//                        e.printStackTrace();
////                        toast(e.toString());
//
//                    }
                }
            });

            ///fill the image view
    }

    public void toast(String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class User_List_ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        ImageView img_more,img_delete,img_edit;
        CardView cardView;

        public User_List_ViewHolder(@NonNull View view) {
            super(view);
            imageView=(ImageView) view.findViewById(R.id.list_item_img);
            textView=(TextView)view.findViewById(R.id.list_item_title);
            img_more=(ImageView) view.findViewById(R.id.list_item_more_img);
            img_delete=(ImageView) view.findViewById(R.id.list_item_delete_img);
            img_edit=(ImageView) view.findViewById(R.id.list_item_edt_img);
            cardView=(CardView) view.findViewById(R.id.card_view);

        }
    }
}
