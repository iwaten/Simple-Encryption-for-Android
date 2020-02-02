package secretswamp.simpleencryption.com.tabian.tabfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import secretswamp.simpleencryption.R;
import secretswamp.simpleencryption.com.tabian.tabfragments.pgp.PGPUtils;

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
        Toast.makeText(getActivity(), "TESTING BUTTON CLICK 3",Toast.LENGTH_SHORT).show();
        String pubKey;
        String userInput;
        try {
        pubKey = ((EditText) (getView().findViewById(R.id.pubkeytext))).getText().toString();
        userInput = ((EditText) (getView().findViewById(R.id.userinputtext))).getText().toString();
        } catch (NullPointerException e) {
            return;
        }

        String encMessage = PGPUtils.encryptMessage(userInput, (PublicKey) PGPUtils.decodePublic(pubKey));
        ((EditText)getView().findViewById(R.id.outputtext)).setText(encMessage);
    }
}
