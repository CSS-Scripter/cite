package co.dontlookaway.cite.api.group;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.api.GsonRequest;
import co.dontlookaway.cite.api.RequestCallback;
import co.dontlookaway.cite.api.auth.SessionManager;
import co.dontlookaway.cite.api.group.favorite.FavoriteGroupRequest;
import co.dontlookaway.cite.api.group.favorite.FavoriteGroupResponse;
import co.dontlookaway.cite.api.group.favorite.UnfavoriteGroupRequest;
import co.dontlookaway.cite.api.group.favorite.UnfavoriteGroupResponse;
import co.dontlookaway.cite.models.Group;
import co.dontlookaway.cite.util.ErrorHelper;
import co.dontlookaway.cite.util.Logger;

public class GroupManager {

    private static GroupManager instance;

    private final Logger<GroupManager> logger = new Logger<>(GroupManager.class);
    private final Set<GroupsObserver> subscribers;

    private boolean notifying = false;
    private final List<GroupsObserver> removeQueue = new ArrayList<>();

    private Group[] groups;

    private GroupManager() {
        subscribers = new HashSet<>();
    }

    public static GroupManager getInstance() {
        if (instance == null) {
            instance = new GroupManager();
        }
        return instance;
    }

    public void subscribe(GroupsObserver observer) {
        this.subscribers.add(observer);
        this.logger.info(observer.getClass().getName() + " has subscribed to the Group Manager");
    }

    public void unsubscribe(GroupsObserver observer) {
        if(notifying) {
            this.removeQueue.add(observer);
            return;
        }

        this.subscribers.remove(observer);
        this.logger.info(observer.getClass().getName() + " has unsubscribed from the Group Manager");
    }

