package com.example.graphicsmaker.main

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

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val billingClient: BillingClient = BillingClient.newBuilder(application)
        .setListener(::onPurchasesUpdated)
        .enablePendingPurchases()
        .build()

    private val _isPremiumActive = MutableLiveData<Boolean>(false)
    val isPremiumActive: LiveData<Boolean> get() = _isPremiumActive

    private val _subscriptionPrice = MutableLiveData<String>()
    val subscriptionPrice: LiveData<String> get() = _subscriptionPrice

    private val retryPolicy = RetryPolicy(maxAttempts = 3)
    private var retryCount = 0

    init {
        initializeBillingClient()
    }

    private fun initializeBillingClient() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                if (retryCount < retryPolicy.maxAttempts) {
                    retryCount++
                    billingClient.startConnection(this)
                } else {
                    Log.e("Billing", "Max retry attempts reached for billing service connection.")
                }
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryAvailableSubscriptions()
                } else {
                    Log.e("Billing", "Billing setup failed: ${billingResult.debugMessage}")
                }
            }
        })
    }

    private fun queryAvailableSubscriptions() {
        val skuList = listOf("premium_subscription") // Replace with actual subscription IDs
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.SUBS)

        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                skuDetailsList.forEach { skuDetails ->
                    Log.d("Billing", "Product ID: ${skuDetails.sku}, Price: ${skuDetails.price}")
                    _subscriptionPrice.postValue(skuDetails.price)
                }
            } else {
                Log.e("Billing", "Failed to query SKU details: ${billingResult.debugMessage}")
                _subscriptionPrice.postValue("Unavailable")
            }
        }
    }

    fun subscribeToPremium(activity: Activity) {
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

    private fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            purchases.forEach(::handlePurchase)
        } else {
            Log.e("Billing", "Purchases update failed: ${billingResult.debugMessage}")
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            _isPremiumActive.value = true
            if (!purchase.isAcknowledged) {
                val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(acknowledgeParams) { result ->
                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.d("Billing", "Purchase acknowledged successfully.")
                    } else {
                        Log.e("Billing", "Failed to acknowledge purchase: ${result.debugMessage}")
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
