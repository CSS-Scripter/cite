package co.dontlookaway.cite.fragments;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import co.dontlookaway.cite.api.group.GroupManager;

public class CustomFragment extends Fragment {
    protected Activity activity;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
