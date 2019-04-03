package com.example.team13.personalbest_team13_skeleton.fitness;

import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.team13.personalbest_team13_skeleton.MainActivity;
import com.example.team13.personalbest_team13_skeleton.observer.ISubject;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class GoogleFitAdapter implements FitnessService {

    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private final String TAG = "GoogleFitAdapter";

    private MainActivity activity;
    private Collection<FitnessObserver> observers;

    public GoogleFitAdapter(MainActivity activity) {
        this.activity = activity;
        observers = new ArrayList<FitnessObserver>();
    }

    public void setup() {
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(activity), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    activity, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(activity),
                    fitnessOptions);
        } else {
            updateStepCount();
            startRecording();
        }
    }

    private void startRecording() {
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(activity);

        if (lastSignedInAccount == null) {
            return;
        }

        //String personEmail = lastSignedInAccount.getEmail();
        //activity.getEmail(personEmail);

        Fitness.getRecordingClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully subscribed!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "There was a problem subscribing.");
                    }
                });
    }

    // XXX (Nate): On update step count check if the user has completed their goal.

    /**
     * Reads the current daily step total, computed from midnight of the current day on the device's
     * current timezone.
     */
    public void updateStepCount() {
        Log.d(TAG, "Update step count");
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(activity);
        if (lastSignedInAccount == null) {
            return;
        }

        Fitness.getHistoryClient(activity, lastSignedInAccount)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                Log.d(TAG, dataSet.toString());
                                long total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();

                                //activity.setStepCount(total);
                                notifyRegisteredDevices((int)total);

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "There was a problem getting the step count.", e);
                            }
                        });
    }

    /**
     * Notify all the listeners that the steps have been updated.
     * @param steps
     */
    public void notifyRegisteredDevices(int steps) {
        for (FitnessObserver o : observers) {
            o.stepsUpdated(steps);
        }
    }

    public void getYesterdaysSteps() {
        // Begin by creating the query.
        DataReadRequest readRequest = queryFitnessData(true);

        // Invoke the History API to fetch the data with the query
        Fitness.getHistoryClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .readData(readRequest)
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                // For the sake of the sample, we'll print the data so we can see what we just
                                // added. In general, logging fitness information should be avoided for privacy
                                // reasons.
                                printData(dataReadResponse, true);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "There was a problem reading the data.", e);
                            }
                        });
    }

    public void getWeekData() {
        // Begin by creating the query.
        DataReadRequest readRequest = queryFitnessData(false);

        // Invoke the History API to fetch the data with the query
        Fitness.getHistoryClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .readData(readRequest)
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                // For the sake of the sample, we'll print the data so we can see what we just
                                // added. In general, logging fitness information should be avoided for privacy
                                // reasons.
                                printData(dataReadResponse, false);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "There was a problem reading the data.", e);
                            }
                        });
    }

    public DataReadRequest queryFitnessData(boolean yesterday) {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        int interval = (yesterday) ? Calendar.DAY_OF_YEAR : Calendar.WEEK_OF_MONTH;
        int pastDay = (yesterday) ? -2 : -1;
        cal.add(interval, pastDay);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateInstance();
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest =
                new DataReadRequest.Builder()
                        // The data request can specify multiple data types to return, effectively
                        // combining multiple data queries into one call.
                        // In this example, it's very unlikely that the request is for several hundred
                        // datapoints each consisting of a few steps and a timestamp.  The more likely
                        // scenario is wanting to see how many steps were walked per day, for 7 days.
                        .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                        // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                        // bucketByTime allows for a time span, whereas bucketBySession would allow
                        // bucketing by "sessions", which would need to be defined in code.
                        .bucketByTime(1, TimeUnit.DAYS)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();
        // [END build_read_data_request]

        return readRequest;
    }

    public void printData(DataReadResponse dataReadResult, boolean yesterday) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        ArrayList<Integer> weekHistoryList = new ArrayList<>();
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(
                    TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (int i = 0; i < dataReadResult.getBuckets().size(); i++) {
                List<DataSet> dataSets = dataReadResult.getBuckets().get(i).getDataSets();
                for (DataSet dataSet : dataSets) {
                    weekHistoryList.add(dumpDataSet(dataSet));
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                weekHistoryList.add(dumpDataSet(dataSet));

            }
        }
        Log.d(TAG, "" + weekHistoryList.size());
        if (!yesterday) {
            activity.setProgress(weekHistoryList);
        } else {
            activity.setYesterdaySteps(weekHistoryList);
        }
        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    private int dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();
        int total = 0;

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                total += dp.getValue(field).asInt();
            }
        }

        return total;
    }


    @Override
    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }

    /**
     * Link the listener objects
     */
    @Override
    public void register(FitnessObserver observer) {
        observers.add(observer);
    }

    /**
     * Unlink the listener objects
     */
    @Override
    public void unregister(FitnessObserver observer) {

    }
}
