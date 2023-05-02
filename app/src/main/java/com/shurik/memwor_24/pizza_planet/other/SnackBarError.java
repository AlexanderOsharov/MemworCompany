package com.shurik.memwor_24.pizza_planet.other;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.shurik.memwor_24.R;

// класс snackbar - а ошибки
// todo можно сделать alert dialog - как захочется!
public class SnackBarError {

    private Snackbar snackbar;
    private View snackbarView;
    private Snackbar.SnackbarLayout snackbarLayout;

    // метод для создания snackbar - а ошибки
    public void showResponseErrorSnackbar(View view, LayoutInflater inflater) {
        snackbar = Snackbar.make(view,
                "Упс..Что - то пошло не так",
                Snackbar.LENGTH_LONG);
        snackbarView = inflater.inflate(R.layout.snackbar_error, null);
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.addView(snackbarView, 0);
        snackbar.show();
    }
}