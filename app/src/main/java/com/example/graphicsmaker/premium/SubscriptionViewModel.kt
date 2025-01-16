package com.example.graphicsmaker.premium

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetailsParams
import vocsy.ads.AdsHandler


class SubscriptionViewModel(application: Application) : AndroidViewModel(application) {
    val billingClient: BillingClient = BillingClient.newBuilder(application)
        .setListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
            } else {
                // Handle other response codes
            }
        }
        .enablePendingPurchases()
        .build()

    private val _isPremiumActive = MutableLiveData(false)
    val isPremiumActive: LiveData<Boolean> get() = _isPremiumActive

    private val _subscriptionPrice = MutableLiveData<String>()
    val subscriptionPrice: LiveData<String> get() = _subscriptionPrice

    private val retryPolicy = RetryPolicy(maxAttempts = 3)
    private var retryCount = 0

    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                if (retryCount < retryPolicy.maxAttempts) {
                    retryCount++
                    billingClient.startConnection(this)
                }
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryAvailableSubscriptions()
                } else {
                    // Handle setup failure
                }
            }
        })

        AdsHandler.setAdsOn(isPremiumActive.value?:false)
    }

    fun queryAvailableSubscriptions() {
        val skuList = listOf("premium_subscription") // Replace with your subscription ID
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.SUBS)

        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                skuDetailsList.forEach { skuDetails ->
                    Log.d("Billing", "Product ID: ${skuDetails.sku}, Price: ${skuDetails.price}")
                    // Update the price LiveData
                    _subscriptionPrice.postValue(skuDetails.price)
                }
            } else {
                Log.e("Billing", "Failed to query SKU details: ${billingResult.debugMessage}")
                _subscriptionPrice.postValue("Unavailable")
            }
        }
    }

    fun subscribePremium(activity: Activity) {
        val skuList = listOf("premium_subscription")
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.SUBS)

        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                val skuDetails = skuDetailsList[0]
                val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetails)
                    .build()
                billingClient.launchBillingFlow(activity, flowParams)
            } else {
                Log.e("Billing", "Error launching billing flow: ${billingResult.debugMessage}")
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            _isPremiumActive.value = true
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.d("Billing", "Purchase acknowledged")
                    } else {
                        Log.e("Billing", "Failed to acknowledge purchase: ${billingResult.debugMessage}")
                    }
                }
            }
        } else {
            Log.w("Billing", "Purchase not completed: ${purchase.purchaseState}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (billingClient.isReady) {
            billingClient.endConnection()
        }
    }

    private data class RetryPolicy(val maxAttempts: Int)
}



//class SubscriptionViewModel(application: Application) : AndroidViewModel(application) {
//    private val billingClient: BillingClient = BillingClient.newBuilder(application)
//        .setListener { billingResult, purchases ->
//            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
//                for (purchase in purchases) {
//                    handlePurchase(purchase)
//                }
//            } else {
//                // Handle other response codes
//            }
//        }
//        .enablePendingPurchases()
//        .build()
//
//    // LiveData to observe subscription status
//    private val _isPremiumActive = MutableLiveData(false)
//    val isPremiumActive: LiveData<Boolean> get() = _isPremiumActive
//
//    // Retry connection logic
//    private val retryPolicy = RetryPolicy(maxAttempts = 3)
//    private var retryCount = 0
//
//    init {
//        billingClient.startConnection(object : BillingClientStateListener {
//            override fun onBillingServiceDisconnected() {
//                // Retry connecting to the BillingClient
//                if (retryCount < retryPolicy.maxAttempts) {
//                    retryCount++
//                    billingClient.startConnection(this)
//                }
//            }
//
//            override fun onBillingSetupFinished(billingResult: BillingResult) {
//                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                    // BillingClient is ready
//                    queryAvailableSubscriptions()
//                } else {
//                    // Handle setup failure
//                }
//            }
//        })
//
//        AdsHandler.setAdsOn(isPremiumActive.value == false)
//    }
//
//    private fun queryAvailableSubscriptions() {
//        val skuList = listOf("premium_subscription") // Replace with your subscription ID
//        val params = SkuDetailsParams.newBuilder()
//            .setSkusList(skuList)
//            .setType(BillingClient.SkuType.SUBS)
//
//        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
//            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
//                // Handle SKU details, e.g., display prices in the UI
//                skuDetailsList.forEach { skuDetails ->
//                    Log.d("Billing", "Product ID: ${skuDetails.sku}, Price: ${skuDetails.price}")
//                }
//            } else {
//                // Handle error or empty list
//                Log.e("Billing", "Failed to query SKU details: ${billingResult.debugMessage}")
//            }
//        }
//    }
//
//    fun subscribePremium(activity: Activity) {
//        val skuList = listOf("premium_subscription") // Replace with your subscription ID
//        val params = SkuDetailsParams.newBuilder()
//            .setSkusList(skuList)
//            .setType(BillingClient.SkuType.SUBS)
//
//        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
//            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
//                val skuDetails = skuDetailsList[0] // Assume you have only one subscription
//                val flowParams = BillingFlowParams.newBuilder()
//                    .setSkuDetails(skuDetails)
//                    .build()
//                billingClient.launchBillingFlow(activity, flowParams)
//            } else {
//                // Handle error querying SKU details
//                Log.e("Billing", "Error launching billing flow: ${billingResult.debugMessage}")
//            }
//        }
//    }
//
//    private fun handlePurchase(purchase: Purchase) {
//        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
//            // Grant the user premium access
//            _isPremiumActive.value = true
//
//            // Acknowledge the purchase to prevent it from being refunded
//            if (!purchase.isAcknowledged) {
//                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
//                    .setPurchaseToken(purchase.purchaseToken)
//                    .build()
//                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
//                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                        Log.d("Billing", "Purchase acknowledged")
//                    } else {
//                        Log.e("Billing", "Failed to acknowledge purchase: ${billingResult.debugMessage}")
//                    }
//                }
//            }
//        } else {
//            // Handle other purchase states (e.g., PENDING, UNSPECIFIED_STATE)
//            Log.w("Billing", "Purchase not completed: ${purchase.purchaseState}")
//        }
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        // Disconnect the BillingClient when ViewModel is cleared
//        if (billingClient.isReady) {
//            billingClient.endConnection()
//        }
//    }
//
//    private data class RetryPolicy(val maxAttempts: Int)
//}

