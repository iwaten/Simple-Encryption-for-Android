package secretswamp.simpleencryption.com.tabian.tabfragments;

import android.content.Context;
import android.content.SharedPreferences;
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
    private EditText pubKeyEditText;
    private EditText userInputEditText;
    private EditText outputEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(secretswamp.simpleencryption.R.layout.tab3,container,false);
        btnTEST = (Button) view.findViewById(secretswamp.simpleencryption.R.id.btnTEST3);

        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 3",Toast.LENGTH_SHORT).show();
                SharedPreferences pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
                pubKeyEditText = ((EditText) (view.findViewById(R.id.pubkeyEditText)));
                userInputEditText = ((EditText) (view.findViewById(R.id.userinputEditText)));
                if(pubKeyEditText == null || userInputEditText == null) {
                    return;
                }

                String encMessage = PGPUtils.encryptMessage(userInputEditText.getText().toString(), (PublicKey) PGPUtils.decodeKey(pubKeyEditText.getText().toString(), false));
                // Should update the "encrypted message" text box here with encMessage
            }
        });

        return view;
    }
}
