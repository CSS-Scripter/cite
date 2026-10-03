package co.dontlookaway.cite.components.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

import co.dontlookaway.cite.R;
import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.api.GsonRequest;
import co.dontlookaway.cite.api.RequestCallback;
import co.dontlookaway.cite.api.auth.SessionManager;
import co.dontlookaway.cite.api.quotes.DeleteQuoteResponse;
import co.dontlookaway.cite.api.quotes.EditQuoteResponse;
import co.dontlookaway.cite.api.quotes.LikeQuoteRequest;
import co.dontlookaway.cite.api.quotes.LikeQuoteResponse;
import co.dontlookaway.cite.api.quotes.QuoteManager;
import co.dontlookaway.cite.models.Quote;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.GroupViewHolder>  {

    private Context context = null;
    private Quote[] quotes;

    private String editQuoteId;
    private int editPosition;
    private EditText newQuoteText;
    private EditText newQuotee;

    private GsonRequest request;

    public QuoteAdapter(Context context, Quote[] quotes) {
        this.context = context;
        this.quotes = quotes;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.quote_row, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Quote quote = quotes[position];
        holder.quoteText.setText(quote.getQuote());
        holder.likeCount.setText(String.valueOf(quote.getLikes().length));
        holder.quoteeText.setText(quote.getQuotee());
        holder.cardView.setOnClickListener((View v) -> onClick(position));

        AppCompatImageButton ib = holder.cardView.findViewById(R.id.imageButton2);
        likeyMan(ib, quote);
        holder.cardView.findViewById(R.id.imageButton2).setOnClickListener((View v) -> likeOnClicknator2000(position));
    }

    private void likeyMan(AppCompatImageButton ib, Quote quote) {
        String[] likes = quote.getLikes();
        String userId = SessionManager.getInstance().getUser().getId();

        List<String> likesList = Arrays.asList(likes);

        if(likesList.contains(userId)) {
            ib.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.presence_online));
        } else {
            ib.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.presence_invisible));
        }
    }

    private void likeOnClicknator2000(int position) {
//        Log.d("aaaa", "likeOnClicknator2000: " + position);
        Quote quote = quotes[position];
        editPosition = position;
        like(quote);
    }

    private void onClick(int position)  {
        Quote quote = quotes[position];
        editPosition = position;

        String quoterId = quote.getQuoter().getId();
        SessionManager sessionManager = SessionManager.getInstance();
        String localUserId = sessionManager.getUser().getId();

        if(!quoterId.equals(localUserId)) {
            cannotEditToast();
            return;
        }
        editDialog(quote);

    }

    /**
     * Like
     */
    private void like(Quote quote) {
        editQuoteId = quote.getId();
        createLikeRequest();
        executeRequest();
    }

    private void editDialog(Quote quote) {
        AlertDialog.Builder dialog = createDialogWithTitle();

        LinearLayout layout = createLayout();

        editQuoteId = quote.getId();

        initDialogQuoteField(quote);
        initDialogQuoteeField(quote);

        layout.addView(newQuoteText);
        layout.addView(newQuotee);

        dialog.setView(layout);

        dialog = dialogButtons(dialog);

        AlertDialog finalDialog = dialog.create();

        finalDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                finalDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#FF0000"));
            }
        });

        finalDialog.show();
    }

    private void cannotEditToast() {
        Toast toast = Toast.makeText(context, "You cannot manage this quote", Toast.LENGTH_LONG);
        toast.show();
    }

    private AlertDialog.Builder createDialogWithTitle() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Edit your quote");
        return dialog;
    }

    private void initDialogQuoteField(Quote quote) {
        newQuoteText = new EditText(context);
        newQuoteText.setHint("Quote");
        newQuoteText.setText(quote.getQuote());
    }

    private void initDialogQuoteeField(Quote quote) {
        newQuotee = new EditText(context);
        newQuotee.setHint("Quotee");
        newQuotee.setText(quote.getQuotee());
    }

    private LinearLayout createLayout() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    private AlertDialog.Builder dialogButtons (AlertDialog.Builder dialog) {
        dialog.setPositiveButton("Save", (dialogInterface, which) -> {
            createEditRequest();
            executeRequest();
        });

        dialog.setNeutralButton("Delete", (dialogInterface, which) -> {
            createDeleteRequest();
            executeRequest();

        });

        dialog.setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.cancel());
        return dialog;
    }

    private void createLikeRequest() {
        RequestCallback<LikeQuoteResponse> callback = createLikeRequestCallback();
        this.request =  QuoteManager.likeQuoteRequest(editQuoteId, callback);
    }

    private void createEditRequest() {
        RequestCallback<EditQuoteResponse> callback = createEditRequestCallback();
        this.request =  QuoteManager.editQuoteRequest(editQuoteId, newQuoteText.getText().toString(), newQuotee.getText().toString(), callback);
    }

    private void createDeleteRequest() {
        RequestCallback<DeleteQuoteResponse> callback = deleteQuoteRequestCallback();
        this.request =  QuoteManager.deleteQuoteRequest(editQuoteId, callback);

    }

    private void executeRequest() {
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(this.request);
    }

    private RequestCallback<LikeQuoteResponse> createLikeRequestCallback() {
        return new RequestCallback<LikeQuoteResponse>() {
            @Override
            public void onResponse(LikeQuoteResponse response) {
                Quote quote = quotes[editPosition];
                quote.setLikes(response.getQuote().getLikes());
                quotes[editPosition] = quote;
                notifyDataSetChanged();
            }

            @Override
            public void onError(int statusCode, ErrorResponse errorResponse) {
                Log.d("err", String.valueOf(errorResponse.getErrors()));
                // TODO: add error handling
            }
        };
    }
    private RequestCallback<EditQuoteResponse> createEditRequestCallback() {
        return new RequestCallback<EditQuoteResponse>() {
            @Override
            public void onResponse(EditQuoteResponse response) {
                Quote quote = quotes[editPosition];
                quote.setQuote(response.getQuote().getQuote());
                quote.setQuotee(response.getQuote().getQuotee());
                quotes[editPosition] = quote;
                notifyDataSetChanged();
            }

            @Override
            public void onError(int statusCode, ErrorResponse errorResponse) {
                Log.d("err", String.valueOf(errorResponse.getErrors()));
                // TODO: add error handling
            }
        };
    }

    private RequestCallback<DeleteQuoteResponse> deleteQuoteRequestCallback() {
        return new RequestCallback<DeleteQuoteResponse>() {
            @Override
            public void onResponse(DeleteQuoteResponse response) {
                Quote[] newQuotes = ArrayUtils.remove(quotes, editPosition);
                quotes = newQuotes;
                notifyDataSetChanged();
            }

            @Override
            public void onError(int statusCode, ErrorResponse errorResponse) {
                Log.d("err", String.valueOf(errorResponse.getErrors()));
                // TODO: add error handling
            }
        };
    }

    @Override
    public int getItemCount() {
        return quotes.length;
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView quoteText;
        TextView quoteeText;
        CardView cardView;
        TextView likeCount;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            this.quoteText = itemView.findViewById(R.id.quoteText);
            this.quoteeText = itemView.findViewById(R.id.quotee);
            this.cardView = itemView.findViewById(R.id.quoteCardView);
            this.likeCount = itemView.findViewById(R.id.textView5);
        }
    }
}
