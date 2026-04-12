package edu.snhu.cs360.inventoryapp;

import android.telephony.SmsManager;


// Helper class for sending SMS messages
public class SmsService {

// Sends an SMS using the phone number and message passed in
    public static void sendSms(String phone, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null, message, null, null);
    }
}
