package com.example.drinksafe;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SMSmain extends Activity {
    LocationManager lm;
    static String PHONE_NUMBER;
    static String MESSAGE = "I'm drunken!! http://maps.google.com/?q=";
    private int SIM_STATE;

    TextView tv;
    ToggleButton tb;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Intent i = getIntent();
        PHONE_NUMBER = i.getStringExtra("phone");

        // Location 제공자에서 정보를 얻어오기(GPS)
        // 1. Location을 사용하기 위한 권한을 얻어와야한다 AndroidManifest.xml
        //     ACCESS_FINE_LOCATION : NETWORK_PROVIDER, GPS_PROVIDER
        //     ACCESS_COARSE_LOCATION : NETWORK_PROVIDER
        // 2. LocationManager 를 통해서 원하는 제공자의 리스너 등록
        // 3. GPS 는 에뮬레이터에서는 기본적으로 동작하지 않는다
        // 4. 실내에서는 GPS_PROVIDER 를 요청해도 응답이 없다.  특별한 처리를 안하면 아무리 시간이 지나도
        //    응답이 없다.
        //    해결방법은
        //     ① 타이머를 설정하여 GPS_PROVIDER 에서 일정시간 응답이 없는 경우 NETWORK_PROVIDER로 전환
        //     ② 혹은, 둘다 한꺼번헤 호출하여 들어오는 값을 사용하는 방식.
        // LocationManager 객체를 얻어온다
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                100, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                100, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendSMS();
    } // end of onCreate
    protected void onDestroy(){
        super.onDestroy();
        lm.removeUpdates(mLocationListener);
    }
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.

            str=latitude+","+longitude;
            MESSAGE = MESSAGE+str;
        }
        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };
    /**
     * When sendSMS button clicked, do the below job
     *
     * @param
     */
    public void sendSMS() {
        if (isSimExists()) {

            try {

                String SENT = "SMS_SENT";

                PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                        new Intent(SENT), 0);

                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        int resultCode = getResultCode();
                        switch (resultCode) {
                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS sent",
                                        Toast.LENGTH_LONG).show();
                                // When sms sent successfully, start service to
                                // insert sent message

                                Intent intent = new Intent(
                                        "com.example.drinksafe.SentSmsLogger");
                                intent.putExtra(Constants.KEY_PHONE_NUMBER,
                                        PHONE_NUMBER);
                                intent.putExtra(Constants.KEY_MESSAGE, MESSAGE);
                                intent.setPackage("com.example.drinksafe");
                                startService(intent);

                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(getBaseContext(), "Generic failure",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(getBaseContext(), "No service",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Toast.makeText(getBaseContext(), "Null PDU",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(getBaseContext(), "Radio off",
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }, new IntentFilter(SENT));

                SmsManager smsMgr = SmsManager.getDefault();
                smsMgr.sendTextMessage(PHONE_NUMBER, null, MESSAGE, sentPI,
                        null);

            } catch (Exception e) {
                Toast.makeText(this,
                        e.getMessage() + "!\n" + "Failed to send SMS",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, getSimState() + " " + "Cannot send SMS",
                    Toast.LENGTH_LONG).show();
        }
        finish();
    }

    /**
     * @return true if SIM card exists false if SIM card is locked or doesn't
     *         exists <br/>
     * <br/>
     *         <b>Note:</b> This method requires permissions <b>
     *         "android.permission.READ_PHONE_STATE" </b>
     */
    private boolean isSimExists() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        SIM_STATE = telephonyManager.getSimState();

        if (SIM_STATE == TelephonyManager.SIM_STATE_READY)
            return true;

        return false;
    }

    /**
     * Get simcard state
     *
     * @return
     */
    private String getSimState() {
        switch (SIM_STATE) {
            case TelephonyManager.SIM_STATE_ABSENT: // SimState =
                return "No Sim Found!"; // "No Sim Found!";
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED: // SimState =
                // "Network Locked!";
                return "Network Locked!";
            case TelephonyManager.SIM_STATE_PIN_REQUIRED: // SimState =
                // "PIN Required to access SIM!";
                return "PIN Required to access SIM!";
            case TelephonyManager.SIM_STATE_PUK_REQUIRED: // SimState =
                // "PUK Required to access SIM!";
                // // Personal
                // Unblocking Code
                return "PUK Required to access SIM!";
            case TelephonyManager.SIM_STATE_UNKNOWN: // SimState =
                // "Unknown SIM State!";
                return "Unknown SIM State!";
        }
        return null;
    }

}
