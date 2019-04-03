package com.example.team13.personalbest_team13_skeleton;

import android.app.Activity;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team13.personalbest_team13_skeleton.activities.ChatActivity;
import com.example.team13.personalbest_team13_skeleton.activities.ChatBoxActivity;
import com.example.team13.personalbest_team13_skeleton.activities.FriendList;
import com.example.team13.personalbest_team13_skeleton.activities.GoalSettingsActivity;
import com.example.team13.personalbest_team13_skeleton.activities.SettingsActivity;
import com.example.team13.personalbest_team13_skeleton.activities.StackedBarChartActivity;
import com.example.team13.personalbest_team13_skeleton.activities.ViewFriendActivities;
import com.example.team13.personalbest_team13_skeleton.activities.ViewMonthlySummary;
import com.example.team13.personalbest_team13_skeleton.chatmessage.FirestoreChatAdapter;
import com.example.team13.personalbest_team13_skeleton.dialogs.CongratsDialog;
import com.example.team13.personalbest_team13_skeleton.dialogs.DialogManager;
import com.example.team13.personalbest_team13_skeleton.dialogs.StatsDialog;
import com.example.team13.personalbest_team13_skeleton.fitness.FitnessMock;
import com.example.team13.personalbest_team13_skeleton.fitness.FitnessObserver;
import com.example.team13.personalbest_team13_skeleton.friendActivities.FirestoreFriendActivityAdapter;
import com.example.team13.personalbest_team13_skeleton.friendActivities.IFriendActivityService;
import com.example.team13.personalbest_team13_skeleton.notification.FirebaseCloudMessagingAdapter;
import com.example.team13.personalbest_team13_skeleton.notification.MockMessagingService;
import com.example.team13.personalbest_team13_skeleton.notification.NotificationService;
import com.example.team13.personalbest_team13_skeleton.services.DailyCallbacks;
import com.example.team13.personalbest_team13_skeleton.services.DailyService;
import com.example.team13.personalbest_team13_skeleton.fitness.FitnessService;
import com.example.team13.personalbest_team13_skeleton.fitness.FitnessServiceFactory;
import com.example.team13.personalbest_team13_skeleton.fitness.GoogleFitAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CongratsDialog.CongratsDialogListener,
        StatsDialog.StatsDialogListener, DailyCallbacks, FitnessObserver {

    private Person person;
    private ArrayList<String> weekDataList = new ArrayList<String>(
            Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
    private ArrayList<String> plannedActivityDataList = new ArrayList<String>(
            Arrays.asList("SundaySteps", "MondaySteps", "TuesdaySteps", "WednesdaySteps", "ThursdaySteps",
                    "FridaySteps", "SaturdaySteps"));

    // fitness
    private String fitnessServiceKey = "GOOGLE_FIT";
    // google signin
    private static final int RC_SIGN_IN = 14;
    private static final int GOAL_SETTING_ACTIVITY = 99;
    private static final int SETTING_ACTIVITY = 98;
    // Use this service when we what to mock the steps.
    private final String fitnessServiceMock = "MOCK_FIT";

    // TODO (Nate): Remove one of the keys, we only need one.
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "MainActivity";

    private TextView textSteps;
    private TextView textGoal;

    private FitnessService fitnessService;
    private FitnessMock mockFitnessService;

    private DialogManager dialogManager;
    private DataIOStream dataIOStream;

    private DailyService dailyService;
    private boolean bound = false;

    private Button launchPlanWalk;
    private Button launchEndWalk;
    private Button showStatsBtn;
    private Button btnIncrementSteps;
    private Button btnIncrementTime;

    private StatsDialog statsDialog;
    private int yesterdaySteps;

    private boolean useSystemClock = true; // Are we using the system clock?

    public static String messFlag = new String("false");
    public static String messText;

    double elapsedTime;

    private NotificationService notificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "In onCreate");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textSteps = findViewById(R.id.textSteps);
        textGoal = findViewById(R.id.textGoal);

        launchPlanWalk = findViewById(R.id.startWalkButton);

        btnIncrementSteps= findViewById(R.id.incrementSteps);
        btnIncrementTime = findViewById(R.id.incrementTime);

        launchEndWalk = findViewById(R.id.endWalkButton);
        launchEndWalk.setVisibility(View.INVISIBLE);

        showStatsBtn = findViewById(R.id.showStatsOnWalk);
        showStatsBtn.setVisibility(View.INVISIBLE);

        final Button btnUpdateSteps = findViewById(R.id.buttonUpdateSteps);

        if (getIntent().getStringExtra("TEST_MESS") != null)
            messFlag = getIntent().getStringExtra("TEST_MESS");

        if (messFlag.equals("true")) {
            notificationService = new MockMessagingService();
            messText = notificationService.subscribeToNotificationsTopic("chat1", this);
        } else {
            notificationService = new FirebaseCloudMessagingAdapter();
        }
        //subscribeToNotificationsTopic();

        signIn(); // call google signin
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            Log.d(TAG, "SIGNING WORKED");
            String email = GoogleSignIn.getLastSignedInAccount(this).getEmail();
            this.dataIOStream = new DataIOStream(this.getApplicationContext(), email);
            buildPerson(email);
            runFitnessService();
            registerDialogManager();
            messText = notificationService.subscribeToNotificationsTopic(FirestoreChatAdapter.convertToPercent(person.getEmailAcc()), this);
            //notificationService.subscribeToNotificationsTopic(getDate(), this);
        }

        // Allow step count manipulation.
        btnIncrementSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.d(TAG, "Switch to mock fitness");
                // We replace our fitnessService with the mockFitnessService.
                fitnessService = mockFitnessService;
                // We increment the steps by 500.
                mockFitnessService.incrementStepsBy500();
                //person.setTotalSteps(person.getTotalSteps() + FitnessMock.STEP_INC);
                fitnessService.updateStepCount();
            }
        });

        // Allow time manipulation.
        btnIncrementTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.d(TAG, "Increase time 1 hour");
                TimeMachine.useFixedClockAt(TimeMachine.now());
                TimeMachine.passOneHour();
                Toast.makeText(MainActivity.this, "" + TimeMachine.now().toLocalTime().toString(), Toast.LENGTH_LONG).show();
            }
        });

        launchPlanWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // Perform UI changes
                launchPlanWalk.setVisibility(View.INVISIBLE);
                launchEndWalk.setVisibility(View.VISIBLE);
                btnUpdateSteps.setVisibility(View.INVISIBLE);
                showStatsBtn.setVisibility(View.VISIBLE);

                // Start the planned walk
                person.startPlannedWalk();

            }
        });

        launchEndWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                // Perform UI changes
                launchPlanWalk.setVisibility(View.VISIBLE);
                launchEndWalk.setVisibility(View.INVISIBLE);
                showStatsBtn.setVisibility(View.INVISIBLE);
                btnUpdateSteps.setVisibility(View.VISIBLE);

                // End the planned walk
                person.endWalk();

                // Display the planned walk information.
                displayStatsDialog(person.getPlannedWalk());

                // update db with planned walk
                btnUpdateSteps.performClick();
            }
        });


        btnUpdateSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (getIntent().getExtras() == null && !fitnessService.equals("GOOGLE_FIT")) {
                    fitnessService = FitnessServiceFactory.create(fitnessServiceKey, MainActivity.this);
                    fitnessService.setup();
                }*/
                fitnessService.updateStepCount();
                Toast.makeText(MainActivity.this, "Fetching Data ...\nCurrent goal: " +
                        person.getGoal(), Toast.LENGTH_LONG).show();
            }
        });

        showStatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayStatsDialog(person.getPlannedWalk());
            }
        });

        Log.d(TAG, "Initialized shared pref");
        setStepGoalView();

        /* TODO test
        dataIOStream.saveDailyTotalSteps(3000);
        dataIOStream.saveDailyTotalSteps(4500);
        dataIOStream.saveDailyTotalSteps(4123);
        dataIOStream.saveDailyTotalSteps(1000);
        dataIOStream.saveDailyTotalSteps(6000);
        dataIOStream.saveDailyTotalSteps(3300);
        dataIOStream.saveDailyTotalSteps(300);
        dataIOStream.saveDailyTotalSteps(5000);
        dataIOStream.saveDailyPlannedSteps(1000);
        dataIOStream.saveDailyPlannedSteps(1500);
        dataIOStream.saveDailyPlannedSteps(1123);
        dataIOStream.saveDailyPlannedSteps(300);
        dataIOStream.saveDailyPlannedSteps(2000);
        dataIOStream.saveDailyPlannedSteps(1300);
        dataIOStream.saveDailyPlannedSteps(200);
        dataIOStream.saveDailyPlannedSteps(100); */

        // TODO (Nate) XXX:
        if (this.getIntent().getExtras() != null) {
            Log.d(TAG, "Opening new activity");
            Bundle bundle = this.getIntent().getExtras();
            if(bundle.get("click_action") != null) {
                if (bundle.get("click_action").equals("OPEN_CHAT")) {
                    String activityString = (String) bundle.get("click_action");

                    String friendEmail = (String) bundle.get("friendEmail");

                    Intent newIntent = new Intent(this, ChatBoxActivity.class);
                    newIntent.putExtra("EMAIL", person.getEmailAcc());
                    newIntent.putExtra("userEmail", person.getEmailAcc());
                    newIntent.putExtra("friendEmail", friendEmail);

                    Log.d(TAG, "COOL:" + activityString);
                    Log.d(TAG, "COOL:" + person.getEmailAcc());
                    Log.d(TAG, "COOL:" + friendEmail);

                    startActivity(newIntent);
                }
                else if (bundle.get("click_action").equals("OPEN_GOAL")) {

                    Log.d(TAG, "COOL:" + "in goal push notification");
                    //String activityString = (String) bundle.get("click_action");
                    Intent newIntent = new Intent(this, GoalSettingsActivity.class);
                    newIntent.putExtra("GOAL", person.getGoal());

                    startActivity(newIntent);
                }
            }
        }
    }

    private void subscribeToNotificationsTopic() {

        FirebaseMessaging.getInstance().subscribeToTopic(person.getEmailAcc())
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "on start: start the daily service");
        startDailyService();
        // TODO(Nate): Verify only one service is running at a time.
    }

    public void startDailyService() {
        Intent intent = new Intent(this, DailyService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    /** Callbacks for service binding, passed to bindService() */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "in the service connection");
            // cast the IBinder and get MyService instance
            DailyService.LocalBinder binder = (DailyService.LocalBinder) service;
            dailyService = binder.getService();
            dailyService.setCallbacks(MainActivity.this); // register
            bound = true;
            Intent intent = new Intent(MainActivity.this, DailyService.class);
            startService(intent);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // We don't disconnect this service at the moment.
        }
    };

    /**
     * Perform daily actions here.
     */
    @Override
    public void performDailyAction() {
        // XXX(Nate): Note we might have undefined behavior when the application is forced close as
        // no main activity will exist. Thus, nothing will be executed here. We can add a service
        // helper function in the service class that is guaranteed to be executed.

        // TODO: Do all daily actions here.
        Log.d(TAG, "PERFORM DAILY ACTION IN MAIN ACTIVITY");

        // save daily steps
        dataIOStream.saveDailyTotalSteps(person.getTotalSteps());
        dataIOStream.saveDailyPlannedSteps(person.getPlannedSteps());

        // Reset shown congrats and encouragement messages.
        dataIOStream.resetFlags();
        dataIOStream.savePerson(person);
        dataIOStream.resetPersonData();

        Log.d(TAG, "Reset the person obj");
        // Reset the person steps.
        Person tempPerson = dataIOStream.loadPerson();
        person.setTotalSteps(tempPerson.getTotalSteps());
        person.setPlannedSteps(tempPerson.getPlannedSteps());

        //person = dataIOStream.loadPerson();
        Log.d(TAG, "person steps:" + person.getTotalSteps());

        // XXX: Not sure why but when we try to refresh the UI here,
        // it causes the application to crash.
        // We need to refresh the UI to update the steps on the display.

        //fitnessService.updateStepCount();


    }

    // Callback from fitness services.
    @Override
    public void stepsUpdated(int steps) {
        Log.d(TAG, "stepsUpdated: " + steps);

        setStepCount(steps);

        //TODO sync data to cloud db
        updateDB(person.getTotalSteps(), person.getPlannedSteps(), person.getGoal());
    }

    public void updateDB(int totalSteps, int plannedSteps, int goal) {
        IFriendActivityService activities = new FirestoreFriendActivityAdapter(person.getEmailAcc());
        String date = getDate();
        activities.addData(date, totalSteps, plannedSteps, goal);
    }

    public String getDate() {
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        return formattedDate;
    }

    // Update the step count of the person from the fitness service.
    public void setStepCount(long stepCount) {
        Log.d(TAG, "setStepCount: " + stepCount);
        person.setTotalSteps((int)stepCount);
        setStepCountView();
    }

    private void setStepCountView() {
        if (textSteps == null) {
            Log.d(TAG, "view is null");
        }

        if (person == null) {
            Log.d(TAG, "person is null");
        }
        int steps = person.getTotalSteps();
        Log.d(TAG, "setStepCountView: " + steps);

        textSteps.setText("" + steps);
        //textSteps.setText(String.valueOf(person.getTotalSteps()));
    }


    // TODO (Nate): remove after resolving when encouragement should show
    public void setYesterdaySteps(ArrayList<Integer> historyList) {
        this.yesterdaySteps = historyList.get(0);
    }

    // after receiving last 7 days data
    public void setProgress(ArrayList<Integer> historyList) {
        SharedPreferences sharedPreferences = getSharedPreferences("week_progress", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Date currDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currDate);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        int startIdx = 7 - dayOfWeek;
        int j = 0;
        for (int i = startIdx; i < historyList.size(); i++,j++) {
            editor.putInt(weekDataList.get(j), historyList.get(i));
        }

        for (; j < 7; j++) {
            editor.putInt(weekDataList.get(j), 0);
        }
        editor.apply();
/*
        Toast.makeText(MainActivity.this, "Day of week: " + dayOfWeek +
                " Sunday " + sharedPreferences.getInt("Sunday", 0)
                        + " Monday: " + sharedPreferences.getInt("Monday", 0)
                        + " Tuesday: " + sharedPreferences.getInt("Tuesday", 0)
                        + " Wednesday: " + sharedPreferences.getInt("Wednesday", 0)
                        + " Thursday: " + sharedPreferences.getInt("Thursday", 0)
                        + " Friday: " + sharedPreferences.getInt("Friday", 0)
                        + " Saturday: " + sharedPreferences.getInt("Saturday", 0), Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.goalBtn) {
            Intent intent = new Intent(this, GoalSettingsActivity.class);
            intent.putExtra("GOAL", person.getGoal());
            this.startActivityForResult(intent, GOAL_SETTING_ACTIVITY);
        } else if (id == R.id.progressBtn) {
            fitnessService.getWeekData(); // get week data into sharedPref "week_progress", stored as (k(string),v(int)): e.g. "Sunday":41
            Intent intent = new Intent(this, StackedBarChartActivity.class);
            intent.putExtra("currentTotalSteps", person.getTotalSteps());
            intent.putExtra("currentPlannedSteps", person.getPlannedSteps());
            intent.putExtra("currentGoal", person.getGoal());
            intent.putExtra("EMAIL", person.getEmailAcc());
            this.startActivity(intent);
        } else if (id == R.id.settingsBtn) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("HEIGHT", person.getHeight());
            intent.putExtra("STRIDE_LENGTH", person.getStrideLen());
            this.startActivityForResult(intent, SETTING_ACTIVITY);
        } else if (id == R.id.friendsBtn) {
            Intent intent = new Intent(this, FriendList.class);
            intent.putExtra("EMAIL", person.getEmailAcc());
            intent.putExtra("FRIEND_LIST_SERVICE", "firestore");
            this.startActivity(intent);
        } else if (id == R.id.chatBtn) {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("EMAIL", person.getEmailAcc());
            //intent.putExtra("FRIEND_LIST_SERVICE", "firestore");
            this.startActivity(intent);
        } else if (id == R.id.monthlyBtn) {
            Intent intent = new Intent(this, ViewMonthlySummary.class);
            intent.putExtra("userEmail", person.getEmailAcc());
            this.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "Handle sign in");
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else if (requestCode == GOAL_SETTING_ACTIVITY) {
            Log.d(TAG, "Handle setting of goal.");
            person.setGoal(data.getExtras().getInt("NEW_GOAL"));
            setStepGoalView();
        }
        else if (requestCode == SETTING_ACTIVITY) {
            Log.d(TAG, "Handle setting of height");
            person.setHeight(data.getExtras().getFloat("NEW_HEIGHT"));
        }
        // If authentication was required during google fit setup, this will be called after the user authenticates
        else if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Google authentication.");
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        }
        else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    @Override
    public void onResume() {
        if (fitnessService != null)
            fitnessService.updateStepCount();
        if (person != null) {
            setStepGoalView();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DESTROYED");
        if (person != null) {
            dataIOStream.savePerson(person);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "On Pause");
        if (person != null)
            dataIOStream.savePerson(person);
    }



    // Set the test of the goal portion of the view.
    public void setStepGoalView() {
        if (person != null) {
            String goalText = " / " + Integer.toString(person.getGoal());
            textGoal.setText(goalText);
        }
        Log.d(TAG, "in setStepGoalView");
    }

    public void buildPerson(String email) {
        if (dataIOStream != null) {
            Log.d(TAG, "in buildPerson: loading from dataIOStream");
            this.person = dataIOStream.loadPerson();
        } else {
            Log.d(TAG, "in buildPerson: build a new person (dataIOStream == null)");
            this.person = new Person(email);
        }
        Toast.makeText(MainActivity.this, email, Toast.LENGTH_LONG).show();
    }

    /**
     * Check if we are using the system clock or the artificial time right now.
     * @return
     */
    public boolean checkUseSystemClock() {
        return useSystemClock;
    }


    private void signIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        if (GoogleSignIn.getLastSignedInAccount(this) != null){
            // if has signed in already, skip sign in screen
            return;
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d(TAG, "handleSignInResult");
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            buildPerson(account.getEmail());
            runFitnessService();
            registerDialogManager();
            notificationService.subscribeToNotificationsTopic(FirestoreChatAdapter.convertToPercent(person.getEmailAcc()), this);
            //updateUI(accoutn);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            buildPerson(null);
            //updateUI(null);
        }
    }

    private void runFitnessService() {
        // register a blueprint with the factory under a key
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity activity) {
                return new GoogleFitAdapter(activity);
            }
        });

        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        // Build a mockFitnessService to replace the actual fitness service when we want to simulate steps.
        mockFitnessService = new FitnessMock(this, person);

        fitnessService.setup();
        fitnessService.updateStepCount();
    }

    private void registerDialogManager() {
        Log.d(TAG, "registerDialogManager");
        if (dataIOStream != null) {
            Log.d(TAG, "registerDialogManager: dataIOStream != null");
            this.dialogManager = new DialogManager(this, dataIOStream, person);
        } else {
            Log.d(TAG, "registerDialogManager: dataIOStream == null");
            String email = GoogleSignIn.getLastSignedInAccount(this).getEmail();
            dataIOStream = new DataIOStream(this, email);
            this.dialogManager = new DialogManager(this, dataIOStream, person);
        }

        fitnessService.register(dialogManager);
        mockFitnessService.register(dialogManager);
        fitnessService.register(this);
        mockFitnessService.register(this);
    }

    public void displayStatsDialog(PlannedWalk plannedWalk) {
        statsDialog = new StatsDialog();
        Bundle bundle = new Bundle();
        bundle.putDouble("steps", plannedWalk.getSteps());
        bundle.putDouble("distance", plannedWalk.getDistance());
        bundle.putDouble("speed", plannedWalk.getSpeed());
        bundle.putDouble("time", plannedWalk.getTime());
        statsDialog.setArguments(bundle);
        statsDialog.show(getSupportFragmentManager(), "stats dialog");
    }
}

