package br.com.ladoleste.simpleredditclient.ui.customtabs

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.customtabs.CustomTabsClient
import android.support.customtabs.CustomTabsIntent
import android.support.customtabs.CustomTabsServiceConnection
import android.support.customtabs.CustomTabsSession

/**
 * An adaptation of the official sample
 */
class CustomTabsHelper {

    private var customTabsSession: CustomTabsSession? = null
    private var client: CustomTabsClient? = null
    private var connection: CustomTabsServiceConnection? = null
    private var connectionCallback: ConnectionCallback? = null

    /**
     * Creates or retrieves an exiting CustomTabsSession
     *
     * @return a CustomTabsSession
     */
    private val session: CustomTabsSession?
        get() {
            if (client == null) {
                customTabsSession = null
            } else if (customTabsSession == null) {
                customTabsSession = client!!.newSession(null)
            }
            return customTabsSession
        }

    /**
     * Unbinds the Activity from the Custom Tabs Service
     *
     * @param activity the activity that is connected to the service
     */
    fun unbindCustomTabsService(activity: Activity) {
        if (connection == null) {
            return
        }
        activity.unbindService(connection)
        client = null
        customTabsSession = null
    }

    @Suppress("unused")
            /**
             * Register a Callback to be called when connected or disconnected from the Custom Tabs Service
             */
    fun setConnectionCallback(connectionCallback: ConnectionCallback) {
        this.connectionCallback = connectionCallback
    }

    /**
     * Binds the Activity to the Custom Tabs Service
     *
     * @param activity the activity to be bound to the service
     */
    fun bindCustomTabsService(activity: Activity) {
        if (client != null) {
            return
        }

        val packageName = CustomTabsPackageHelper.getPackageNameToUse(activity) ?: return
        connection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
                this@CustomTabsHelper.client = client
                this@CustomTabsHelper.client!!.warmup(0L)
                if (connectionCallback != null) {
                    connectionCallback!!.onCustomTabsConnected()
                }
                //Initialize a session as soon as possible.
                session
            }

            override fun onServiceDisconnected(name: ComponentName) {
                client = null
                if (connectionCallback != null) {
                    connectionCallback!!.onCustomTabsDisconnected()
                }
            }

            override fun onBindingDied(name: ComponentName) {
                client = null
                if (connectionCallback != null) {
                    connectionCallback!!.onCustomTabsDisconnected()
                }
            }
        }
        CustomTabsClient.bindCustomTabsService(activity, packageName, connection)
    }

    fun mayLaunchUrl(uri: Uri): Boolean {
        if (client == null) {
            return false
        }
        val session = session
        return session != null && session.mayLaunchUrl(uri, null, null)
    }

    /**
     * A Callback for when the service is connected or disconnected. Use those callbacks to
     * handle UI changes when the service is connected or disconnected
     */
    interface ConnectionCallback {
        /**
         * Called when the service is connected
         */
        fun onCustomTabsConnected()

        /**
         * Called when the service is disconnected
         */
        fun onCustomTabsDisconnected()
    }

    /**
     * To be used as a fallback to open the Uri when Custom Tabs is not available
     */
    interface CustomTabFallback {
        /**
         * @param context The Activity that wants to open the Uri
         * @param uri     The uri to be opened by the fallback
         */
        fun openUri(context: Context, uri: Uri)
    }

    companion object {

        /**
         * Opens the URL on a Custom Tab if possible. Otherwise fallsback to opening it on a WebView
         *
         * @param context          The host activity
         * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available
         * @param uri              the Uri to be opened
         * @param fallback         a CustomTabFallback to be used if Custom Tabs is not available
         */
        fun openCustomTab(context: Context,
                          customTabsIntent: CustomTabsIntent,
                          uri: Uri,
                          fallback: CustomTabFallback?) {
            val packageName = CustomTabsPackageHelper.getPackageNameToUse(context)

            //If we cant find a package name, it means there's no browser that supports
            //Chrome Custom Tabs installed. So, we fallback to the web-view
            if (packageName == null) {
                fallback?.openUri(context, uri)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    customTabsIntent.intent
                            .putExtra(Intent.EXTRA_REFERRER,
                                    Uri.parse(Intent.URI_ANDROID_APP_SCHEME.toString() + "//" + context.packageName))
                }

                customTabsIntent.intent.`package` = packageName
                customTabsIntent.launchUrl(context, uri)
            }
        }
    }
}