// IConnectionService.aidl
package com.wmycsq.androidstudy;

/*
连接服务
*/
interface IConnectionService {

    oneway void connect();

    void disconnect();

    boolean isConnect();
}