    public void updateGroups(Context context) {
        RequestCallback<GetAllGroupsResponse> callback = createGetAllGroupsCallback();
        GsonRequest<GetAllGroupsResponse> request = getAllGroupsRequest(callback);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private RequestCallback<GetAllGroupsResponse> createGetAllGroupsCallback() {
        return new RequestCallback<GetAllGroupsResponse>() {
            @Override
            public void onResponse(GetAllGroupsResponse response) {
                groups = response.getGroups();
                notifySubscribers();
            }

            @Override
            public void onError(int statusCode, ErrorResponse errorResponse) {
                notifySubscribersOfError(errorResponse);
            }
        };
    }

    private GsonRequest<GetAllGroupsResponse> getAllGroupsRequest(RequestCallback<GetAllGroupsResponse> callback) {
        return new GsonRequest<>(
                new GetAllGroupsRequest(),
                callback::onResponse,
                error -> new ErrorHelper<GetAllGroupsResponse>().handleVolleyError(error, callback));
    }

    private void notifySubscribers() {
        notifying = true;
        for (GroupsObserver observer: subscribers) {
            observer.onGroupsUpdated(groups);
        }
        notifying = false;
        for (GroupsObserver observer: removeQueue) {
            subscribers.remove(observer);
        }
        removeQueue.clear();
    }

    private void notifySubscribersOfError(ErrorResponse errorResponse) {
        notifying = true;
        for (GroupsObserver observer : subscribers) {
            observer.onError(errorResponse);
        }
        notifying = false;
        for (GroupsObserver observer: removeQueue) {
            subscribers.remove(observer);
        }
        removeQueue.clear();
    }

    public void createGroup(Context context, String groupName) {
        RequestCallback<CreateGroupResponse> callback = createCreateGroupCallback();
        GsonRequest<CreateGroupResponse> request = createGroupRequest(groupName, callback);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public RequestCallback<CreateGroupResponse> createCreateGroupCallback() {
        return new RequestCallback<CreateGroupResponse>() {
            @Override
            public void onResponse(CreateGroupResponse response) {
                Group group = response.getGroup();
                addGroup(group);
                notifySubscribers();
            }

            @Override
            public void onError(int statusCode, ErrorResponse errorResponse) {
                notifySubscribersOfError(errorResponse);
            }
        };
    }

    private void addGroup(Group group) {
        Group[] newGroupsArray = new Group[this.groups.length + 1];
        System.arraycopy(this.groups, 0, newGroupsArray, 0, this.groups.length);
        newGroupsArray[this.groups.length] = group;
        this.groups = newGroupsArray;
    }

    private GsonRequest<CreateGroupResponse> createGroupRequest(String name, RequestCallback<CreateGroupResponse> callback) {
        CreateGroupRequest createGroupRequest = new CreateGroupRequest(name);
        return new GsonRequest<>(
                createGroupRequest,
                callback::onResponse,
                error -> new ErrorHelper<CreateGroupResponse>().handleVolleyError(error, callback));
    }

    public GsonRequest<JoinGroupResponse> joinGroupRequest(String groupCode, RequestCallback<JoinGroupResponse> callback) {
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest(groupCode);
        return new GsonRequest<>(
                joinGroupRequest,
                callback::onResponse,
                error -> new ErrorHelper<JoinGroupResponse>().handleVolleyError(error, callback));
    }

    public Group[] getGroups() {
        return this.groups;
    }

    public Group[] getFavoriteGroups() {
        String[] favoriteGroupIds = SessionManager.getInstance().getUser().getFavoriteGroups();
        List<Group> favorites = new ArrayList<>();
        for (Group group : groups) {
            if (isIdInFavorites(favoriteGroupIds, group.getId())) {
                favorites.add(group);
            }
        }
        return favorites.toArray(new Group[0]);
    }

    public boolean isGroupInFavorites(Group group) {
        String[] favoriteGroupIds = SessionManager.getInstance().getUser().getFavoriteGroups();
        return isIdInFavorites(favoriteGroupIds, group.getId());
    }

    private boolean isIdInFavorites(String[] favoriteIds, String id) {
        for (String favoriteId : favoriteIds) {
            if (favoriteId.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void favoriteGroup(Context context, Group group) {
        GsonRequest<FavoriteGroupResponse> request = createFavoriteGroupResponseRequest(group, context);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private GsonRequest<FavoriteGroupResponse> createFavoriteGroupResponseRequest(Group group, Context context) {
        FavoriteGroupRequest request = new FavoriteGroupRequest(group.getId());
        RequestCallback<FavoriteGroupResponse> callback = favoriteGroupResponseRequestCallback(group, context);
        return new GsonRequest<>(
                request,
                callback::onResponse,
                error -> new ErrorHelper<FavoriteGroupResponse>().handleVolleyError(error, callback));
    }

    private RequestCallback<FavoriteGroupResponse> favoriteGroupResponseRequestCallback(Group group, Context context) {
        return new RequestCallback<FavoriteGroupResponse>() {
            @Override
            public void onResponse(FavoriteGroupResponse response) {
                SessionManager.getInstance().getUser().setFavoriteGroups(response.getFavoriteGroups());
                notifySubscribers();
                Toast.makeText(context, String.format("Favorited %s", group.getName()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int statusCode, ErrorResponse errorResponse) {
                logger.error("Error!");
                Toast.makeText(context, "Something went wrong, looks like you will never truely show your appreciation for this group...", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void unfavoriteGroup(Context context, Group group) {
        GsonRequest<UnfavoriteGroupResponse> request = createUnfavoriteGroupResponseRequest(group, context);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private GsonRequest<UnfavoriteGroupResponse> createUnfavoriteGroupResponseRequest(Group group, Context context) {
        UnfavoriteGroupRequest request = new UnfavoriteGroupRequest(group.getId());
        RequestCallback<UnfavoriteGroupResponse> callback = unfavoriteGroupResponseRequestCallback(group, context);
        return new GsonRequest<>(
                request,
                callback::onResponse,
                error -> new ErrorHelper<UnfavoriteGroupResponse>().handleVolleyError(error, callback));
    }

    private RequestCallback<UnfavoriteGroupResponse> unfavoriteGroupResponseRequestCallback(Group group, Context context) {
        return new RequestCallback<UnfavoriteGroupResponse>() {
            @Override
            public void onResponse(UnfavoriteGroupResponse response) {
                SessionManager.getInstance().getUser().setFavoriteGroups(response.getFavoriteGroups());
                notifySubscribers();
                Toast.makeText(context, String.format("Unfavorited %s", group.getName()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int statusCode, ErrorResponse errorResponse) {
                logger.error("Error!");
                Toast.makeText(context, "Something went wrong, looks like it's in your favorites for ever now.", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void leaveGroup(Context context, Group group) {
        RequestCallback<LeaveGroupResponse> callback = createLeaveGroupCallback(context);
        GsonRequest<LeaveGroupResponse> request = createLeaveGroupRequest(group, callback);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private RequestCallback<LeaveGroupResponse> createLeaveGroupCallback(Context context) {
        return new RequestCallback<LeaveGroupResponse>() {
            @Override
            public void onResponse(LeaveGroupResponse response) {
                updateGroups(context);
                ((Activity) context).finish();
            }

            @Override
            public void onError(int statusCode, ErrorResponse errorResponse) {
                Toast.makeText(context, "An error occured while trying to leave this group. If you are the owner, you cannot leave.", Toast.LENGTH_LONG).show();
            }
        };
    }

    private GsonRequest<LeaveGroupResponse> createLeaveGroupRequest(Group group, RequestCallback<LeaveGroupResponse> callback) {
        LeaveGroupRequest request = new LeaveGroupRequest(group.getId());
        return new GsonRequest<>(
                request,
                callback::onResponse,
                error -> new ErrorHelper<LeaveGroupResponse>().handleVolleyError(error, callback));
    }
}
