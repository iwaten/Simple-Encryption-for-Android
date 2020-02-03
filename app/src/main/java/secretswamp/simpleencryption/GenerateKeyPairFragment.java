package secretswamp.simpleencryption;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.KeyPair;

import secretswamp.simpleencryption.Crypt.KeyStore;
import secretswamp.simpleencryption.R;
import secretswamp.simpleencryption.Crypt.CryptUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class GenerateKeyPairFragment extends Fragment {
    private static final String TAG = "GenerateKeyPairFragment";

    private Button btnTEST;
    private EditText keyEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(secretswamp.simpleencryption.R.layout.tab1, container, false);
        btnTEST = view.findViewById(R.id.btnTEST);
        keyEditText = view.findViewById(R.id.generatedKey);

        btnTEST.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                generateNewKeys(v);
            }
        });
        return view;
    }

    private void generateNewKeys(View view) {
        KeyPair kp = CryptUtils.generateKeyPair();
        KeyStore keys = KeyStore.getKeyStoreInstance();
        keys.loadKeys(getContext());
        keys.setMyPrivateKey(kp.getPrivate());
        keys.setMyPublicKey(kp.getPublic());
        keys.storeKeys(getContext());
        Toast.makeText(getActivity(), "New Keypair Generated", Toast.LENGTH_SHORT).show();
        keyEditText.setText(CryptUtils.encodePublicKeyToBase64(keys.getMyPublicKey()));
    }
}
