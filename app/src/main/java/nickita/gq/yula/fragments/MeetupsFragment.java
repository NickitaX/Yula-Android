package nickita.gq.yula.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import nickita.gq.yula.R;
import nickita.gq.yula.adapters.MeetupsPagerAdapter;
import nickita.gq.yula.callbacks.OnTagsPulledCallback;
import nickita.gq.yula.model.GeoTag;
import nickita.gq.yula.utils.TagsManager;

/**
 * Created by admin on 13/7/17.
 */
public class MeetupsFragment extends Fragment {
    private View mView;
    private List<GeoTag> mTags;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.meetup_fragment_layout, container, false);
        loadTags();
        return mView;
    }

    private void loadTags(){
        TagsManager.pullGeoTagsFromServer(new OnTagsPulledCallback() {
            @Override
            public void pulled(List<GeoTag> tags) {
                mTags = tags;
                ((Activity)mView.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUpViewPager();
                    }
                });
            }
        });
    }

    private void setUpViewPager() {
        ViewPager viewPager = (ViewPager) mView.findViewById(R.id.meetup_pager);
        MeetupsPagerAdapter adapter = new MeetupsPagerAdapter(mView.getContext(),mTags);
        viewPager.setAdapter(adapter);
    }
}
