package co.dontlookaway.cite.components.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import co.dontlookaway.cite.R;
import co.dontlookaway.cite.models.Error;

public class ErrorAdapter extends RecyclerView.Adapter<ErrorAdapter.ErrorViewHolder> {

    private final Context context;
    private final Error[] errors;

    public ErrorAdapter(Context context, Error[] errors) {
        this.context = context;
        this.errors = errors;
    }

    /**
     * Runs when the viewholder gets created
     * @param parent parent ViewGroup
     * @param viewType type of view
     * @return ErrorViewHolder
     */
    @NonNull
    @Override
    public ErrorAdapter.ErrorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.error_row, parent, false);
        return new ErrorViewHolder(view);
    }

    /**
     * Sets text of holder with error message at possition
     * @param holder ErrorViewHolder
     * @param position position
     */
    @Override
    public void onBindViewHolder(@NonNull ErrorAdapter.ErrorViewHolder holder, int position) {
        holder.message.setText(this.errors[position].getMessage());
    }

    @Override
    public int getItemCount() {
        return this.errors.length;
    }

    public class ErrorViewHolder extends RecyclerView.ViewHolder {
        TextView message;

        public ErrorViewHolder(@NonNull View itemView) {
            super(itemView);
            this.message = itemView.findViewById(R.id.errorMessage);
        }
    }
}
