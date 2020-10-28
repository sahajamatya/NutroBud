package com.example.nutrobud.ui.contact;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nutrobud.R;

public class ContactFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact, container, false);
        final TextView contactEmail = root.findViewById(R.id.text_contactEmail);
        contactEmail.setText(Html.fromHtml("<a href=\"mailto:support@nutrobud.com\">support@nutrobud.com</a>"));
        contactEmail.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView contactPhone1 = root.findViewById(R.id.text_contactPhone1);
        contactPhone1.setText(Html.fromHtml("<a href=\"tel:817-272-7066\">(817)-272-7066</a>"));
        contactPhone1.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView contactPhone2 = root.findViewById(R.id.text_contactPhone2);
        contactPhone2.setText(Html.fromHtml("<a href=\"tel:817-272-8016\">(817)272-8016</a>"));
        contactPhone2.setMovementMethod(LinkMovementMethod.getInstance());
        return root;
    }

}