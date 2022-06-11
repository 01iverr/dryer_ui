package activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.aueb.idry.R;
import com.aueb.idry.T8816WP.TumbleDryer;
import com.aueb.idry.T8816WP.TumbleDryerImp;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import model.Preference;
import model.PreferenceDAO;

public class FunctionButtonsFragment extends Fragment {
    private Button doorUnlockBtn;
    private TumbleDryer dryer;
    private TextToSpeech tts;
    private Preference preference;

    public FunctionButtonsFragment() {
        // Required empty public constructor
    }

    public static FunctionButtonsFragment newInstance() {
        return new FunctionButtonsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preference = PreferenceDAO.getInstance(getContext()).retrievePreference();
        if (preference.getVoiceInstructions()) {
            initTextToSpeech();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_function_buttons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get door unlock button
        doorUnlockBtn = view.findViewById(R.id.doorUnlockBtn);

        // Get the dryer's implementation
        dryer = TumbleDryerImp.getInstance();

        // Set listeners to the fragment's buttons
        // Settings button
        ImageButton settingsBtn = view.findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(v -> {
            // Navigate to the settings activity
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), Settings.class);
                startActivity(intent);
            }
        });


        //Home Button
        ImageButton HomeBtn;
        HomeBtn = view.findViewById(R.id.homeBtn);
        HomeBtn.setOnClickListener(v -> {
            // Navigate to the first page with favorites programs
            if (getActivity() != null) {
                startActivity(new Intent(getActivity(), RoutineMenuActivity.class));
            }
        });

//        if (this.getClass().getSimpleName().contains("SelectionFirstStepActivity")
//                || this.getClass().getSimpleName().contains("SelectionSecondStepActivity")
//                || this.getClass().getSimpleName().equals("SelectionThirdStepActivity")){
//            HomeBtn.setVisibility(View.VISIBLE);
//        } else{
//            HomeBtn.setVisibility(View.GONE);}



        // Door unlock button
        doorUnlockBtn.setOnClickListener(v -> {
            // Open the door and hide this button
                // Navigate to the door activity
                if (getActivity() != null) {
                    Intent temp=new Intent(getActivity(), DoorGuideActivity.class);
                    temp.putExtra("class",String.valueOf(getActivity()));
                    startActivity(temp);
                }
            dryer.openDoor();
            hideDoorUnlockBtn();

            // Display a pop-up informing the use that the door is now unlocked
            Snackbar.make(v, R.string.door_unlocked_message, Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Check whether the door is open
        if (dryer.isClosed()) {
            // Display button for closing the door
            displayDoorUnlockBtn();
        } else {
            hideDoorUnlockBtn();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (preference.getVoiceInstructions()) {
            tts.stop();
            tts.shutdown();
        }
    }

    public void displayDoorUnlockBtn() {
        if (doorUnlockBtn != null) {
            doorUnlockBtn.setVisibility(View.VISIBLE);
        }
    }

    public void hideDoorUnlockBtn() {
        if (doorUnlockBtn != null) {
            doorUnlockBtn.setVisibility(View.GONE);
        }

        // Use speech-to-text to inform the user that the door is now unlocked
        if (preference.getVoiceInstructions()) {
            String toSpeak = getString(R.string.tts_door_unlocked);
            tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "tts_door_unlocked");
        }
    }

    // Helper method
    // Initialize the text-to-speech component
    private void initTextToSpeech() {
        tts = new TextToSpeech(getContext(), i -> {
            if (i != TextToSpeech.ERROR) {
                // Check language availability
                Locale locale = getResources().getConfiguration().locale;
                if (tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                    tts.setLanguage(locale);
                } else {
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }
}