package com.shurik.memwor_24.pumpwimo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.shurik.memwor_24.R;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;

    // изобржения
    int[] images = {
            R.drawable.hi,
            R.drawable.quest,
            R.drawable.rating,
            R.drawable.friends,
            R.drawable.award
    };

    // заголовки
    int[] headings = {
            R.string.first_slide_heading,
            R.string.second_slide_heading,
            R.string.third_slide_heading,
            R.string.fourth_slide_heading,
            R.string.fifth_slide_heading
    };

    // описание
    int[] descriptions = {
            R.string.first_slide_description,
            R.string.second_slide_description,
            R.string.third_slide_description,
            R.string.fourth_slide_description,
            R.string.fifth_slide_description
    };

    // сводки
    int[] swards = {
            R.string.swards_first,
            R.string.swards_second,
            R.string.swards_third,
            R.string.swards_fourth,
            R.string.swards_fifth
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

        ImageView slideTitleImage = (ImageView) view.findViewById(R.id.titleImage);

        TextView slideHeading = (TextView) view.findViewById(R.id.textTitle);
        TextView slideDescription = (TextView) view.findViewById(R.id.textDescription);
        TextView sward = (TextView) view.findViewById(R.id.sward);

        slideTitleImage.setImageResource(images[position]);
        slideHeading.setText(headings[position]);
        slideDescription.setText(descriptions[position]);
        sward.setText(swards[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}

