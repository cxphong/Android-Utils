//dpToPixel()
//pxelToDp()
//screenWidth()
//screenHeight()
//makeScreenAlwaysOn();
//getBattery()
//getTotalMemory()
//getFreeMemory()
//isPhoneOrTablet()
//getModel()
//getAndroidversion()
//getIPAddress()
//getBatteryStatus()
//getLanguage()
//getUserName()
//getPhoneNumber()
//getCurrentWifi()
//getSIMInfo();
//getNumberPhotos()
//getNumberImage()
//getNumberVideo()

package common.android.fiot.androidcommon;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class AndroidUtils {

	/**
	 * Keep screen always on
	 * Call in @onCreate() of current Activity, call befor @setContentView()	
	 */
	public static void keepScreenAlwaysOn() {
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public static float dpToPx(Context context, int dp) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
	}
}