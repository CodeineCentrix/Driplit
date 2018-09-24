package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.s215131746.driplit.R;

import org.w3c.dom.Text;

/**
 * Created by s215131746 on 2018/09/17.
 */

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context)
    {
        this.context = context;
    }

    //Arrays
    public int[] slide_images = { R.drawable.report, R.drawable.take_pic_leak, R.drawable.menu_option, R.drawable.reported_leaks, R.drawable.filter_rleaks};
    public String[] slide_headings = {"Report Leak", "Take Picture", "Menu Option", "Filter Reported Leaks"};
    public String[] slide_desc = {"Make Sure You Are Where The Leak Is.\n Click The Button Above To Report Leak \n\n Make Sure Your Location Is Turned On", "Click YES to take a picture\nClick NO to not take a picture\nClick CANCEL to stop reporting", "To See Reported Leaks \nClick The Menu Option Then Select Reported Leaks", "To Filter Reported Leaks \nClick The Menu Option And Select Any option."};

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView imgView = (ImageView) view.findViewById(R.id.imgViewLocation);
        TextView heading = (TextView) view.findViewById(R.id.txtViewHead);
        TextView description = (TextView) view.findViewById(R.id.txtViewDesc);

        imgView.setImageResource(slide_images[position]);
        heading.setText(slide_headings[position]);
        description.setText(slide_desc[position]);

        container.addView(view);

        return view;
    };

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
