package com.example.slution;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BufferFragment extends Fragment {

    public BufferFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_buffer_fragment, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab_confirm);
        if (fab != null) {
            fab.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Translation Confirmed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            });
        }

        return view;
    }
}
