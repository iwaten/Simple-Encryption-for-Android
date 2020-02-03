package secretswamp.simpleencryption;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import secretswamp.simpleencryption.Crypt.KeyStore;
import secretswamp.simpleencryption.R;
import secretswamp.simpleencryption.Crypt.CryptUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DecryptMessageFragment extends Fragment {
    private static final String TAG = "DecryptMessageFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(secretswamp.simpleencryption.R.layout.tab2, container, false);
        Button btnTEST = view.findViewById(secretswamp.simpleencryption.R.id.btnTEST2);

        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryDecryptMessage();
            }
        });

        return view;
    }

    private void tryDecryptMessage() {
        KeyStore keys = KeyStore.getKeyStoreInstance();
        keys.loadKeys(getContext());
        View v = getView();
        assert v != null;
        String message = ((EditText)(v.findViewById(R.id.encryptedEditText))).getText().toString();

        if (message.length() != 0) {
            Toast.makeText(getActivity(), "Decrypting...", Toast.LENGTH_SHORT).show();
            String decMessage = CryptUtils.decryptMessage(message, keys.getMyPrivateKey());
            EditText decMessageEditText = (EditText) (getView().findViewById(R.id.outputEditText));
            Toast.makeText(getActivity(), "Decrypting complete.", Toast.LENGTH_SHORT).show();
            decMessageEditText.setText(decMessage);
        } else {
            Toast.makeText(getActivity(), "Nothing to decrypt.", Toast.LENGTH_SHORT).show();
        }

    }
}