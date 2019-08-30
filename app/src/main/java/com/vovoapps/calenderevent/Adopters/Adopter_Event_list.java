package com.vovoapps.calenderevent.Adopters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vovoapps.calenderevent.Database_Handler;
import com.vovoapps.calenderevent.Interfaces.Edit_Clicked_Listiner;
import com.vovoapps.calenderevent.Interfaces.Event_Listtiner;
import com.vovoapps.calenderevent.R;
import com.vovoapps.calenderevent.models.Event;
import com.vovoapps.calenderevent.models.User;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;


public class Adopter_Event_list extends RecyclerView.Adapter<Adopter_Event_list.User_List_ViewHolder> {

    ArrayList<Event> events=new ArrayList<>();
    private Context context;
    private Database_Handler databaseHandler;
    private Event_Listtiner clicked_listiner;
    private View.OnClickListener clickListener;

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public Adopter_Event_list(ArrayList<Event> events, Context context, Database_Handler databaseHandler) {
        this.events = events;
        this.context = context;
        this.databaseHandler = databaseHandler;
    }

    public void setClicked_listiner(Event_Listtiner clicked_listiner) {
        this.clicked_listiner = clicked_listiner;
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
                if(context==null){
                    Log.e("TAGE", "context is null");
                }
                view= LayoutInflater.from(context).inflate(R.layout.event_list_item,parent,false);
            }
        return new User_List_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final User_List_ViewHolder holder, final int position) {
           final Event event=events.get(position);
            holder.textView.setText(event.getTitle() +"    Hours:"+event.getHour()+"     Pay:"+event.getMax_event_price());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                toast("open detail activity");
                clicked_listiner.clicked(event,position);
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
                        holder.imageView.setBackgroundColor(event.getEvent_color());

                        if(clickListener!=null)
                    holder.img_more.setOnClickListener(clickListener);


//                        holder.img_more.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(final View view) {
//
////                                holder.editText.setVisibility(View.VISIBLE);
////                                holder.img_more.setVisibility(View.INVISIBLE);
////                                holder.editText.animate()
////                                        .translationX(-50)
////                                        .alpha(1.0f)
////                                        .setDuration(100);
//////                                holder.img_delete.animate()
//////                                        .translationX(-50)
//////                                        .alpha(1.0f)
//////                                        .setDuration(100).setListener(new AnimatorListenerAdapter() {
//////                                    @Override
//////                                    public void onAnimationEnd(Animator animation) {
//////                                        super.onAnimationEnd(animation);
//////                                        view.setVisibility(View.GONE);
//////
//////                                    }
//////                                });
//
//                            }
//                        });



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
                                            clicked_listiner.delete(event,position);
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
                                        clicked_listiner.edit(event,position);
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
        return events.size();
    }

    public class User_List_ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        ImageView img_more,img_delete,img_edit;
        CardView cardView;
        EditText editText;

        public User_List_ViewHolder(@NonNull View view) {
            super(view);
            imageView=(ImageView) view.findViewById(R.id.list_item_img);
            textView=(TextView)view.findViewById(R.id.list_item_title);
            img_more=(ImageView) view.findViewById(R.id.list_item_more_img);
            img_delete=(ImageView) view.findViewById(R.id.list_item_delete_img);
            img_edit=(ImageView) view.findViewById(R.id.list_item_edt_img);
            cardView=(CardView) view.findViewById(R.id.card_view);
            editText=(EditText) view.findViewById(R.id.edt_repeat_days);

        }
    }
}
