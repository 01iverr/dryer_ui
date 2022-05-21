package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aueb.idry.R;

public class FilterGuideThirdActivity extends AppCompatActivity {

    Button nextButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_guide_third);

        backButton = findViewById(R.id.back_btn);
        nextButton = findViewById(R.id.next_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FilterGuideFourthActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FilterGuideSecondActivity.class);
                startActivity(intent);
            }
        });
    }
}