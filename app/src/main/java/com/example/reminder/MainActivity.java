package com.example.reminder;

        import android.app.DatePickerDialog;
        import android.app.TimePickerDialog;
        import android.content.ActivityNotFoundException;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.Build;
        import android.os.Bundle;
        import android.speech.RecognizerIntent;
        import android.speech.tts.TextToSpeech;
        import android.util.Log;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.ArrayAdapter;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TimePicker;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.constraintlayout.widget.ConstraintLayout;
        import androidx.core.app.ActivityCompat;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import androidx.viewpager.widget.ViewPager;


        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.android.material.tabs.TabLayout;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;

        import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final String TAG = "MainActivity";

    static int stage_variable=0;
    static String values = "";

    ArrayList<Reminder_detail> reminders = new ArrayList<Reminder_detail>();

    List<item> mlist = new ArrayList<item>();


    public TextToSpeech texttoSpeech;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    String rdesc;
    String rdate;
    String rtime;
    static String message;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_list);

        //microphone permission
        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);

        //text to speech
        texttoSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //VoiceInput
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                values = "";
                startVoiceInput();
            }
        });

        //read n add cards
        addValueListener();
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Do you want to add a reminder?");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            texttoSpeech.speak("Do you want to add a reminder?", TextToSpeech.QUEUE_FLUSH,null,null);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stage_variable=1;

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
        }

    }


    private void add_reminder() {


        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What's the reminder?");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            texttoSpeech.speak("What's the reminder?", TextToSpeech.QUEUE_FLUSH, null, null);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stage_variable=2;
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }


    private void add_date() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            texttoSpeech.speak("Please select a date.", TextToSpeech.QUEUE_FLUSH, null, null);
        }
        Log.i(TAG,"Date called");
        datePickerDialog.show();


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // add date to database
        String format_date = year + "-" + month + "-" + dayOfMonth;
        SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        SimpleDateFormat EEEddMMMyyyy = new SimpleDateFormat("EEE dd-MMM-yyyy", Locale.US);
        String d = "";
        d = parseDate(format_date, ymdFormat, EEEddMMMyyyy);
        rdate = d.replaceAll("-", " ");
        add_time();
    }


    public static String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }


    private void add_time() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            texttoSpeech.speak("Please select the time.", TextToSpeech.QUEUE_FLUSH, null, null);
        }

        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        rtime = hourOfDay + ":" + minute;
        stage_variable = 3;
        add_to_database();

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (stage_variable == 1) {
                        String choice = result.get(0);
                        if (choice.equals("yes")) {
                            add_reminder();
                        }
                        else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                texttoSpeech.speak("Okay. Press this button anytime to add a reminder", TextToSpeech.QUEUE_FLUSH, null, null);
                            }
                        }

                    }
                    else if (stage_variable == 2) {
                        rdesc = result.get(0);
                        add_date();
                    }
                    if (stage_variable == 3){

                        // speech output repeat

                    }


                }
                break;
            }

        }
    }


    public void add_to_database(){

        String key = databaseReference.push().getKey();

        databaseReference.child(key).child("reminder_desc").setValue(rdesc);
        databaseReference.child(key).child("reminder_date").setValue(rdate);
        databaseReference.child(key).child("reminder_time").setValue(rtime);
        mlist.clear();
        addValueListener();

    }

    public void addValueListener(){
        ValueEventListener vl = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.i(TAG, "CHILD ADDED");

                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    Reminder_detail rd = new Reminder_detail();
                    rd.reminder_desc = snapshot.child("reminder_desc").getValue(String.class);
                    rd.reminder_date =  snapshot.child("reminder_date").getValue(String.class);
                    rd.reminder_time =  snapshot.child("reminder_time").getValue(String.class);

                    reminders.add(rd);

                    String c_day = rd.reminder_date.substring(0,3);
                    String c_date = rd.reminder_date.substring(4,6);
                    String c_month = rd.reminder_date.substring(7,10);

                    mlist.add(new item(c_date,c_month,c_day,rd.reminder_desc,rd.reminder_time));

                   System.out.println(c_date);
                    System.out.println(c_day);
                    System.out.println(c_month);


                    i++;

                }

                add_to_card();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(vl);
    }


    private void add_to_card() {

        adapter _adapter = new adapter(this,mlist);
        recyclerView.setAdapter(_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}


