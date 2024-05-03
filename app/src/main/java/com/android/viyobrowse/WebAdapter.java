package com.android.viyobrowse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WebAdapter extends RecyclerView.Adapter<WebAdapter.ViewHolder> {

    private List<String> webUrl;
    private Context context;

    public WebAdapter(Context context, List<String>webUrl ) {

        this.context = context;
        this.webUrl = webUrl;

    }

    @NonNull
    @Override
    public WebAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.showlist,parent,false);
        return  new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WebAdapter.ViewHolder holder, int position) {

        holder.url.setText(webUrl.get(position));
    }

    @Override
    public int getItemCount() {
        return webUrl.size();
    }

    public void clearData() {
            webUrl.clear();
            notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView url;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            url = itemView.findViewById(R.id.historyTxt);
        }
    }
}
