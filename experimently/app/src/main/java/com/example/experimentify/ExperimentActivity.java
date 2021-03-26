package com.example.experimentify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;

// AppCompatActivity
public class ExperimentActivity extends AppCompatActivity {

    private TextView description;
    private TextView date;
    private TextView expType;
    private TextView location;
    private Button statsButton;
    private Button chatButton;
    private Experiment exp;

    private CardView count;
    private Button countButton;

    private CardView binomial;
    private Button passButton;
    private Button failButton;

    private CardView integer;
    private EditText intInput;

    private CardView measure;
    private EditText measureInput;
    private TextView endedMessageBox;
    private Button submitButton;

    private Button qrCodeGene;
    private ImageView qrCodeShow;



    public static final String PREFS_NAME = "PrefsFile";

    private FirebaseFirestore db;

    private Trial newTrial;

    /**
     * This method sets text in the UI.
     */
    private void initUi() {
        description.setText(this.getResources().getString(R.string.description_header) + exp.getDescription());
        date.setText(this.getResources().getString(R.string.date_header) + exp.getDate());
        expType.setText(this.getResources().getString(R.string.exp_type_header) + exp.getExpType());
        location.setText(this.getResources().getString(R.string.region_header) + exp.getRegion());
    }

    /**
     * This method displays a message when an experiment has ended.
     */
    private void showExpEndedMessage() {
        endedMessageBox.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);
    }

    /**
     * This method displays a button for submitting trials when the experiment is ongoing.
     */
    private void showSubmitButton() {
        endedMessageBox.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
    }


    private Trial trial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment);

        description = findViewById(R.id.activ_descrip);
        date = findViewById(R.id.activ_date);
        expType = findViewById(R.id.activ_expType);
        location = findViewById(R.id.activ_location);
        statsButton = findViewById(R.id.stats);
        chatButton = findViewById(R.id.chat);
        count = findViewById(R.id.Count);
        countButton = findViewById(R.id.increaseCount);
        binomial = findViewById(R.id.Binomial);
        passButton = findViewById(R.id.pass);
        failButton = findViewById(R.id.fail);
        integer = findViewById(R.id.Integer);
        intInput = findViewById(R.id.integerInput);
        measure = findViewById(R.id.measurement);
        measureInput = findViewById(R.id.meaasurementInput);
        endedMessageBox = findViewById(R.id.trialEndedMessage);
        submitButton = findViewById(R.id.submitTrials);

        qrCodeGene = findViewById(R.id.qrCode);
        qrCodeShow = findViewById(R.id.qrCodeView);

        db = FirebaseFirestore.getInstance();

        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        String localUID = settings.getString("uid", "0");

        ExperimentController experimentController = new ExperimentController(this);

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        Intent intent = getIntent();
        if (intent.hasExtra("clickedExp")) {
            exp = intent.getParcelableExtra("clickedExp");
            initUi();


            statsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            qrCodeGene.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap temp = null;
                    String tempID = (exp.getUID().toString());
                    try {
                        temp = qrCodeGen.textToImage(tempID,500,500);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    qrCodeShow.setImageBitmap(temp);
                    //System.out.println("testtest"+temp);
                    qrCodeShow.setVisibility(View.VISIBLE);
                }
            });
            // If editable then display ui for conducting trials, else show message
            if (exp.isEditable()) {
                if (exp.getExpType().equals("Count")) {
                    count.setVisibility(View.VISIBLE);
                    countButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CountTrials countTrial = new CountTrials(localUID, exp.getUID(), exp.getRegion());
                            experimentController.addTrialToDB(countTrial, db);
                        }
                    });
                }

                if (exp.getExpType().equals("Binomial")) {
                    binomial.setVisibility(View.VISIBLE);

                    passButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    failButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }

                if (exp.getExpType().equals("Integer")) {
                    integer.setVisibility(View.VISIBLE);
                    showSubmitButton();
                }

                if (exp.getExpType() .equals("Measurement")) {
                    measure.setVisibility(View.VISIBLE);
                    showSubmitButton();

                }


            }
            else {
                showExpEndedMessage();
            }
        }
    }
}