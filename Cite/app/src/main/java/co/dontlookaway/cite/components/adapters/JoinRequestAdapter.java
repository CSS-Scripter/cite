package co.dontlookaway.cite.components.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import co.dontlookaway.cite.R;
import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.api.GsonRequest;
import co.dontlookaway.cite.api.RequestCallback;
import co.dontlookaway.cite.api.joinrequest.JoinRequestManager;
import co.dontlookaway.cite.models.JoinRequest;
import co.dontlookaway.cite.util.Logger;

public class JoinRequestAdapter extends RecyclerView.Adapter<JoinRequestAdapter.JoinRequestHolder> {

    private final Context context;
    private final JoinRequest[] requests;
    private final Logger<JoinRequestAdapter> logger = new Logger<>(JoinRequestAdapter.class);

    public JoinRequestAdapter(Context context, JoinRequest[] requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public JoinRequestAdapter.JoinRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.row_join_request, parent, false);
        return new JoinRequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinRequestAdapter.JoinRequestHolder holder, int position) {
        holder.username.setText(this.requests[position].getUser().getUsername());
        holder.accept.setOnClickListener((View v) -> accept(this.requests[position]));
        holder.deny.setOnClickListener((View v) -> deny(this.requests[position]));
    }

    private void accept(JoinRequest request) {
        this.logger.info("Accepted request");
        RequestCallback<CustomResponse> callback = getCallback(this::showAcceptedToast);
        createProcessJoinRequestRequest(request, true, callback);
    }

    private void deny(JoinRequest request) {
        this.logger.info("Denied request");
        RequestCallback<CustomResponse> callback = getCallback(this::showDeniedToast);
        createProcessJoinRequestRequest(request, false, callback);
    }

    private void createProcessJoinRequestRequest(JoinRequest request, boolean accepted, RequestCallback<CustomResponse> callback) {
        GsonRequest<CustomResponse> processRequest = new JoinRequestManager().processJoinRequest(request, accepted, callback);
        RequestQueue queue = Volley.newRequestQueue(this.context);
        queue.add(processRequest);
    }

    private RequestCallback<CustomResponse> getCallback(Runnable runnable) {
        return new RequestCallback<CustomResponse>() {
            @Override
            public void onResponse(CustomResponse response) {
                runnable.run();
                ((Activity)context).finish();
            }

            @Override
            public void onError(int statusCode, ErrorResponse errorResponse) {
                logger.error(new Gson().toJson(errorResponse.getErrors()));
            }
        };
    }

    private void showAcceptedToast() {
        Toast.makeText(this.context, "Accepted request", Toast.LENGTH_LONG).show();
    }

    private void showDeniedToast() {
        Toast.makeText(this.context, "Denied request", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return this.requests.length;
    }

    public class JoinRequestHolder extends RecyclerView.ViewHolder {

        TextView username;
        AppCompatImageButton accept;
        AppCompatImageButton deny;

        public JoinRequestHolder(@NonNull View itemView) {
            super(itemView);
            this.username = itemView.findViewById(R.id.joinRequestUsername);
            this.accept = itemView.findViewById(R.id.joinRequestAccept);
            this.deny = itemView.findViewById(R.id.joinRequestDeny);
        }
    }
}
