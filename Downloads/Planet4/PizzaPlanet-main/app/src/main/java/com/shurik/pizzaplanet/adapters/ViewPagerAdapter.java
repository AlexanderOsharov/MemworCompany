package com.shurik.pizzaplanet.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.shurik.pizzaplanet.R;

// viewPager для tutorialActivity
public class ViewPagerAdapter extends PagerAdapter {

    Context context;

    // изобржения
    int[] images = {
            R.drawable.hi,
            R.drawable.cus_del,
            R.drawable.cafe,
            R.drawable.pizza,
    };

    // заголовки
    int[] headings = {
            R.string.first_slide_heading,
            R.string.second_slide_heading,
            R.string.third_slide_heading,
            R.string.fourth_slide_heading
    };

    // описание
    int[] descriptions = {
            R.string.first_slide_description,
            R.string.second_slide_description,
            R.string.third_slide_description,
            R.string.fourth_slide_description
    };


    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.slider_layout, container, false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView slideTitleImage = (ImageView) view.findViewById(R.id.titleImage);

        TextView slideHeading = (TextView) view.findViewById(R.id.textTitle);
        TextView slideDescription = (TextView) view.findViewById(R.id.textDescription);

        slideTitleImage.setImageResource(images[position]);
        slideHeading.setText(headings[position]);
        slideDescription.setText(descriptions[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}