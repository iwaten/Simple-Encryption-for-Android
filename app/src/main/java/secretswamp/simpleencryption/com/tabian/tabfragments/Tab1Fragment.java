package secretswamp.simpleencryption.com.tabian.tabfragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;

import secretswamp.simpleencryption.R;
import secretswamp.simpleencryption.com.tabian.tabfragments.pgp.PGPUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Tab1Fragment extends Fragment {
    private static final String TAG = "Public Key Generator";

    private Button btnTEST;
    public EditText keyEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(secretswamp.simpleencryption.R.layout.tab1,container,false);
        btnTEST = (Button) view.findViewById(R.id.btnTEST);
        keyEditText = (EditText) (view.findViewById(R.id.generatedKey));
        btnTEST.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                generateNewKeys(v);
            }
        });
        return view;
    }

    public void generateNewKeys(View view) {
        SharedPreferences pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        KeyPair kp = PGPUtils.generateKeyPair();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("pub-key", PGPUtils.encodeKey(kp.getPublic()));
        editor.putString("priv-key", PGPUtils.encodeKey(kp.getPrivate()));
        try{
            keyEditText.setText(new String(Base64.encode(kp.getPublic().getEncoded(),0),"UTF-8"));
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }

    }
}
