package com.crewcloud.apps.crewboard.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.logintest.Statics;
import com.crewcloud.apps.crewboard.adapter.CommunityAdapter;
import com.crewcloud.apps.crewboard.base.BaseEvent;
import com.crewcloud.apps.crewboard.base.BaseFragment;
import com.crewcloud.apps.crewboard.dtos.ChildBoards;
import com.crewcloud.apps.crewboard.dtos.Community;
import com.crewcloud.apps.crewboard.dtos.LeftMenu;
import com.crewcloud.apps.crewboard.event.MenuEvent;
import com.crewcloud.apps.crewboard.module.community.CommunityPresenter;
import com.crewcloud.apps.crewboard.module.community.CommunityPresenterImp;
import com.crewcloud.apps.crewboard.util.Util;
import com.crewcloud.apps.crewboard.widget.MyRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dazone on 2/21/2017.
 */

public class CommunityFragment extends BaseFragment implements CommunityPresenter.view {

    @Bind(R.id.fragment_home_list_community)
    MyRecyclerView lvCommunity;

    @Bind(R.id.fragment_home_v2_rl_no_data)
    RelativeLayout rlNodata;

    CommunityAdapter communityAdapter;

    private CommunityPresenterImp communityPresenterImp;
    private boolean stopLoading;
    private int currentPage;
    private String textSearch = "";
    private ChildBoards leftMenu;
    private int boardNo;


    public static BaseFragment newInstance(Bundle bundle) {
        CommunityFragment fragment = new CommunityFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.comunity));
        setHasOptionsMenu(true);


        if (getArguments() != null) {
            String name = getArguments().getString("NAME_ALL");
            if (TextUtils.isEmpty(name)) {
                leftMenu = getArguments().getParcelable("OBJECT");
                if (leftMenu != null) {
                    boardNo = leftMenu.getBoardNo();
                }
            } else {
                boardNo = 0;
            }
        }
        communityAdapter = new CommunityAdapter(getBaseActivity());
        communityPresenterImp = new CommunityPresenterImp(getBaseActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_v2, container, false);
        ButterKnife.bind(this, view);
        setActionFloat(false);
        communityPresenterImp.attachView(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvCommunity.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lvCommunity.setAdapter(communityAdapter);

        if (communityAdapter.getItemCount() == 0) {
            currentPage = 1;
            stopLoading = true;
            if (boardNo > 0) {
                getBaseActivity().showProgressDialog();
                communityPresenterImp.getCommunityById(boardNo, currentPage);
            } else {
                getBaseActivity().showProgressDialog();
                communityPresenterImp.getCommunity(currentPage);
            }
        }


        lvCommunity.setMyRecyclerViewListener(new MyRecyclerView.MyRecyclerViewListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                if (boardNo > 0) {
                    communityPresenterImp.getCommunityById(boardNo, currentPage);
                } else {
                    communityPresenterImp.getCommunity(currentPage);
                }
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                currentPage = page + 1;
                if (!stopLoading) {
                    if (boardNo > 0) {
                        communityPresenterImp.getCommunityById(boardNo, currentPage);
                    } else {
                        communityPresenterImp.getCommunity(currentPage);
                    }
                }

            }
        });

        communityAdapter.setOnClickNewsListener(new CommunityAdapter.OnClickNewsListener() {
            @Override
            public void onItemClicked(int position) {
                BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.COMMUNITY_DETAIL);
                Bundle data = new Bundle();
                data.putInt(Statics.ID_COMMUNITY, communityAdapter.getItem(position).getContentNo());
                MenuEvent menuEvent = new MenuEvent();
                menuEvent.setBundle(data);
                baseEvent.setMenuEvent(menuEvent);
                EventBus.getDefault().post(baseEvent);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        if (null != searchView) {

            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getActivity().getComponentName()));

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            // Get the search close button image view
            ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);

            // Set on click listener
            closeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(), "LOG" + "Search close button clicked", Toast.LENGTH_LONG).show();
                    EditText et = (EditText) searchView.findViewById(R.id.search_src_text);
                    //Clear the text from EditText view
                    et.setText("");
                    searchView.setQuery("", false);
                    //Collapse the action view
                    searchView.onActionViewCollapsed();
                    setTitle(getString(R.string.comunity));
                    getBaseActivity().showProgressDialog();
                    rlNodata.setVisibility(View.GONE);
                    communityPresenterImp.getCommunity(currentPage);

                }
            });

            try {
                Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
                mCursorDrawableRes.setAccessible(true);
                mCursorDrawableRes.set(searchTextView, R.drawable.cursor);
            } catch (Exception ignored) {
            }

        }


        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setActionFloat(true);
                BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.LOCK);
                baseEvent.setLock(false);
                EventBus.getDefault().post(baseEvent);
                textSearch = query;
                currentPage = 1;
                getBaseActivity().showProgressDialog();
                communityPresenterImp.searchCommunity(textSearch, currentPage);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if (!TextUtils.isEmpty(newText)) {
//                    BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.LOCK);
//                    baseEvent.setLock(false);
//                    EventBus.getDefault().post(baseEvent);
//                    textSearch = newText;
//                    currentPage = 1;
//                    getBaseActivity().showProgressDialog();
//                    setActionFloat(true);
//                    communityPresenterImp.searchCommunity(textSearch, currentPage);
//                }
                return false;
            }
        };


        if (searchView != null) {

            searchView.setOnQueryTextListener(queryTextListener);
            searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

                @Override
                public void onViewDetachedFromWindow(View arg0) {
                    BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.LOCK);
                    baseEvent.setLock(false);
                    EventBus.getDefault().post(baseEvent);
                    textSearch = "";
                    getBaseActivity().showProgressDialog();
                    setActionFloat(false);
                    rlNodata.setVisibility(View.GONE);
                    communityPresenterImp.getCommunity(currentPage);
                }

                @Override
                public void onViewAttachedToWindow(View arg0) {
                    // search was opened
                    Log.d("open,", " open");
                    setActionFloat(true);
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.search:
                setActionFloat(true);
                BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.LOCK);
                baseEvent.setLock(true);
                EventBus.getDefault().post(baseEvent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        communityPresenterImp.detachView();
    }

    @Override
    public void onSuccess(List<Community> lstCommunity) {
        Util.hideKeyboard(getBaseActivity());
        getBaseActivity().dismissProgressDialog();
        if (currentPage == 1) {
            communityAdapter.clear();
        }
        stopLoading = lstCommunity.size() == 0;
        communityAdapter.addAll(lstCommunity);
        lvCommunity.setNotShowLoadMore(stopLoading);
        lvCommunity.stopShowLoading();
        if (communityAdapter.getList().size() > 0) {
            rlNodata.setVisibility(View.GONE);
        } else {

            rlNodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(String message) {

    }
}
