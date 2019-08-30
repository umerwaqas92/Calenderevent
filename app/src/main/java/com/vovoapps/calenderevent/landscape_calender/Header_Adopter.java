package com.vovoapps.calenderevent.landscape_calender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vovoapps.calenderevent.R;

import java.util.ArrayList;



public class Header_Adopter extends RecyclerView.Adapter<Header_Adopter.ViewHolder> {
    ArrayList<Header_Item> headerItems;

    Context context;

    public Header_Adopter(ArrayList<Header_Item> headerItems, Context context) {
        this.headerItems = headerItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.calender_header_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Header_Item item=headerItems.get(position);
  holder.txt_date.setText(item.date+"");
            holder.txt_day.setText(item.day_name);
    }

    @Override
    public int getItemCount() {
        return headerItems.size();
    }

    public Header_Adopter(ArrayList<Header_Item> headerItems) {
        this.headerItems = headerItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_date,txt_day;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_date=(TextView)itemView.findViewById(R.id.calender_header_cell_date);
            txt_day=(TextView)itemView.findViewById(R.id.calender_header_cell_day);
        }
    }


   public static class Header_Item {
        String day_name;
        int date;
        public Header_Item(String day_name, int date) {
            this.day_name = day_name;
            this.date = date;
        }

        public int getDate() {
            return date;
        }

        public String getDay_name() {
            return day_name;
        }
    }
}
