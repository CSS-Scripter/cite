package co.dontlookaway.cite.components.menus;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import co.dontlookaway.cite.GroupJoinRequestsActivity;
import co.dontlookaway.cite.QuotesActivity;
import co.dontlookaway.cite.R;
import co.dontlookaway.cite.api.auth.SessionManager;
import co.dontlookaway.cite.api.group.GroupManager;
import co.dontlookaway.cite.models.Group;
import co.dontlookaway.cite.models.User;
import co.dontlookaway.cite.util.Config;
import co.dontlookaway.cite.util.Logger;

public class GroupOptionsMenu extends PopupMenu {

    private final Logger<GroupOptionsMenu> logger = new Logger<>(GroupOptionsMenu.class);
    private final Context context;
    private final Group group;
    private Map<Integer, MenuAction> actionsMap;

    public GroupOptionsMenu(Context context, View anchor, Group group) {
        super(context, anchor);
        this.context = context;
        this.group = group;
        setup();
    }

    private void setup() {
        this.inflate(R.menu.menu_group_options);
        this.setOnMenuItemClickListener(this::onMenuItemClick);
        setupActions();
        setupAdditionalMenuItems();
    }

    private void setupActions() {
        this.actionsMap = new HashMap<>();
        this.actionsMap.put(R.id.group_menu_view_requests, this::navigateToJoinRequests);
        this.actionsMap.put(R.id.group_menu_refresh, this::refreshQuotes);
        this.actionsMap.put(R.id.group_menu_copy_invite_code, this::copyGroupIdToClipboard);
        this.actionsMap.put(R.id.group_menu_leave_group, this::leaveGroup);
    }

    private void setupAdditionalMenuItems() {
        if (isUserAdminOfGroup()) {
            getMenu().add(0, R.id.group_menu_view_requests, 0, R.string.group_menu_element_view_requests);
        }
        getMenu().add(0, R.id.group_menu_refresh, 0, context.getString(R.string.group_menu_element_refresh));
        getMenu().add(0, R.id.group_menu_copy_invite_code, 0, context.getString(R.string.group_menu_element_copy_invite_code));
        getMenu().add(0, R.id.group_menu_leave_group, 0, context.getString(R.string.group_menu_element_leave_group));
    }

    private boolean isUserAdminOfGroup() {
        SessionManager sessionManager = SessionManager.getInstance();
        String currentUserId = sessionManager.getUser().getId();
        for (User admin : this.group.getAdmins()) {
            if (admin.getId().equals(currentUserId)) {
                return true;
            }
        }
        return false;
    }

    public boolean onMenuItemClick(MenuItem item) {
        MenuAction action = this.actionsMap.get(item.getItemId());
        if (action == null) {
            logger.warn("Action not found");
            return false;
        }
        action.execute();
        return true;
    }

    private void navigateToJoinRequests() {
        Intent intent = new Intent(this.context, GroupJoinRequestsActivity.class);
        intent.putExtra(Config.INTENT_GROUP_KEY, new Gson().toJson(this.group));
        context.startActivity(intent);
    }

    private void copySuccessToastMesssageHQ4k() {
        Toast toast = Toast.makeText(this.context, "Copied invite code to clipboard", Toast.LENGTH_LONG);
        toast.show();
    }

    private void copyGroupIdToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Invite Code", this.group.getId());
        clipboard.setPrimaryClip(clip);
        copySuccessToastMesssageHQ4k();
    }

    private void leaveGroup() {
        GroupManager.getInstance().leaveGroup(context, group);
    }

    private void refreshQuotes() {
        Intent i = new Intent(context, QuotesActivity.class);
        String groupKey = context.getString(R.string.intent_group_key);
        String groupValue = new Gson().toJson(group);
        i.putExtra(groupKey, groupValue);
        context.startActivity(i);
        ((Activity)context).finish();


    }
}


