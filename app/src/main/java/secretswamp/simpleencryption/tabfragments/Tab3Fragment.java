package secretswamp.simpleencryption.tabfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import secretswamp.simpleencryption.CryptUtils.CryptUtils;
import secretswamp.simpleencryption.R;
import secretswamp.simpleencryption.tabfragments.MainActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.security.PublicKey;


public class Tab3Fragment extends Fragment {
    private static final String TAG = "Tab3Fragment";

    private Button btnTEST;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(secretswamp.simpleencryption.R.layout.tab3,container,false);
        btnTEST = (Button) view.findViewById(secretswamp.simpleencryption.R.id.btnTEST3);

        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bruh();
            }
        });

        return view;
    }

    public void bruh() {
        Toast.makeText(getActivity(), "Encrypting",Toast.LENGTH_SHORT).show();
        String userInput;
        String encryptionKey;
        KeyStore keys = KeyStore.getKeyStoreInstance();
        keys.loadKeys(getContext());
        try {
            encryptionKey = ((EditText) (getView().findViewById(R.id.pubkeytext))).getText().toString();
            userInput = ((EditText) (getView().findViewById(R.id.userinputtext))).getText().toString();
        } catch (NullPointerException e) {
            return;
        }
        if(userInput.length() != 0 && encryptionKey.length() != 0){
            Toast.makeText(getActivity(), "Encrypting",Toast.LENGTH_SHORT).show();
            String encMessage = CryptUtils.encryptMessage(userInput,keys.getMyPublicKey() );
            Toast.makeText(getActivity(), "Encryption complete",Toast.LENGTH_SHORT).show();
            ((EditText)getView().findViewById(R.id.outputtext)).setText(encMessage);
        }else{
            Toast.makeText(getActivity(), "Missing either the key or the data",Toast.LENGTH_SHORT).show();
        }

    }
}