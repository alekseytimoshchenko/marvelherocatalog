package com.krokosha.marvelhero.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.krokosha.marvelhero.SingletonHolder
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ConnectivityMonitor private constructor(ctx: Context): ConnectivityObservable {
    companion object: SingletonHolder<ConnectivityMonitor, Context>(::ConnectivityMonitor)
    private val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityObservable.Status> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch { send(ConnectivityObservable.Status.Available) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                launch { send(ConnectivityObservable.Status.Unavailable) }
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        if (connectivityManager.activeNetwork == null) {
            launch { send(ConnectivityObservable.Status.Unavailable) }
        }

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()
}