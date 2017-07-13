package nickita.gq.yula.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.LinkedList;
import java.util.List;

import nickita.gq.yula.R;
import nickita.gq.yula.adapters.MeetupsPagerAdapter;
import nickita.gq.yula.callbacks.OnTagsPulledCallback;
import nickita.gq.yula.model.GeoTag;
import nickita.gq.yula.utils.FragmentBroker;
import nickita.gq.yula.utils.TagsManager;

/**
 * Created by admin on 13/7/17.
 */
public class MeetupsFragment extends Fragment {
    private View mView;
    private List<GeoTag> mTags;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.meetup_fragment_layout, container, false);
        initialize();
        loadTags();
        return mView;
    }

    private void initialize(){
        mProgressBar = (ProgressBar) mView.findViewById(R.id.meetups_progress);
    }

    private void showProgress(boolean state){
        if(state){
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void loadTags(){
        showProgress(true);
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

    public void reloadMeetups(){
        loadTags();
    }

    private void setUpViewPager() {
        ViewPager viewPager = (ViewPager) mView.findViewById(R.id.meetup_pager);
        MeetupsPagerAdapter adapter = new MeetupsPagerAdapter(mView.getContext(),mTags);
        viewPager.setAdapter(adapter);
        showProgress(false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ((MapFragment)FragmentBroker.getFragmentByTag(mView.getContext(), R.id.main_container))
                        .navigateToTagWithId(mTags.get(position).getTagid());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
