package common.android.fiot.androidcommon;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by caoxuanphong on    8/16/16.
 */
public class GPSUtils {
    private static final String TAG = "GPSUtils";
    private static GPSUtilsListener listener;
    protected static GoogleApiClient mGoogleApiClient;
    protected static LocationRequest mLocationRequest;
    private static MODE mode;

    private enum MODE {
        none,
        request,
        watch
    }

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    public interface GPSUtilsListener {
        void didGetGPS(Location location);
    }

    public static boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public synchronized static void startWatchLocation(Context context, GPSUtilsListener l) {
        listener = l;
        mode = MODE.watch;

        try {
            if (mGoogleApiClient != null &&
                    (mGoogleApiClient.isConnected() ||
                            mGoogleApiClient.isConnecting())) {
                mGoogleApiClient.disconnect();
                stopLocationUpdates();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        buildGoogleApiClient(context);
        mGoogleApiClient.connect();
    }

    public synchronized static void stopWatchLocation() {
        mode = MODE.none;
    }

    public synchronized  static void requestLocation(Context context, GPSUtilsListener l) {
        listener = l;
        mode = MODE.request;

        try {
            if (mGoogleApiClient != null &&
                    (mGoogleApiClient.isConnected() ||
                            mGoogleApiClient.isConnecting())) {
                mGoogleApiClient.disconnect();
                stopLocationUpdates();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        buildGoogleApiClient(context);
        mGoogleApiClient.connect();
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized static void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(connectionCallback)
                .addOnConnectionFailedListener(connectionFailedListener)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    private static void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, locationListener);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private static void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener);
    }

    private static void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    static GoogleApiClient.ConnectionCallbacks connectionCallback = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                Log.i(TAG, "old: lat: " + mLastLocation.getLatitude() + ", long: " + mLastLocation.getLongitude());
            }

            startLocationUpdates();
        }

        @Override
        public void onConnectionSuspended(int i) {
                mGoogleApiClient.connect();
        }
    };

    static GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
        }
    };

    static LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (mode == MODE.request) {
                stopLocationUpdates();
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                }

                if (listener != null) {
                    listener.didGetGPS(location);
                }
            } else if (mode == MODE.none) {
                stopLocationUpdates();
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                }
            } else if (mode == MODE.watch) {
                if (listener != null) {
                    listener.didGetGPS(location);
                }
            }
        }
    };
}


