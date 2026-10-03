package co.dontlookaway.cite.components.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import co.dontlookaway.cite.QuotesActivity;
import co.dontlookaway.cite.api.auth.SessionManager;
import co.dontlookaway.cite.api.group.GroupManager;
import co.dontlookaway.cite.models.Group;
import co.dontlookaway.cite.R;
import co.dontlookaway.cite.models.Quote;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder>  {

    private final Context context;
    private final Group[] groups;

    public GroupAdapter(Context context, co.dontlookaway.cite.models.Group[] groups) {
        this.context = context;
        this.groups = groups;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.group_row, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groups[position];
        holder.groupTitle.setText(group.getName());
        holder.cardView.setOnClickListener((View v) -> onClick(group));
        holder.cardView.findViewById(R.id.favoriteGroup).setOnClickListener((View v) -> onFavorite(group));
        AppCompatImageButton ib = holder.cardView.findViewById(R.id.favoriteGroup);
        groupyMan(ib, group);
    }


    private void groupyMan(AppCompatImageButton ib, Group group) {
        boolean fav = GroupManager.getInstance().isGroupInFavorites(group);

        if(fav) {
            ib.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_on));
        } else {
            ib.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_off));
        }
    }


    private void onClick(Group group) {
        Intent intent = new Intent(context, QuotesActivity.class);
        String groupKey = context.getString(R.string.intent_group_key);
        String groupValue = new Gson().toJson(group);
        intent.putExtra(groupKey, groupValue);
        context.startActivity(intent);
    }

    private void onFavorite(Group group) {
        GroupManager groupManager = GroupManager.getInstance();
        if(groupManager.isGroupInFavorites(group)) {
            groupManager.unfavoriteGroup(context, group);
            return;
        }
        groupManager.favoriteGroup(context, group);
    }

    @Override
    public int getItemCount() {
        return groups.length;
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView groupTitle;
        CardView cardView;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            this.groupTitle = itemView.findViewById(R.id.groupTitle);
            this.cardView = itemView.findViewById(R.id.groupCardView);
        }
    }
}
