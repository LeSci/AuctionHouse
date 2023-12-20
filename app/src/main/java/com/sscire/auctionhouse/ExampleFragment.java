package com.sscire.auctionhouse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.zip.Inflater;

// https://www.youtube.com/watch?v=HZYYjY2NSKk&list=PLrnPJCHvNZuBkhcesO6DfdCghl6ZejVPc
// How to Send Data to a New Fragment with a Factory Method - Android Studio Tutorial
public class ExampleFragment extends Fragment {

    private static final String ARG_TEXT = "argText";
    private static final String ARG_TEXT2 = "argText2";
    private static final String ARG_NUMBER = "argNumber";
    private String text;
    private String text2;
    private int number;

    public static ExampleFragment newInstance(String text, int number, String text2){
        ExampleFragment fragment = new ExampleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putInt(ARG_NUMBER, number);
        args.putString(ARG_TEXT2, text2);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.example_fragment, container, false);
        TextView textView = v.findViewById(R.id.textView_Fragment);
        TextView textView2 = v.findViewById(R.id.textView2_Fragment);

        if(getArguments()!=null){
            text = getArguments().getString(ARG_TEXT);
            number = getArguments().getInt(ARG_NUMBER);

            text2 = getArguments().getString(ARG_TEXT2);
        }

        textView.setText(text + number);
        textView2.setText(text2);
        return v;
    }
}
