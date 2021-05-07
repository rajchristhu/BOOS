package com.ceseagod.showcase.map

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils


class AppUtils {


    object LocationConstants {
        val SUCCESS_RESULT = 0

        val FAILURE_RESULT = 1

        val PACKAGE_NAME = "com.sample.sishin.maplocation"

        val RECEIVER = "$PACKAGE_NAME.RECEIVER"

        val RESULT_DATA_KEY = "$PACKAGE_NAME.RESULT_DATA_KEY"

        val LOCATION_DATA_EXTRA = "$PACKAGE_NAME.LOCATION_DATA_EXTRA"

        val LOCATION_DATA_AREA = "$PACKAGE_NAME.LOCATION_DATA_AREA"
        val LOCATION_DATA_CITY = "$PACKAGE_NAME.LOCATION_DATA_CITY"
        val LOCATION_DATA_STREET = "$PACKAGE_NAME.LOCATION_DATA_STREET"


    }

    companion object {


        @SuppressLint("ObsoleteSdkInt")
        fun hasLollipop(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        }

        @SuppressLint("ObsoleteSdkInt")
        fun isLocationEnabled(context: Context): Boolean {
            var locationMode = 0
            val locationProviders: String

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    locationMode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)

                } catch (e: Settings.SettingNotFoundException) {
                    e.printStackTrace()
                }

                return locationMode != Settings.Secure.LOCATION_MODE_OFF
            } else {
                locationProviders =
                    Settings.Secure.getString(context.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
                return !TextUtils.isEmpty(locationProviders)
            }
        }
    }

}
