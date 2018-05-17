package com.jhacks.vrclassroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AskQuestion extends AppCompatActivity {
    String question,optionA,optionB,optionC,optionD,allChoices;
    EditText choiceA, choiceB, choiceC, choiceD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
    }

    public void sendQuestion(View view){
        EditText questionEditText;
        allChoices = grabValidChoices();
        questionEditText = findViewById(R.id.questionText);
        question = questionEditText.getText().toString().trim();
        String text = "No Question Asked!";
        Toast mToastToShow; // Notifier for question input
        String referenceTagQuestions = "";
        final String logTag = "Professor database: ";

        // Make sure a question field has input (Question is being asked)
        if (question.length() < 0)
        {

            mToastToShow = Toast.makeText(AskQuestion.this, text, Toast.LENGTH_SHORT);
            mToastToShow.show();
        }
        else {
            referenceTagQuestions = question;
            mToastToShow = Toast.makeText(AskQuestion.this, "Question sent!", Toast.LENGTH_SHORT);



            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(referenceTagQuestions);
            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(logTag, "Value is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(logTag, "Failed to read value.", error.toException());
                }
            });

            // If we do have choices to choose from then save the data to the database.
            if (allChoices.length() > 0) {
                // Place holder for now - Set value of reference to contain these values
                myRef.setValue(allChoices);
            }


            mToastToShow.show();
            finish();
        }



    }

    private String grabValidChoices(){

        // Remember to delimit by comma for questions

        String choicesToSend = "";


        choiceA = findViewById(R.id.editTextA);
        choiceB = findViewById(R.id.editTextB);
        choiceC = findViewById(R.id.editTextC);
        choiceD = findViewById(R.id.editTextD);

        // Parse the choices
        optionA = choiceA.getText().toString().trim();
        optionB = choiceB.getText().toString().trim();
        optionC = choiceC.getText().toString().trim();
        optionD = choiceD.getText().toString().trim();

        // Check if there was an option typed in for choices.
        // If so, concatenate to be used later.
        if (optionA.length() > 0)
            choicesToSend = choicesToSend + "," + optionA;
        if (optionB.length() >0)
            choicesToSend = choicesToSend + "," + optionB;
        if (optionC.length() >0)
            choicesToSend = choicesToSend + "," + optionC;
        if (optionD.length() >0)
            choicesToSend = choicesToSend + "," + optionD;

        return choicesToSend;
    }

}
