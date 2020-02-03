package secretswamp.simpleencryption.tabfragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;

import java.security.KeyPair;

import secretswamp.simpleencryption.R;
import secretswamp.simpleencryption.CryptUtils.CryptUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Tab1Fragment extends Fragment {
    private static final String TAG = "Public Key Generator";

    private Button btnTEST;
    private EditText keyEditText;
    private EditText privateKeyEditText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(secretswamp.simpleencryption.R.layout.tab1,container,false);
        btnTEST = (Button) view.findViewById(R.id.btnTEST);
        keyEditText = (EditText) (view.findViewById(R.id.generatedKey));
        privateKeyEditText = (EditText) (view.findViewById(R.id.generatedPrivateKey));

        btnTEST.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                generateNewKeys(v);
            }
        });
        return view;
    }

    public void generateNewKeys(View view) {
        SharedPreferences pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        KeyPair kp = CryptUtils.generateKeyPair();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("pub-key", CryptUtils.encodePublic(kp.getPublic()));
        editor.putString("priv-key", CryptUtils.encodePrivate(kp.getPrivate()));
        keyEditText.setText(CryptUtils.encodePublic(kp.getPublic()));
        privateKeyEditText.setText(CryptUtils.encodePrivate(kp.getPrivate()));

    }
}
