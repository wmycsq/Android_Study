// IServiceManager.aidl
package com.wmycsq.androidstudy;

// Declare any non-default types here with import statements

interface IServiceManager {
    IBinder getService(String name);
}
