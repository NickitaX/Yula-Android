package nickita.gq.yula.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import nickita.gq.yula.R;
import nickita.gq.yula.model.GeoTag;

/**
 * Created by admin on 13/7/17.
 */
public class MeetupsPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<GeoTag> mTags;

    @Override
    public int getCount() {
        return mTags.size();
    }

    public MeetupsPagerAdapter(Context context, List<GeoTag> tags){
        mContext = context;
        mTags = tags;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.meetup_item_layout, container,
                false);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
