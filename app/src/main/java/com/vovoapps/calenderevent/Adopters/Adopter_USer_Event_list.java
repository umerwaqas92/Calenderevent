package com.vovoapps.calenderevent.Adopters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vovoapps.calenderevent.Database_Handler;
import com.vovoapps.calenderevent.Interfaces.Event_Listtiner;
import com.vovoapps.calenderevent.R;
import com.vovoapps.calenderevent.models.Event;
import com.vovoapps.calenderevent.models.User_Event2;

import java.util.ArrayList;


public class Adopter_USer_Event_list extends RecyclerView.Adapter<Adopter_USer_Event_list.User_List_ViewHolder> {

    ArrayList<User_Event2> events=new ArrayList<>();
    ArrayList<User_Event2> events_done=new ArrayList<>();
    private Context context;
    private Database_Handler databaseHandler;
    private Event_Listtiner clicked_listiner;




    public Adopter_USer_Event_list(ArrayList<User_Event2> events, Context context, Database_Handler databaseHandler) {
        this.events = events;
        this.context = context;
        this.databaseHandler = databaseHandler;

    }

    public void setClicked_listiner(Event_Listtiner clicked_listiner) {
        this.clicked_listiner = clicked_listiner;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public User_List_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=null;
            if(view==null){
                if(context==null){
                    Log.e("TAGE", "context is null");
                }
                view= LayoutInflater.from(context).inflate(R.layout.user_event_list_item,parent,false);
            }

        return new User_List_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final User_List_ViewHolder holder, final int position) {

           final User_Event2 event=events.get(position);

            holder.textView.setText(event.getTitle());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("open detail activity");
//                clicked_listiner.clicked(event,position);
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
                        holder.imageView.setBackgroundColor(event.getColor());
                        holder.textView.setText(event.getTitle());
                        int total_hour=event.getCount()*event.getHours();
                        holder.textView2.setText(event.getCount()+"     hours"+total_hour+"/43"+"     Payment"+((event.getMax_pay()/43)*total_hour)+"$");






//



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
        TextView textView2;
        CardView cardView;

        public User_List_ViewHolder(@NonNull View view) {
            super(view);
            imageView=(ImageView) view.findViewById(R.id.list_item_img);
            textView=(TextView)view.findViewById(R.id.list_item_title);
            textView2=(TextView)view.findViewById(R.id.list_item_title_cound);
            cardView=(CardView) view.findViewById(R.id.card_view);

        }
    }
}
