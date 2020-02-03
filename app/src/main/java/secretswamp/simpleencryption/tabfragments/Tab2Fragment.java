package secretswamp.simpleencryption.tabfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import secretswamp.simpleencryption.R;
import secretswamp.simpleencryption.CryptUtils.CryptUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    private Button btnTEST;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(secretswamp.simpleencryption.R.layout.tab2,container,false);
        btnTEST = (Button) view.findViewById(secretswamp.simpleencryption.R.id.btnTEST2);

        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bruh();

            }
        });

        return view;
    }

    public void bruh() {
        SharedPreferences pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        // Get data from "encrypted message" text box here (message = ...)
        String message;
        try {
            message = ((EditText) (getView().findViewById(R.id.encryptedEditText))).getText().toString();
        } catch(NullPointerException e) {
            return;
        }
        //message = message.replace("\n", "");
        String decMessage = CryptUtils.decryptMessage(message, CryptUtils.decodePrivate(
                pref.getString("priv-key", null)));
        EditText decMessageEditText = (EditText) (getView().findViewById(R.id.outputEditText));
        decMessageEditText.setText(decMessage);
    }
}